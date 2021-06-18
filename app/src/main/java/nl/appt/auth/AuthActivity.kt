package nl.appt.auth

import android.content.Intent
import android.os.Bundle
import nl.appt.MainActivity
import nl.appt.R
import nl.appt.helpers.Preferences
import nl.appt.helpers.PrefsKeys
import nl.appt.widgets.BaseActivity

class AuthActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_auth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.title = ""
        if (Preferences.getString(PrefsKeys.USER_EMAIL_KEY).isEmpty()) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.auth_container, AuthFragment())
                .commit()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}   