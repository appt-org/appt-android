package nl.appt.auth.reset

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import nl.appt.R
import nl.appt.databinding.ActivityResetPasswordBinding
import nl.appt.extensions.showDialog
import nl.appt.helpers.Status
import nl.appt.widgets.ToolbarActivity

class ResetPasswordActivity : ToolbarActivity() {

    private lateinit var viewModel: ResetPasswordViewModel

    private lateinit var binding: ActivityResetPasswordBinding

    override fun getToolbarTitle() = getString(R.string.reset_password_toolbar_title)

    override fun getLayoutId() = R.layout.activity_reset_password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ResetPasswordViewModel::class.java)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        onViewCreated()
        initUi()
        setObservers()
    }

    private fun setObservers() {
        viewModel.resetResponse.observe(this, { result ->
            showDialog(getString(R.string.reset_password_dialog_title), result.data) {
                if (result.status == Status.SUCCESS) {
                    finish()
                }
            }
        })

        viewModel.errorState.observe(this, { state ->
            setFieldState(state)
        })
    }

    private fun initUi() {
        binding.resetBtn.setOnClickListener {
            val email = binding.email.text.toString()
            if (viewModel.checkEmailField(email)) {
                viewModel.resetPassword(email)
            }
        }
    }

    private fun setFieldState(state: ResetPasswordViewModel.FieldStates) {
        when (state) {
            ResetPasswordViewModel.FieldStates.EMAIL_ERROR -> {
                binding.labelEmail.text = getString(R.string.registration_email_label_error)
                binding.labelEmail.setTextColor(getColor(R.color.red))
                binding.emailLayout.error = getString(R.string.registration_email_error)
            }
            ResetPasswordViewModel.FieldStates.EMAIL_VALID -> {
                binding.emailLayout.isErrorEnabled = false
                binding.labelEmail.text = getText(R.string.registration_field_email)
                binding.labelEmail.setTextColor(getColor(R.color.black))
            }
        }
    }
}