package nl.appt.auth

import android.os.Bundle
import nl.appt.R
import nl.appt.widgets.BaseActivity

class AuthActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_auth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.title = ""
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.auth_container, AuthFragment())
            .commit()
    }
}