package nl.appt.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.launch
import nl.appt.database.Database
import nl.appt.database.Bookmark
import nl.appt.database.History

class WebViewModel(application: Application) : AndroidViewModel(application) {

    private val database: Database by lazy {
        Room.databaseBuilder(
            application,
            Database::class.java, "appt_database"
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

    fun allHistory(): LiveData<List<History>> {
        return database.history().all()
    }

    fun getHistory(url: String): LiveData<History?> {
        return database.history().get(url)
    }

    fun insertHistory(history: History) = viewModelScope.launch {
        database.history().insert(history)
    }

    fun updateHistory(history: History) = viewModelScope.launch {
        database.history().update(history)
    }

    fun deleteHistory(history: History) = viewModelScope.launch {
        database.history().delete(history)
    }
}