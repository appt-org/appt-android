package nl.appt.auth.login

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import nl.appt.MainActivity
import nl.appt.R
import nl.appt.auth.reset.ResetPasswordActivity
import nl.appt.databinding.ActivityLoginBinding
import nl.appt.extensions.showError
import nl.appt.helpers.Result
import nl.appt.helpers.Status
import nl.appt.model.User
import nl.appt.widgets.ToolbarActivity

class LoginActivity : ToolbarActivity() {

    private lateinit var viewModel: LoginViewModel

    private lateinit var binding: ActivityLoginBinding

    override fun getToolbarTitle() = getString(R.string.login_title)

    override fun getLayoutId() = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onViewCreated()
        initUi()
    }

    private fun initUi() {
        binding.loginPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val password = binding.loginPassword.text.toString()
                viewModel.checkPasswordField(password)
            } else {
                setFieldState(LoginViewModel.FieldStates.PASSWORD_VALID)
            }
        }

        binding.loginEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val email = binding.loginEmail.text.toString()
                viewModel.checkEmailField(email)
            } else {
                setFieldState(LoginViewModel.FieldStates.EMAIL_VALID)
            }
        }
        binding.loginBtn.setOnClickListener {
            if (isAllFieldsFiled()) {
                val email = binding.loginEmail.text.toString()
                val password = binding.loginPassword.text.toString()

                viewModel.loginResponse.observe(this, { result ->
                    onEvent(result)
                })
                viewModel.userLogin(email, password)
            }
        }

        binding.forgotPasswordBtn.setOnClickListener {
            startActivity<ResetPasswordActivity>()
        }

        viewModel.errorState.observe(this, { errorState ->
            setFieldState(errorState)
        })
    }

    private fun onEvent(result: Result<User>) {
        when (result.status) {
            Status.SUCCESS -> {
                startActivity<MainActivity>()
            }
            Status.ERROR -> {
                showError(getString(R.string.login_error_message))
                setFieldState(LoginViewModel.FieldStates.LOGIN_ERROR)
            }
        }
    }

    private fun isAllFieldsFiled(): Boolean {
        val isValidPassword = viewModel.checkPasswordField(binding.loginPassword.text.toString())
        val isValidEmail = viewModel.checkEmailField(binding.loginEmail.text.toString())
        return isValidEmail && isValidPassword
    }

    private fun setFieldState(state: Enum<LoginViewModel.FieldStates>) {
        when (state) {
            LoginViewModel.FieldStates.PASSWORD_ERROR -> {
                binding.labelPassword.run {
                    text = getString(R.string.registration_password_label_error)
                    setTextColor(getColor(R.color.red))
                }
                binding.loginPasswordLayout.error =
                    getString(R.string.registration_password_length_error)
            }
            LoginViewModel.FieldStates.EMAIL_ERROR -> {
                binding.labelEmail.run {
                    text = getString(R.string.registration_email_label_error)
                    setTextColor(getColor(R.color.red))
                }
                binding.loginEmailLayout.error = getString(R.string.registration_email_error)
            }
            LoginViewModel.FieldStates.PASSWORD_VALID -> {
                binding.loginPasswordLayout.isErrorEnabled = false
                binding.labelPassword.run {
                    text = getString(R.string.registration_password)
                    setTextColor(getColor(R.color.black))
                }
            }
            LoginViewModel.FieldStates.EMAIL_VALID -> {
                binding.loginEmailLayout.isErrorEnabled = false
                binding.labelEmail.run {
                    text = getText(R.string.registration_field_email)
                    setTextColor(getColor(R.color.black))
                }
            }
            LoginViewModel.FieldStates.LOGIN_ERROR -> {
                binding.labelPassword.run {
                    text = getString(R.string.registration_password_label_error)
                    setTextColor(getColor(R.color.red))
                }
                binding.labelEmail.run {
                    text = getString(R.string.registration_email_label_error)
                    setTextColor(getColor(R.color.red))
                }
            }
        }
    }
}
