package nl.appt.auth.registration

import android.app.Application
import android.widget.CheckBox
import androidx.lifecycle.AndroidViewModel
import nl.appt.R

class ChooseTypeViewModel(private val app: Application) : AndroidViewModel(app) {

    val checkBoxArrayList = arrayListOf<CheckBox>()

    fun getSelectedTypes(): ArrayList<String> {
        val selectedTypes = arrayListOf<String>()
        checkBoxArrayList.forEach { checkBox ->
            if (checkBox.isChecked) {
                when (checkBox.text) {
                    app.getString(R.string.user_type_expert) ->
                        selectedTypes.add(UserTypeConst.EXPERT)

                    app.getString(R.string.user_type_interested) ->
                        selectedTypes.add(UserTypeConst.INTERESTED)

                    app.getString(R.string.user_type_ambassador) ->
                        selectedTypes.add(UserTypeConst.AMBASSADOR)

                    app.getString(R.string.user_type_designer) ->
                        selectedTypes.add(UserTypeConst.DESIGNER)

                    app.getString(R.string.user_type_tester) ->
                        selectedTypes.add(UserTypeConst.TESTER)

                    app.getString(R.string.user_type_developer) ->
                        selectedTypes.add(UserTypeConst.DEVELOPER)

                    app.getString(R.string.user_type_manager) ->
                        selectedTypes.add(UserTypeConst.MANAGER)

                    app.getString(R.string.user_type_accessibility) ->
                        selectedTypes.add(UserTypeConst.ACCESSIBILITY)

                    app.getString(R.string.user_type_auditor) ->
                        selectedTypes.add(UserTypeConst.AUDITOR)
                }
            }
        }
        return selectedTypes
    }
}