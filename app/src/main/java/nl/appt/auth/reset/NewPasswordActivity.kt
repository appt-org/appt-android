package nl.appt.auth.reset

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import nl.appt.R
import nl.appt.auth.AuthActivity
import nl.appt.databinding.ActivityNewPasswordBinding
import nl.appt.extensions.showDialog
import nl.appt.extensions.showError
import nl.appt.helpers.Status
import nl.appt.widgets.ToolbarActivity

private const val SPLIT_DELIMITER = "&"
private const val SUBSTRING_DELIMITER = "="

class NewPasswordActivity : ToolbarActivity() {

    private lateinit var viewModel: NewPasswordViewModel

    private lateinit var binding: ActivityNewPasswordBinding

    private lateinit var url: String

    override fun getToolbarTitle() = getString(R.string.new_password_toolbar_title)

    override fun getLayoutId() = R.layout.activity_new_password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewPasswordViewModel::class.java)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        url = intent.data.toString()
        val view = binding.root
        setContentView(view)
        onViewCreated()
        initUi()
        setFieldStateObserver()
    }

    private fun setFieldStateObserver() {
        viewModel.errorState.observe(this, { errorState ->
            setFieldState(errorState)
        })

        viewModel.response.observe(this, { result ->
            if (result.status == Status.SUCCESS) {
                showDialog(getString(R.string.reset_password_dialog_title), result.data) {
                    val intent = Intent(this, AuthActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
            } else {
                showError(result.data) {
                    finish()
                }
            }
        })
    }

    private fun initUi() {
        binding.newPasswordBtn.setOnClickListener {
            val password = binding.newPassword.text.toString()
            if (viewModel.checkPasswordField(password)) {
                val values = url.split(SPLIT_DELIMITER)
                val key = values[1].substringAfter(SUBSTRING_DELIMITER)
                val login = values[2].substringAfter(SUBSTRING_DELIMITER)
                viewModel.setNewPassword(key, login, password)
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