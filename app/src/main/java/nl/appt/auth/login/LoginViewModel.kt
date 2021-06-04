package nl.appt.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.auth.ValidationManager
import nl.appt.helpers.Result
import nl.appt.model.UserLogin
import nl.appt.model.UserResponse

class LoginViewModel: ViewModel() {

    enum class FieldStates {
        PASSWORD_ERROR, EMAIL_ERROR, PASSWORD_VALID, EMAIL_VALID, LOGIN_ERROR,
    }

    private val _loginResponse = MutableLiveData<Result<UserResponse>>()

    val loginResponse: LiveData<Result<UserResponse>> = _loginResponse

    private val _errorState = MutableLiveData<FieldStates>()

    val errorState: LiveData<FieldStates> = _errorState

    fun checkPasswordField(password: String): Boolean {
        return if (!ValidationManager.isValidPassword(password)) {
            _errorState.value = FieldStates.PASSWORD_ERROR
            false
        } else {
            _errorState.value = FieldStates.PASSWORD_VALID
            true
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

    fun userLogin(email: String, password: String) {
        val username = createUsername(email)
        API.userLogin(UserLogin(email, password, username)) { response ->
            response.result?.let { result ->
                _loginResponse.value = Result.success(result)
            }

            response.error?.let { error ->
                _loginResponse.value = Result.error(error)
            }
        }
    }

    private fun createUsername(email: String): String {
        return email.split("@")[0]
    }

}