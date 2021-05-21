package nl.appt.auth.reset

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.auth.ValidationManager

class NewPasswordViewModel : ViewModel() {

    enum class FieldStates {
        PASSWORD_ERROR, PASSWORD_VALID
    }

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
}