package nl.appt.helpers

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import nl.appt.R

object SnackbarCreator {

    fun createSnackbar(context: Context, contextView: ViewGroup): Snackbar {
        val snackbar = Snackbar.make(
            contextView,
            context.getString(R.string.user_not_verified_allert),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(context.getString(R.string.allert_action_title)) {}
            .setActionTextColor(context.getColor(R.color.row))
            .setTextColor(context.getColor(R.color.row))
            .setBackgroundTint(context.getColor(R.color.red))
            .setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
        val styledAttributes =
            context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val view = snackbar.view
        val params = view.layoutParams as CoordinatorLayout.LayoutParams
        params.gravity = Gravity.TOP
        params.topMargin = styledAttributes.getDimension(0, 0f).toInt()
        view.layoutParams = params
        return snackbar
    }
}