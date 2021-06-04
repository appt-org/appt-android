package nl.appt.model

import com.google.gson.annotations.SerializedName

data class UserMeta(
    @SerializedName("user_activation_status")
    val userActivationStatus: ArrayList<String>
)
