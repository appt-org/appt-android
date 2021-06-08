package nl.appt.auth.reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.auth.ValidationManager
import nl.appt.helpers.Result

private const val RESPONSE_STATUS = "status"
private const val RESPONSE_STATUS_OK = "ok"
private const val RESPONSE_MESSAGE = "msg"
private const val RESPONSE_ERROR_MESSAGE = "error"

class ResetPasswordViewModel : ViewModel() {

    enum class FieldStates {
        EMAIL_ERROR, EMAIL_VALID
    }

    private val _resetResponse = MutableLiveData<Result<String>>()

    val resetResponse: LiveData<Result<String>> = _resetResponse

    private val _errorState = MutableLiveData<FieldStates>()

    val errorState: LiveData<FieldStates> = _errorState

    fun resetPassword(email: String) {
        API.sendResetPasswordEmail(email) { response ->
            response.result?.let { data ->
                if (data[RESPONSE_STATUS] == RESPONSE_STATUS_OK) {
                    _resetResponse.value = Result.success(data[RESPONSE_MESSAGE])
                } else {
                    _resetResponse.value = Result.error(null, data[RESPONSE_ERROR_MESSAGE])
                }
            }
        }
    }

    fun checkEmailField(email: String): Boolean {
        return if (!ValidationManager.isValidEmail(email)) {
            _errorState.value = FieldStates.EMAIL_ERROR
            false
        } else {
            _errorState.value = FieldStates.EMAIL_VALID
            true
        }
    }
}