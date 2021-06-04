package nl.appt.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val id: Int,

    val email: String,

    val roles: ArrayList<String>,

    @SerializedName("user_meta")
    val userMeta: UserMeta
)
