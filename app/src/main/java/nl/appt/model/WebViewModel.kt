package nl.appt.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.launch
import nl.appt.database.ApptDatabase
import nl.appt.database.Bookmark

class WebViewModel(application: Application) : AndroidViewModel(application) {

    private val database: ApptDatabase by lazy {
        Room.databaseBuilder(
            application,
            ApptDatabase::class.java, "appt_database"
        ).build()
    }

    fun get(url: String): LiveData<Bookmark?> {
        return database.bookmarks().get(url)
    }

    fun insert(bookmark: Bookmark) = viewModelScope.launch {
        database.bookmarks().insert(bookmark)
    }

    fun delete(bookmark: Bookmark) = viewModelScope.launch {
        database.bookmarks().delete(bookmark)
    }
}