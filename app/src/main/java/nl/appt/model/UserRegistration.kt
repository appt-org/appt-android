package nl.appt.model

data class UserRegistration(
    val email: String,
    val username: String,
    val password: String,
    val roles: ArrayList<String>
)
