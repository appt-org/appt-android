package nl.appt.auth.reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.auth.ValidationManager
import nl.appt.helpers.Result

private const val SUCCEED_MESSAGE = "Password successfully changed!"
private const val SPLIT_DELIMITER = "&"
private const val SUBSTRING_DELIMITER = "="

class NewPasswordViewModel : ViewModel() {

    enum class FieldStates {
        PASSWORD_ERROR, PASSWORD_VALID
    }

    private val _response = MutableLiveData<Result<String>>()

    val response: LiveData<Result<String>> = _response

    private val _errorState = MutableLiveData<FieldStates>()

    val errorState: LiveData<FieldStates> = _errorState

    fun setNewPassword(url: String, password: String){
        if (checkPasswordField(password)) {
            sendNewPassword(url, password)
        }
    }

    private fun sendNewPassword(url: String, password: String) {
        val values = url.split(SPLIT_DELIMITER)
        val key = values[1].substringAfter(SUBSTRING_DELIMITER)
        val login = values[2].substringAfter(SUBSTRING_DELIMITER)
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