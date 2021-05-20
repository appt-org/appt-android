package nl.appt.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nl.appt.R
import nl.appt.auth.registration.RegistrationActivity
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
            startActivity<RegistrationActivity>()
        }
        binding.loginBtn.setOnClickListener {
            //Open Login Screen
        }
        binding.forgotPasswordBtn.setOnClickListener {
            //Open Reset Password Screen
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}