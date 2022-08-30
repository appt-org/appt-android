package nl.appt.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Bookmark::class, Page::class], version = 1)
abstract class ApptDatabase : RoomDatabase() {
    abstract fun bookmarks(): BookmarkDao
    abstract fun history(): PageDao
}
