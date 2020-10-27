package nl.appt.api

import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import nl.appt.model.Article

/**
 * Created by Jan Jaap de Groot on 26/10/2020
 * Copyright 2020 Stichting Appt
 */
class API {
    companion object {

        fun getArticles(callback: (Response<List<Article>>) -> Unit) {
            val parameters = listOf(
                "_fields" to "type,id,date,title",
                "page" to 1,
                "per_page" to 1
            )
            return getObject("posts", parameters, callback)
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