package nl.appt.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nl.appt.R
import nl.appt.auth.login.LoginActivity
import nl.appt.auth.registration.ChooseTypeActivity
import nl.appt.auth.reset.ResetPasswordActivity
import nl.appt.databinding.FragmentAuthBinding
import nl.appt.widgets.ToolbarFragment

class AuthFragment : ToolbarFragment() {

    override fun getTitle() = getString(R.string.tab_training)

    override fun getLayoutId() = R.layout.fragment_auth

    private var _binding: FragmentAuthBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.createAccountBtn.setOnClickListener {
            startActivity<ChooseTypeActivity>()
        }
        binding.loginBtn.setOnClickListener {
            startActivity<LoginActivity>()
        }
        binding.forgotPasswordBtn.setOnClickListener {
            startActivity<ResetPasswordActivity>()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}