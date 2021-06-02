package nl.appt.auth.registration

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import nl.appt.R
import nl.appt.databinding.ActivityChooseTypeBinding
import nl.appt.widgets.ToolbarActivity

class ChooseTypeActivity : ToolbarActivity() {

    private lateinit var viewModel: ChooseTypeViewModel

    private lateinit var binding: ActivityChooseTypeBinding

    override fun getToolbarTitle() = getString(R.string.registration_toolbar_title)

    override fun getLayoutId() = R.layout.activity_choose_type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChooseTypeViewModel::class.java)
        binding = ActivityChooseTypeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        onViewCreated()
        initUi()

        createCheckBoxList()
        viewModel.setListenerOnCheckBox()
    }

    private fun initUi() {

        viewModel.isButtonClickable.observe(this@ChooseTypeActivity, {
            binding.createAccountBtn.isEnabled = it
        })

        binding.createAccountBtn.setOnClickListener {
            startActivity<RegistrationActivity> {
                val userTypes = viewModel.getSelectedTypes(this@ChooseTypeActivity)
                putStringArrayListExtra(RegistrationActivity.USER_TYPES_KEY, userTypes)
            }
        }
    }

    private fun createCheckBoxList() {
        viewModel.checkBoxArrayList.run {
            add(binding.expertCheckBox)
            add(binding.interestedCheckBox)
            add(binding.ambassadorCheckBox)
            add(binding.designerCheckBox)
            add(binding.testerCheckBox)
            add(binding.developerCheckBox)
            add(binding.managerCheckBox)
            add(binding.accessibilityCheckBox)
            add(binding.auditorCheckBox)
        }
    }
}