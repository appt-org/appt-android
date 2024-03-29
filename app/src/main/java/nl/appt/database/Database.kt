package nl.appt.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Bookmark::class, History::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun bookmarks(): BookmarkDao
    abstract fun history(): HistoryDao
}
