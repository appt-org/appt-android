package nl.appt.dialog

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import nl.appt.database.Bookmark
import nl.appt.model.Item
import nl.appt.model.WebViewModel

class BookmarksDialog : PageDialog<Bookmark>(Item.BOOKMARKS.title) {

    private val model: WebViewModel by activityViewModels()

    override fun getData(): LiveData<List<Bookmark>> {
        return model.allBookmarks()
    }
}