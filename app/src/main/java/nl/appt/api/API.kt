package nl.appt.api

import android.net.Uri
import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import nl.appt.model.*

/**
 * Created by Jan Jaap de Groot on 26/10/2020
 * Copyright 2020 Stichting Appt
 */

private const val ARTICLE_PATH = "wp-json/wp/v2/"

class API {
    companion object {

        /** Block **/

        fun getBlocks(path: String, callback: (Response<Block>) -> Unit) {
            getBlockObject(path, null, callback)
        }

        /** Articles **/

        fun getArticles(
            type: Article.Type,
            filters: Filters? = null,
            slug: String? = null,
            parentId: Int? = null,
            page: Int = 1,
            callback: (Response<List<Article>>) -> Unit
        ) {
            val parameters = arrayListOf(
                "_fields" to "type,id,date,title,link",
                "page" to page,
                "per_page" to 20
            )

            filters?.categories?.selected?.ids?.let { categories ->
                if (categories.isNotEmpty()) {
                    parameters.add("categories" to categories.joinToString(","))
                }
            }

            filters?.tags?.selected?.ids?.let { tags ->
                if (tags.isNotEmpty()) {
                    parameters.add("tags" to tags.joinToString(","))
                }
            }

            slug?.let { slug ->
                parameters.add("slug" to slug)
            }

            parentId?.let { id ->
                parameters.add("parent" to id)
            }

            if (type == Article.Type.PAGE) {
                parameters.add("orderby" to "title")
                parameters.add("order" to "asc")
            } else if (type == Article.Type.POST) {
                parameters.add("orderby" to "date")
                parameters.add("order" to "desc")
            }

            return getObject(type.path, parameters, callback)
        }

        fun getArticle(type: Article.Type, id: Int, callback: (Response<Article>) -> Unit) {
            val parameters = listOf(
                "_fields" to "type,id,date,modified,link,title,content,author,tags,categories"
            )
            return getObject("${type.path}/$id", parameters, callback)
        }

        fun getArticle(type: Article.Type, slug: String, callback: (Response<Article>) -> Unit) {
            val parameters = listOf(
                "_fields" to "type,id,date,modified,link,title,content,author,tags,categories",
                "slug" to slug,
                "per_page" to 1
            )

            // Get articles by slug, return first article.
            getObject<List<Article>>(type.path, parameters) { response ->
                var article: Article? = null
                response.result?.firstOrNull()?.let {
                    article = it
                }
                callback(Response(article, response.total, response.pages, response.error))
            }
        }

        fun getArticle(type: Article.Type, uri: Uri, callback: (Response<Article>) -> Unit) {
            getArticles(type = type, slug = uri.lastPathSegment) { response ->
                response.result?.let { articles ->
                    articles.firstOrNull {
                        it.link?.contains(uri.toString(), true) ?: false
                    }?.let { article ->
                        getArticle(type = type, id = article.id, callback = callback)
                    }
                }

                response.error?.let { error ->
                    callback(Response(error = error))
                }
            }
        }

        /** Filters **/

        private fun getTaxonomies(path: String, callback: (Response<ArrayList<Taxonomy>>) -> Unit) {
            val parameters = listOf(
                "_fields" to "id,count,name,description",
            )
            return getObject(path, parameters, callback)
        }

        private fun getCategories(callback: (Response<ArrayList<Category>>) -> Unit) {
            return getTaxonomies("categories", callback)
        }

        private fun getTags(callback: (Response<ArrayList<Tag>>) -> Unit) {
            return getTaxonomies("tags", callback)
        }

        fun getFilters(callback: (Response<Filters>) -> Unit) {
            getCategories { response1 ->
                response1.result?.let { categories ->
                    getTags { response2 ->
                        response2.result?.let { tags ->
                            callback(Response(result = Filters(categories, tags)))
                        } ?: run {
                            callback(Response(error = response2.error))
                        }
                    }
                } ?: run {
                    callback(Response(error = response1.error))
                }
            }
        }

        /** Helper methods **/

        private inline fun <reified T : Any> getObject(
            path: String,
            parameters: Parameters?,
            crossinline callback: (Response<T>) -> Unit
        ) {
            (ARTICLE_PATH + path).httpGet(parameters).responseObject<T> { _, response, result ->
                callback(Response.from(response, result))
            }
        }

        private inline fun <reified T : Any> postObject(
            path: String,
            data: Any,
            crossinline callback: (Response<T>) -> Unit
        ) {
            (ARTICLE_PATH + path).httpPost().jsonBody(data)
                .responseObject<T> { _, response, result ->
                    callback(Response.from(response, result))
                }
        }

        private inline fun <reified T : Any> getBlockObject(
            path: String,
            parameters: Parameters?,
            crossinline callback: (Response<T>) -> Unit
        ) {
            path.httpGet(parameters).responseObject<T> { _, response, result ->
                callback(Response.from(response, result))
            }
        }
    }
}