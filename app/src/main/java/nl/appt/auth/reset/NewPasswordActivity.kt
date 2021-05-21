package nl.appt.auth.reset

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import nl.appt.MainActivity
import nl.appt.R
import nl.appt.databinding.ActivityNewPasswordBinding
import nl.appt.widgets.ToolbarActivity

class NewPasswordActivity : ToolbarActivity() {

    private lateinit var viewModel: NewPasswordViewModel

    private lateinit var binding: ActivityNewPasswordBinding

    override fun getToolbarTitle() = getString(R.string.new_password_toolbar_title)

    override fun getLayoutId() = R.layout.activity_new_password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewPasswordViewModel::class.java)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        onViewCreated()
        initUi()
        setFieldStateObserver()
    }

    private fun setFieldStateObserver(){
        viewModel.errorState.observe(this, { errorState ->
            setFieldState(errorState)
        })
    }

    private fun initUi() {
        binding.newPasswordBtn.setOnClickListener {
            val password = binding.newPassword.text.toString()
            if (viewModel.checkPasswordField(password)) {
                startActivity<MainActivity>()
            }
        }
    }

    private fun setFieldState(state: NewPasswordViewModel.FieldStates) {
        when (state) {
            NewPasswordViewModel.FieldStates.PASSWORD_ERROR -> {
                binding.labelPassword.text = getString(R.string.registration_password_label_error)
                binding.labelPassword.setTextColor(getColor(R.color.red))
                binding.newPasswordLayout.error =
                    getString(R.string.registration_password_length_error)
            }
            NewPasswordViewModel.FieldStates.PASSWORD_VALID -> {
                binding.newPasswordLayout.isErrorEnabled = false
                binding.labelPassword.text = getString(R.string.registration_password)
                binding.labelPassword.setTextColor(getColor(R.color.black))
            }
        }
    }
}