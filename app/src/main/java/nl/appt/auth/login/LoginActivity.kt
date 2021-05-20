package nl.appt.auth.login

import android.os.Bundle
import nl.appt.MainActivity
import nl.appt.R
import nl.appt.databinding.ActivityLoginBinding
import nl.appt.widgets.ToolbarActivity

class LoginActivity : ToolbarActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun getToolbarTitle() = getString(R.string.login_title)

    override fun getLayoutId() = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        onViewCreated()
        initUi()
    }

    private fun initUi() {
        binding.loginBtn.setOnClickListener {
            startActivity<MainActivity>()
        }

        binding.forgotPasswordBtn.setOnClickListener {
            //to forgot password screen
        }
    }
}
