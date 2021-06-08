package nl.appt.auth.reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.auth.ValidationManager
import nl.appt.helpers.Result

private const val SUCCEED_MESSAGE = "Password successfully changed!"

class NewPasswordViewModel : ViewModel() {

    enum class FieldStates {
        PASSWORD_ERROR, PASSWORD_VALID
    }

    private val _response = MutableLiveData<Result<String>>()

    val response: LiveData<Result<String>> = _response

    private val _errorState = MutableLiveData<FieldStates>()

    val errorState: LiveData<FieldStates> = _errorState

    fun setNewPassword(key: String, login: String, password: String) {
        API.setNewPassword(key, login, password) { response ->
            val message = response.result.toString().replace("\"", "")
            if (message == SUCCEED_MESSAGE) {
                _response.value = Result.success(message)
            } else {
                _response.value = Result.error(null, message)
            }
        }
    }

    fun checkPasswordField(password: String): Boolean {
        return if (!ValidationManager.isValidPassword(password)) {
            _errorState.value = FieldStates.PASSWORD_ERROR
            false
        } else {
            _errorState.value = FieldStates.PASSWORD_VALID
            true
        }
    }
}