package nl.appt.auth.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.auth.ValidationManager

class RegistrationViewModel() : ViewModel() {

    enum class FieldStates {
        PASSWORD_ERROR, EMAIL_ERROR, PASSWORD_VALID, EMAIL_VALID
    }

    val errorState = MutableLiveData<Enum<FieldStates>>()

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
}