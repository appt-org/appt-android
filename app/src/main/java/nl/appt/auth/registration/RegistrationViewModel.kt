package nl.appt.auth.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.auth.ValidationManager
import nl.appt.helpers.Result
import nl.appt.model.User

class RegistrationViewModel() : ViewModel() {

    enum class FieldStates {
        PASSWORD_ERROR, EMAIL_ERROR, PASSWORD_VALID, EMAIL_VALID
    }

    var registrationResponse = MutableLiveData<Result<User>>()

    val errorState = MutableLiveData<FieldStates>()

    fun checkPasswordField(password: String): Boolean {
        return if (!ValidationManager.isValidPassword(password)) {
            errorState.value = FieldStates.PASSWORD_ERROR
            false
        } else {
            errorState.value = FieldStates.PASSWORD_VALID
            true
        }
    }

    fun checkEmailField(email: String): Boolean {
        return if (!ValidationManager.isValidEmail(email)) {
            errorState.value = FieldStates.EMAIL_ERROR
            false
        } else {
            errorState.value = FieldStates.EMAIL_VALID
            true
        }
    }

    fun userRegistration(email: String, password: String, userTypes: ArrayList<String>) {
        val username = createUsername(email)
        API.userRegistration(User(email, username, password, userTypes)) { response ->
            response.result?.let { result ->
                registrationResponse.value = Result.success(result)
            }

            response.error?.let { error ->
                registrationResponse.value = Result.error(error)
            }
        }
    }

    private fun createUsername(email: String): String {
        return email.split("@")[0]
    }
}
