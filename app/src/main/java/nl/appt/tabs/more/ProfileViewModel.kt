package nl.appt.tabs.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.helpers.Result

class ProfileViewModel : ViewModel() {

    private val _response = MutableLiveData<Result<Any>>()

    val response: LiveData<Result<Any>> = _response

    fun logoutUser(id: Int) {
        API.userLogout(id) { response ->
            response.result?.let { result ->
                _response.value = Result.success(result)
            }

            response.error?.let { error ->
                _response.value = Result.error(error)
            }
        }
    }

    fun deleteUser(id: Int) {
        API.userDelete(id) { response ->
            response.result?.let { result ->
                _response.value = Result.success(result)
            }

            response.error?.let { error ->
                _response.value = Result.error(error)
            }
        }
    }

}