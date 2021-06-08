package nl.appt.auth.registration

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import nl.appt.MainActivity
import nl.appt.R
import nl.appt.databinding.ActivityRegistrationBinding
import nl.appt.extensions.setArticleType
import nl.appt.extensions.setSlug
import nl.appt.extensions.showDialog
import nl.appt.extensions.showError
import nl.appt.helpers.Preferences
import nl.appt.helpers.Result
import nl.appt.helpers.Status
import nl.appt.helpers.UserConst
import nl.appt.model.Article
import nl.appt.model.UserResponse
import nl.appt.tabs.news.ArticleActivity
import nl.appt.widgets.ToolbarActivity

private const val TERMS_SLUG = "algemene-voorwaarden"
private const val PRIVACY_SLUG = "privacybeleid"

class RegistrationActivity : ToolbarActivity() {

    companion object {
        const val USER_TYPES_KEY = "key_user_types"
    }

    private lateinit var viewModel: RegistrationViewModel

    private lateinit var binding: ActivityRegistrationBinding

    override fun getToolbarTitle() = getString(R.string.registration_toolbar_title)

    override fun getLayoutId() = R.layout.activity_registration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onViewCreated()
        initUi()
    }

    private fun initUi() {
        binding.registrationPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val password = binding.registrationPassword.text.toString()
                viewModel.checkPasswordField(password)
            } else {
                setFieldState(RegistrationViewModel.FieldStates.PASSWORD_VALID)
            }
        }

        binding.registrationEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val email = binding.registrationEmail.text.toString()
                viewModel.checkEmailField(email)
            } else {
                setFieldState(RegistrationViewModel.FieldStates.EMAIL_VALID)
            }
        }

        binding.createAccountBtn.setOnClickListener {
            if (isAllFieldsFiled()) {
                val email = binding.registrationEmail.text.toString()
                val password = binding.registrationPassword.text.toString()
                val userTypes = intent.getStringArrayListExtra(USER_TYPES_KEY) as ArrayList<String>
                viewModel.registrationResponse.observe(this, { result ->
                    onEvent(result)
                })
                viewModel.userRegistration(email, password, userTypes)
            }
        }

        binding.privacyStatementLink.setOnClickListener {
            startActivity<ArticleActivity> {
                setArticleType(Article.Type.PAGE)
                setSlug(PRIVACY_SLUG)
            }
        }

        binding.termsAndConditionsLink.setOnClickListener {
            startActivity<ArticleActivity> {
                setArticleType(Article.Type.PAGE)
                setSlug(TERMS_SLUG)
            }
        }

        viewModel.errorState.observe(this, { errorState ->
            setFieldState(errorState)
        })
    }

    private fun onEvent(result: Result<UserResponse>) {
        when (result.status) {
            Status.SUCCESS -> {
                showDialog(
                    getString(R.string.registration_succeed_dialog_title),
                    getString(R.string.registration_succeed_dialog_message)
                ) {
                    result.data?.let {
                        Preferences.run {
                            setString(UserConst.USER_EMAIL_KEY, result.data.email)
                            setInt(UserConst.USER_ID_KEY, result.data.id)
                            setString(
                                UserConst.USER_VERIFIED_KEY,
                                result.data.userMeta.userActivationStatus[0]
                            )
                        }
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
                }
            }
            Status.ERROR -> {
                showError(result.fuelError)
            }
        }
    }

    private fun isAllFieldsFiled(): Boolean {
        val password = binding.registrationPassword.text.toString()
        val email = binding.registrationEmail.text.toString()

        val isValidPassword = viewModel.checkPasswordField(password)
        val isValidEmail = viewModel.checkEmailField(email)
        return binding.termsAndConditionsCheckbox.isChecked && binding.privacyStatementCheckbox.isChecked && isValidEmail && isValidPassword
    }

    private fun setFieldState(state: Enum<RegistrationViewModel.FieldStates>) {
        when (state) {
            RegistrationViewModel.FieldStates.PASSWORD_ERROR -> {
                binding.labelPassword.text = getString(R.string.registration_password_label_error)
                binding.labelPassword.setTextColor(getColor(R.color.red))
                binding.registrationPasswordLayout.error =
                    getString(R.string.registration_password_length_error)
            }
            RegistrationViewModel.FieldStates.EMAIL_ERROR -> {
                binding.labelEmail.text = getString(R.string.registration_email_label_error)
                binding.labelEmail.setTextColor(getColor(R.color.red))
                binding.registrationEmailLayout.error = getString(R.string.registration_email_error)
            }
            RegistrationViewModel.FieldStates.PASSWORD_VALID -> {
                binding.registrationPasswordLayout.isErrorEnabled = false
                binding.labelPassword.text = getString(R.string.registration_password)
                binding.labelPassword.setTextColor(getColor(R.color.black))
            }
            RegistrationViewModel.FieldStates.EMAIL_VALID -> {
                binding.registrationEmailLayout.isErrorEnabled = false
                binding.labelEmail.text = getText(R.string.registration_field_email)
                binding.labelEmail.setTextColor(getColor(R.color.black))
            }
        }
    }
}