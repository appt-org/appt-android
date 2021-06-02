package nl.appt.model

data class User(
    val email: String,
    val username: String,
    val password: String,
    val roles: ArrayList<String>
)
