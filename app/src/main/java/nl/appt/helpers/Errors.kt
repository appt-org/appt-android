package nl.appt.helpers

import com.github.kittinunf.fuel.core.FuelError

data class Errors<out T>(val error: FuelError) {
    companion object {
        fun <T> error(error: FuelError): Errors<T> {
            return Errors(error)
        }
    }
}