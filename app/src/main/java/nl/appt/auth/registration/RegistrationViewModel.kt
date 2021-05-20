package nl.appt.auth.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.auth.ValidationManager

class RegistrationViewModel() : ViewModel() {

    companion object {
        const val PASSWORD_ERROR_CODE = 1
        const val EMAIL_ERROR_CODE = 2
    }

    val errorCode = MutableLiveData<Int>()

    val cleanErrorCode = MutableLiveData<Int>()

    fun checkPasswordField(password: String): Boolean {
        return if (!ValidationManager.isValidPassword(password)) {
            errorCode.value = PASSWORD_ERROR_CODE
            false
        } else {
            cleanErrorCode.value = PASSWORD_ERROR_CODE
            true
        }
    }

    fun checkEmailField(email: String): Boolean {
        return if (!ValidationManager.isValidEmail(email)) {
            errorCode.value = EMAIL_ERROR_CODE
            false
        } else {
            cleanErrorCode.value = EMAIL_ERROR_CODE
            true
        }
    }
}