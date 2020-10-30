package nl.appt.api

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
class API {
    companion object {

        /** Articles **/

        fun getArticles(page: Int, callback: (Response<List<Article>>) -> Unit) {
            val parameters = listOf(
                "_fields" to "type,id,date,title",
                "page" to page,
                "per_page" to 20
            )
            return getObject("posts", parameters, callback)
        }

        fun getArticle(id: Int, callback: (Response<Article>) -> Unit) {
            val parameters = listOf(
                "_fields" to "type,id,date,modified,link,title,content,author,tags,categories"
            )
            return getObject("posts/$id", parameters, callback)
        }

        fun getArticle(slug: String, callback: (Response<Article>) -> Unit) {
            val parameters = listOf(
                "_fields" to "type,id,date,modified,link,title,content,author,tags,categories",
                "slug" to slug,
                "per_page" to 1
            )

            // Get articles by slug, return first article.
            getObject<List<Article>>("posts", parameters) { response ->
                var article: Article? = null
                response.result?.firstOrNull()?.let {
                    article = it
                }
                callback(Response(article, response.total, response.pages, response.error))
            }
        }

        /** Filters **/

        private fun getTaxonomies(path: String, callback: (Response<List<Taxonomy>>) -> Unit) {
            val parameters = listOf(
                "_fields" to "id,count,name,description",
            )
            return getObject(path, parameters, callback)
        }

        private fun getCategories(callback: (Response<List<Category>>) -> Unit) {
            return getTaxonomies("categories", callback)
        }

        private fun getTags(callback: (Response<List<Tag>>) -> Unit) {
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

        private inline fun<reified T : Any> getObject(path: String, parameters: Parameters?, crossinline callback: (Response<T>) -> Unit) {
            path.httpGet(parameters).responseObject<T> { _, response, result ->
                callback(Response.from(response, result))
            }
        }

        private inline fun<reified T : Any> postObject(path: String, data: Any, crossinline callback: (Response<T>) -> Unit) {
            path.httpPost().jsonBody(data).responseObject<T> { _, response, result ->
                callback(Response.from(response, result))
            }
        }
    }
}