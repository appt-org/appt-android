package nl.appt.api

import android.net.Uri
import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.google.gson.JsonObject
import nl.appt.model.Article
import nl.appt.model.Block
import nl.appt.model.Category
import nl.appt.model.Filters
import nl.appt.model.Tag
import nl.appt.model.Taxonomy
import nl.appt.model.UserLogin
import nl.appt.model.UserRegistration
import nl.appt.model.UserResponse
import nl.appt.model.ids
import nl.appt.model.selected

/**
 * Created by Jan Jaap de Groot on 26/10/2020
 * Copyright 2020 Stichting Appt
 */

class API {
    companion object {

        /** Auth **/

        fun userRegistration(data: UserRegistration, callback: (Response<UserResponse>) -> Unit) {
            postObject(ApiConst.REGISTRATION_PATH, data, callback)
        }

        fun userLogin(data: UserLogin, callback: (Response<JsonObject>) -> Unit) {
            postObject(ApiConst.LOGIN_PATH, data, callback)
        }

        fun userLogout(data: Int, callback: (Response<Any>) -> Unit) {
            val parameters = arrayListOf(
                ApiConst.PARAM_ID to data
            )
            getObject(ApiConst.LOGOUT_PATH, parameters, callback)
        }

        fun userDelete(data: Int, callback: (Response<UserResponse>) -> Unit) {
            val parameters = arrayListOf(
                ApiConst.PARAM_REASSIGN to "",
                ApiConst.PARAM_FORCE to true

            )
            deleteObject(ApiConst.DELETE_PATH + data.toString(), parameters, callback)
        }

        fun sendResetPasswordEmail(
            email: String,
            callback: (Response<Map<String, String>>) -> Unit
        ) {
            val parameters = arrayListOf(
                ApiConst.PARAM_USER_LOGIN to email
            )
            getObject(ApiConst.RESET_PASSWORD_PATH, parameters, callback)
        }

        fun setNewPassword(
            key: String,
            login: String,
            password: String,
            callback: (Response<Any>) -> Unit
        ) {
            val data = arrayListOf(
                ApiConst.PARAM_KEY to key,
                ApiConst.PARAM_PASSWORD to password,
                ApiConst.PARAM_LOGIN to login
            )
            postPassword<Any>(ApiConst.NEW_PASSWORD_PATH, data, callback)
        }

        /** Block **/

        fun getBlocks(path: String, callback: (Response<Block>) -> Unit) {
            getBlocks(path, null, callback)
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
            (ApiConst.ARTICLE_PATH + path).httpGet(parameters)
                .responseObject<T> { _, response, result ->
                    callback(Response.from(response, result))
                }
        }

        private inline fun <reified T : Any> postObject(
            path: String,
            data: Any,
            crossinline callback: (Response<T>) -> Unit
        ) {
            (ApiConst.ARTICLE_PATH + path)
                .httpPost()
                .jsonBody(data)
                .responseObject<T> { _, response, result ->
                    callback(Response.from(response, result))
                }
        }

        private inline fun <reified T : Any> postPassword(
            path: String,
            data: Parameters?,
            crossinline callback: (Response<Any>) -> Unit
        ) {
            (ApiConst.ARTICLE_PATH + path)
                .httpPost(data)
                .responseString { _, response, result ->
                    callback(Response.from(response, result))
                }
        }

        private inline fun <reified T : Any> deleteObject(
            path: String,
            data: Parameters?,
            crossinline callback: (Response<T>) -> Unit
        ) {
            (ApiConst.ARTICLE_PATH + path)
                .httpDelete(data)
                .responseObject<T> { _, response, result ->
                    callback(Response.from(response, result))
                }
        }

        private inline fun <reified T : Any> getBlocks(
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