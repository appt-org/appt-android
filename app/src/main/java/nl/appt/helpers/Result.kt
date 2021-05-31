package nl.appt.helpers

import com.github.kittinunf.fuel.core.FuelError
import nl.appt.model.Block

data class Result<out T>(val status: Status, val error: FuelError?, val data: Block?) {
    companion object {

        fun <T> success(data: Block): Result<T> {
            return Result(Status.SUCCESS, null, data)
        }

        fun <T> error(error: FuelError): Result<T> {
            return Result(Status.ERROR, error, null)
        }
    }
}