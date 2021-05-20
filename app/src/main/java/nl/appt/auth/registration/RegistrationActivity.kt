package nl.appt.auth.registration

import android.os.Bundle
import nl.appt.R
import nl.appt.databinding.ActivityRegistrationBinding
import nl.appt.widgets.ToolbarActivity

class RegistrationActivity : ToolbarActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun getToolbarTitle() = getString(R.string.registration_toolbar_title)

    override fun getLayoutId() = R.layout.activity_registration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                if (isValidPassword(password)) {
                    cleanError(PASSWORD_ERROR_CODE)
                } else {
                    setError(PASSWORD_ERROR_CODE)
                }
            }
        }

        binding.registrationEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val email = binding.registrationEmail.text.toString()
                if (isValidEmail(email)) {
                    cleanError(EMAIL_ERROR_CODE)
                } else {
                    setError(EMAIL_ERROR_CODE)
                }
            }
        }

        binding.createAccountBtn.setOnClickListener {
            if (isAllFieldsFiled()) {
                //to next screen
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= MIN_PASSWORD_LENGTH
    }

    private fun isAllFieldsFiled(): Boolean {
        val password = binding.registrationPassword.text.toString()
        val email = binding.registrationEmail.text.toString()
        var result = true
        if (isValidEmail(email)) {
            cleanError(EMAIL_ERROR_CODE)
        } else {
            setError(EMAIL_ERROR_CODE)
            result = false
        }

        if (isValidPassword(password)) {
            cleanError(PASSWORD_ERROR_CODE)
        } else {
            setError(PASSWORD_ERROR_CODE)
            result = false
        }

        if (!binding.termsAndConditionsCheckbox.isChecked || !binding.privacyStatementCheckbox.isChecked) {
            result = false
        }

        return result
    }

    private fun setError(errorCode: Int) {
        when (errorCode) {
            PASSWORD_ERROR_CODE -> {
                binding.labelPassword.text = getString(R.string.registration_password_label_error)
                binding.labelPassword.setTextColor(getColor(R.color.red))
                binding.registrationPasswordLayout.error =
                    getString(R.string.registration_password_length_error)
            }
            EMAIL_ERROR_CODE -> {
                binding.labelEmail.text = getString(R.string.registration_email_label_error)
                binding.labelEmail.setTextColor(getColor(R.color.red))
                binding.registrationEmailLayout.error = getString(R.string.registration_email_error)
            }
        }
    }

    private fun cleanError(errorCode: Int) {
        when (errorCode) {
            PASSWORD_ERROR_CODE -> {
                binding.registrationPasswordLayout.isErrorEnabled = false
                binding.labelPassword.text = getString(R.string.registration_password)
                binding.labelPassword.setTextColor(getColor(R.color.black))
            }
            EMAIL_ERROR_CODE -> {
                binding.registrationEmailLayout.isErrorEnabled = false
                binding.labelEmail.text = getText(R.string.registration_field_email)
                binding.labelEmail.setTextColor(getColor(R.color.black))
            }
        }
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 10
        private const val PASSWORD_ERROR_CODE = 1
        private const val EMAIL_ERROR_CODE = 2
    }
}