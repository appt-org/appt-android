package nl.appt.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import nl.appt.api.API
import nl.appt.auth.ValidationManager
import nl.appt.helpers.Preferences
import nl.appt.helpers.PrefsKeys
import nl.appt.helpers.Result
import nl.appt.model.UserLogin
import nl.appt.model.UserResponse

private const val JSON_CODE = "code"
private const val JSON_MESSAGE = "message"
private const val SPLIT_DELIMITER = "@"

class LoginViewModel : ViewModel() {

    enum class FieldStates {
        PASSWORD_ERROR, EMAIL_ERROR, PASSWORD_VALID, EMAIL_VALID
    }

    private val _loginResponse = MutableLiveData<Result<String>>()

    val loginResponse: LiveData<Result<String>> = _loginResponse

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
            response.result?.let { jsonObject ->
                if (jsonObject[JSON_CODE] != null) {
                    _loginResponse.value = Result.error(null, jsonObject[JSON_MESSAGE].asString)
                } else {
                    val userResponse = Gson().fromJson(jsonObject, UserResponse::class.java)
                    saveUserData(userResponse)
                    _loginResponse.value = Result.success()
                }
            }
        }
    }

    private fun saveUserData(user: UserResponse) {
        Preferences.run {
            setString(PrefsKeys.USER_EMAIL_KEY, user.email)
            setInt(PrefsKeys.USER_ID_KEY, user.id)
            setString(
                PrefsKeys.USER_VERIFIED_KEY,
                user.userMeta.userActivationStatus[0]
            )
        }
    }

    private fun createUsername(email: String): String {
        return email.split(SPLIT_DELIMITER)[0]
    }
}