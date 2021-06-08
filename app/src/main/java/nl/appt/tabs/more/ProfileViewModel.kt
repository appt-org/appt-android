package nl.appt.tabs.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.helpers.Result

private const val RESPONSE_STATUS = "status"
private const val RESPONSE_STATUS_OK = "ok"
private const val RESPONSE_MESSAGE = "msg"
private const val RESPONSE_ERROR_MESSAGE = "error"

class ProfileViewModel : ViewModel() {

    enum class Event {
        DELETE, LOGOUT
    }

    private val _response = MutableLiveData<Result<Any>>()

    val response: LiveData<Result<Any>> = _response

    fun logoutUser(id: Int) {
        API.userLogout(id) { response ->
            if (response.result != null) {
                _response.value = Result.success(Event.LOGOUT)
            } else {
                _response.value = Result.error(response.error)
            }
        }
    }

    fun changePassword(email: String) {
        API.sendResetPasswordEmail(email) { response ->
            response.result?.let { data ->
                if (data[RESPONSE_STATUS] == RESPONSE_STATUS_OK) {
                    _response.value = Result.success(data[RESPONSE_MESSAGE])
                } else {
                    _response.value = Result.error(null, data[RESPONSE_ERROR_MESSAGE])
                }
            }
        }
    }

    fun deleteUser(id: Int) {
        API.userDelete(id) { response ->
            if (response.result != null) {
                _response.value = Result.success(Event.DELETE)
            } else {
                _response.value = Result.error(response.error)
            }
        }
    }

}