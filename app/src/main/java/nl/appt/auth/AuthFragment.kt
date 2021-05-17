package nl.appt.auth

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_auth.*
import nl.appt.R
import nl.appt.widgets.ToolbarFragment

class AuthFragment : ToolbarFragment(), View.OnClickListener {

    override fun getTitle() = getString(R.string.tab_training)

    override fun getLayoutId() = R.layout.fragment_auth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        create_account_btn.setOnClickListener(this)
        login_btn.setOnClickListener(this)
        forgot_password_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            create_account_btn -> {
                //Open Create Account Screen
            }
            login_btn -> {
                //Open Login Screen
            }
            forgot_password_btn -> {
                //Open Reset Password Screen
            }
        }
    }
}