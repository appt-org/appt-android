package nl.appt.auth.registration

import android.widget.CheckBox
import androidx.lifecycle.ViewModel
import nl.appt.R

class ChooseTypeViewModel : ViewModel() {

    val checkBoxArrayList = arrayListOf<CheckBox>()

    fun getSelectedTypes(): ArrayList<String> {
        val selectedTypes = arrayListOf<String>()
        checkBoxArrayList.forEach { checkBox ->
            if (checkBox.isChecked) {
                when (checkBox.id) {
                    R.id.expert_checkBox -> selectedTypes.add(UserTypeConst.EXPERT)

                    R.id.interested_checkBox -> selectedTypes.add(UserTypeConst.INTERESTED)

                    R.id.ambassador_checkBox -> selectedTypes.add(UserTypeConst.AMBASSADOR)

                    R.id.designer_checkBox -> selectedTypes.add(UserTypeConst.DESIGNER)

                    R.id.tester_checkBox -> selectedTypes.add(UserTypeConst.TESTER)

                    R.id.developer_checkBox -> selectedTypes.add(UserTypeConst.DEVELOPER)

                    R.id.manager_checkBox -> selectedTypes.add(UserTypeConst.MANAGER)

                    R.id.accessibility_checkBox -> selectedTypes.add(UserTypeConst.ACCESSIBILITY)

                    R.id.auditor_checkBox -> selectedTypes.add(UserTypeConst.AUDITOR)
                }
            }
        }
        return selectedTypes
    }
}