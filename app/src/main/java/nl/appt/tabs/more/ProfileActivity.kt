package nl.appt.tabs.more

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import nl.appt.R
import nl.appt.auth.AuthActivity
import nl.appt.databinding.ActivityProfileBinding
import nl.appt.extensions.showDialog
import nl.appt.extensions.showError
import nl.appt.helpers.Preferences
import nl.appt.helpers.Result
import nl.appt.helpers.Status
import nl.appt.helpers.UserConst
import nl.appt.widgets.ToolbarActivity

class ProfileActivity : ToolbarActivity() {

    private lateinit var viewModel: ProfileViewModel

    private lateinit var binding: ActivityProfileBinding

    override fun getToolbarTitle() = getString(R.string.topic_profile_title)

    override fun getLayoutId() = R.layout.activity_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        onViewCreated()
        initUi()
        setObservers()
    }

    private fun setObservers() {
        viewModel.response.observe(this, { result ->
            onEvent(result)
        })
    }

    private fun initUi() {
        binding.userEmail.text = Preferences.getString(UserConst.USER_EMAIL_KEY)
        binding.logoutBtn.setOnClickListener {
            viewModel.logoutUser(Preferences.getInt(UserConst.USER_ID_KEY))
        }
        binding.forgotPasswordBtn.setOnClickListener {
            viewModel.changePassword(Preferences.getString(UserConst.USER_EMAIL_KEY))
        }
        binding.deleteAccount.setOnClickListener {
            viewModel.deleteUser(Preferences.getInt(UserConst.USER_ID_KEY))
        }
    }

    private fun onEvent(result: Result<Any>) {
        when (result.status) {
            Status.SUCCESS -> {
                when (result.data) {
                    ProfileViewModel.Event.DELETE -> {
                        toAuthActivity()
                    }
                    ProfileViewModel.Event.LOGOUT -> {
                        toAuthActivity()
                    }
                    else -> {
                        showDialog(
                            getString(R.string.reset_password_dialog_title),
                            result.data.toString()
                        )
                    }
                }
            }
            Status.ERROR -> {
                if (result.fuelError != null) {
                    showError(result.fuelError)
                } else {
                    showError(result.data.toString())
                }
            }
        }
    }

    private fun toAuthActivity() {
        Preferences.setString(UserConst.USER_EMAIL_KEY, "")
        val intent = Intent(this, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}