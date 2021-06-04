package nl.appt.model

data class UserResponse(
    val id: Int,
    val email: String,
    val roles: ArrayList<String>,
    val user_meta: UserMeta
)
