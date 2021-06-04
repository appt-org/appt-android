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
            if (response.result != null){
                _response.value = Result.success(response.result)
            } else {
                _response.value = Result.error(response.error)
            }
        }
    }

    fun deleteUser(id: Int) {
        API.userDelete(id) { response ->
            if (response.result != null){
                _response.value = Result.success(response.result)
            } else {
                _response.value = Result.error(response.error)
            }
        }
    }

}