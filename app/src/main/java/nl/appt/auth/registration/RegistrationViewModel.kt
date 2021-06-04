package nl.appt.auth.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.auth.ValidationManager
import nl.appt.helpers.Result
import nl.appt.model.UserRegistration
import nl.appt.model.UserResponse

class RegistrationViewModel : ViewModel() {

    enum class FieldStates {
        PASSWORD_ERROR, EMAIL_ERROR, PASSWORD_VALID, EMAIL_VALID
    }

    private val _registrationResponse = MutableLiveData<Result<UserResponse>>()

    val registrationResponse: LiveData<Result<UserResponse>> = _registrationResponse

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

    fun userRegistration(email: String, password: String, userTypes: ArrayList<String>) {
        val username = createUsername(email)
        API.userRegistration(UserRegistration(email, username, password, userTypes)) { response ->
            response.result?.let { result ->
                _registrationResponse.value = Result.success(result)
            }

            response.error?.let { error ->
                _registrationResponse.value = Result.error(error)
            }
        }
    }

    private fun createUsername(email: String): String {
        return email.split("@")[0]
    }
}
