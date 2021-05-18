package nl.appt.auth

import android.os.Bundle
import android.view.View
import nl.appt.R
import nl.appt.databinding.FragmentAuthBinding
import nl.appt.widgets.ToolbarFragment

class AuthFragment : ToolbarFragment() {

    private lateinit var binding: FragmentAuthBinding

    override fun getTitle() = getString(R.string.tab_training)

    override fun getLayoutId() = R.layout.fragment_auth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    fun initUi() {
        binding = FragmentAuthBinding.inflate(layoutInflater)
        binding.createAccountBtn.setOnClickListener {
            //Open Create Account Screen
        }
        binding.loginBtn.setOnClickListener {
            //Open Login Screen
        }
        binding.forgotPasswordBtn.setOnClickListener {
            //Open Reset Password Screen
        }
    }
}