package nl.appt.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.launch
import nl.appt.database.ApptDatabase
import nl.appt.database.Bookmark
import nl.appt.database.Page

class WebViewModel(application: Application) : AndroidViewModel(application) {

    private val database: ApptDatabase by lazy {
        Room.databaseBuilder(
            application,
            ApptDatabase::class.java, "appt_database"
        ).build()
    }

    // Bookmarks

    fun allBookmarks(): LiveData<List<Bookmark>> {
        return database.bookmarks().all()
    }

    fun getBookmark(url: String): LiveData<Bookmark?> {
        return database.bookmarks().get(url)
    }

    fun insertBookmark(bookmark: Bookmark) = viewModelScope.launch {
        database.bookmarks().insert(bookmark)
    }

    fun deleteBookmark(bookmark: Bookmark) = viewModelScope.launch {
        database.bookmarks().delete(bookmark)
    }

    // History

    fun allHistory(): LiveData<List<Page>> {
        return database.history().all()
    }

    fun getHistory(url: String): LiveData<Page?> {
        return database.history().get(url)
    }

    fun insertHistory(page: Page) = viewModelScope.launch {
        database.history().insert(page)
    }

    fun deleteHistory(page: Page) = viewModelScope.launch {
        database.history().delete(page)
    }
}