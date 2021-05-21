package nl.appt.auth.reset

import android.os.Bundle
import nl.appt.R
import nl.appt.databinding.ActivityResetPasswordBinding
import nl.appt.widgets.ToolbarActivity

class ResetPasswordActivity : ToolbarActivity() {

    private lateinit var binding: ActivityResetPasswordBinding

    override fun getToolbarTitle() = getString(R.string.reset_password_toolbar_title)

    override fun getLayoutId() = R.layout.activity_reset_password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        onViewCreated()
        initUi()
    }

    private fun initUi() {
        binding.resetBtn.setOnClickListener {
            //Send confirmation email.
            startActivity<NewPasswordActivity>()
        }
    }
}