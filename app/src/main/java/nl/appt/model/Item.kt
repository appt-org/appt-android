package nl.appt.model

import nl.appt.R

enum class Item {

    BOOKMARKS,
    CLOSE,
    HISTORY,
    HOME,
    JUMP_BACK,
    JUMP_FORWARD,
    RELOAD,
    SETTINGS;

    val title: Int
        get() {
            return when (this) {
                BOOKMARKS -> R.string.bookmarks
                CLOSE -> R.string.close
                HISTORY -> R.string.history
                JUMP_BACK -> R.string.jump_back
                JUMP_FORWARD -> R.string.jump_forward
                HOME -> R.string.home
                RELOAD -> R.string.reload
                SETTINGS -> R.string.settings
            }
        }

    val icon: Int
        get() {
            return when (this) {
                BOOKMARKS -> R.drawable.icon_bookmarks
                CLOSE -> R.drawable.icon_close
                HISTORY -> R.drawable.icon_history
                HOME -> R.drawable.icon_home
                JUMP_BACK -> R.drawable.icon_jump_back
                JUMP_FORWARD -> R.drawable.icon_jump_forward
                RELOAD -> R.drawable.icon_reload
                SETTINGS -> R.drawable.icon_settings
            }
        }
}