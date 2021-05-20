package nl.appt.auth.registration

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import nl.appt.R
import nl.appt.databinding.ActivityRegistrationBinding
import nl.appt.widgets.ToolbarActivity

class RegistrationActivity : ToolbarActivity() {

    private lateinit var viewModel: RegistrationViewModel

    private lateinit var binding: ActivityRegistrationBinding

    override fun getToolbarTitle() = getString(R.string.registration_toolbar_title)

    override fun getLayoutId() = R.layout.activity_registration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        onViewCreated()
        initUi()
    }

    private fun initUi() {
        binding.registrationPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val password = binding.registrationPassword.text.toString()
                viewModel.checkPasswordField(password)
            } else {
                cleanError(RegistrationViewModel.PASSWORD_ERROR_CODE)
            }
        }

        binding.registrationEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val email = binding.registrationEmail.text.toString()
                viewModel.checkEmailField(email)
            } else {
                cleanError(RegistrationViewModel.EMAIL_ERROR_CODE)
            }
        }

        binding.createAccountBtn.setOnClickListener {
            if (isAllFieldsFiled(binding)) {
                //to next screen
            }
        }

        viewModel.errorCode.observe(this, { errorCode ->
            setError(errorCode)
        })

        viewModel.cleanErrorCode.observe(this, { cleanErrorCode ->
            cleanError(cleanErrorCode)
        })
    }

    private fun isAllFieldsFiled(binding: ActivityRegistrationBinding): Boolean {
        val password = binding.registrationPassword.text.toString()
        val email = binding.registrationEmail.text.toString()

        val isValidPassword = viewModel.checkPasswordField(password)
        val isValidEmail = viewModel.checkEmailField(email)
        return binding.termsAndConditionsCheckbox.isChecked && binding.privacyStatementCheckbox.isChecked && isValidEmail && isValidPassword
    }

    private fun setError(errorCode: Int) {
        when (errorCode) {
            RegistrationViewModel.PASSWORD_ERROR_CODE -> {
                binding.labelPassword.text = getString(R.string.registration_password_label_error)
                binding.labelPassword.setTextColor(getColor(R.color.red))
                binding.registrationPasswordLayout.error =
                    getString(R.string.registration_password_length_error)
            }
            RegistrationViewModel.EMAIL_ERROR_CODE -> {
                binding.labelEmail.text = getString(R.string.registration_email_label_error)
                binding.labelEmail.setTextColor(getColor(R.color.red))
                binding.registrationEmailLayout.error = getString(R.string.registration_email_error)
            }
        }
    }

    private fun cleanError(errorCode: Int) {
        when (errorCode) {
            RegistrationViewModel.PASSWORD_ERROR_CODE -> {
                binding.registrationPasswordLayout.isErrorEnabled = false
                binding.labelPassword.text = getString(R.string.registration_password)
                binding.labelPassword.setTextColor(getColor(R.color.black))
            }
            RegistrationViewModel.EMAIL_ERROR_CODE -> {
                binding.registrationEmailLayout.isErrorEnabled = false
                binding.labelEmail.text = getText(R.string.registration_field_email)
                binding.labelEmail.setTextColor(getColor(R.color.black))
            }
        }
    }
}