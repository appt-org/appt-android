package nl.appt.api

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result

/**
 * Created by Jan Jaap de Groot on 27/10/2020
 * Copyright 2020 Stichting Appt
 */
class Response<T>(
    val result: T? = null,
    val total: Int? = null,
    val pages: Int? = null,
    val error: Any? = null)
{
    companion object {
        fun <T : Any> from(response: com.github.kittinunf.fuel.core.Response, result: Result<T, FuelError>): Response<T> {
            return Response(
                result = result.component1(),
                total = response.header("X-WP-Total").firstOrNull()?.toIntOrNull(),
                pages = response.header("X-WP-TotalPages").firstOrNull()?.toIntOrNull(),
                error = result.component2()
            )
        }
    }
}