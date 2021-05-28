package nl.appt.tabs.more

import android.content.Intent
import android.os.Bundle
import nl.appt.R
import nl.appt.auth.AuthActivity
import nl.appt.databinding.ActivityProfileBinding
import nl.appt.widgets.ToolbarActivity

class ProfileActivity : ToolbarActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun getToolbarTitle() = getString(R.string.topic_profile_title)

    override fun getLayoutId() = R.layout.activity_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        onViewCreated()
        initUi()
    }

    private fun initUi() {
        binding.logoutBtn.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}