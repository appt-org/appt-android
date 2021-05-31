package nl.appt.helpers

import com.github.kittinunf.fuel.core.FuelError

data class Result<out T>(val status: Status, val data: T?, val fuelError: FuelError?){
    companion object {

        fun <T> success(data: T? = null): Result<T> {
            return Result(Status.SUCCESS, data, null)
        }

        fun <T> error(error: FuelError?, data: T? = null): Result<T> {
            return Result(Status.ERROR, data, error)
        }

        fun <T> loading(data: T? = null): Result<T> {
            return Result(Status.LOADING, data, null)
        }
    }
}

