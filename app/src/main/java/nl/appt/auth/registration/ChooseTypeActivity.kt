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
        setListenerOnCheckBox()
    }

    private fun initUi() {
        binding.createAccountBtn.setOnClickListener {
            startActivity<RegistrationActivity> {
                val userTypes = viewModel.getSelectedTypes()
                putStringArrayListExtra(RegistrationActivity.USER_TYPES_KEY, userTypes)
            }
        }
    }

    private fun setListenerOnCheckBox() {
        viewModel.checkBoxArrayList.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                isButtonEnable()
            }
        }
    }

    private fun isButtonEnable() {
        viewModel.checkBoxArrayList.forEach { checkBox ->
            if (checkBox.isChecked) {
                binding.createAccountBtn.isEnabled = true
                return
            }
        }
        binding.createAccountBtn.isEnabled = false
    }

    private fun createCheckBoxList() {
        with(binding) {
            viewModel.checkBoxArrayList.addAll(
                listOf(
                    expertCheckBox,
                    interestedCheckBox,
                    ambassadorCheckBox,
                    designerCheckBox,
                    testerCheckBox,
                    developerCheckBox,
                    managerCheckBox,
                    accessibilityCheckBox,
                    auditorCheckBox,
                    )
            )
        }
    }
}