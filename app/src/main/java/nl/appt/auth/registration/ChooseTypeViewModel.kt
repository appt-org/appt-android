package nl.appt.auth.registration

import android.content.Context
import android.widget.CheckBox
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.R

class ChooseTypeViewModel : ViewModel() {

    var checkBoxArrayList = arrayListOf<CheckBox>()

    var isButtonClickable = MutableLiveData(false)

    fun setListenerOnCheckBox() {
        checkBoxArrayList.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                isButtonEnable()
            }
        }
    }

    private fun isButtonEnable() {
        checkBoxArrayList.forEach { checkBox ->
            if (checkBox.isChecked) {
                isButtonClickable.value = true
                return
            }
        }
        isButtonClickable.value = false
    }

    fun getSelectedTypes(context: Context): ArrayList<String> {
        val selectedTypes = arrayListOf<String>()
        checkBoxArrayList.forEach { checkBox ->
            if (checkBox.isChecked) {
                when (checkBox.text) {
                    context.getString(R.string.user_type_expert) -> selectedTypes.add(UserTypeConst.EXPERT)
                    context.getString(R.string.user_type_interested) -> selectedTypes.add(
                        UserTypeConst.INTERESTED
                    )
                    context.getString(R.string.user_type_ambassador) -> selectedTypes.add(
                        UserTypeConst.AMBASSADOR
                    )
                    context.getString(R.string.user_type_designer) -> selectedTypes.add(
                        UserTypeConst.DESIGNER
                    )
                    context.getString(R.string.user_type_tester) -> selectedTypes.add(UserTypeConst.TESTER)
                    context.getString(R.string.user_type_developer) -> selectedTypes.add(
                        UserTypeConst.DEVELOPER
                    )
                    context.getString(R.string.user_type_manager) -> selectedTypes.add(UserTypeConst.MANAGER)
                    context.getString(R.string.user_type_accessibility) -> selectedTypes.add(
                        UserTypeConst.ACCESSIBILITY
                    )
                    context.getString(R.string.user_type_auditor) -> selectedTypes.add(UserTypeConst.AUDITOR)
                }
            }
        }
        return selectedTypes
    }
}