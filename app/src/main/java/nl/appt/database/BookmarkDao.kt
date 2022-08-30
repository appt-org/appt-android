package nl.appt.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks")
    fun all(): LiveData<List<Bookmark>>

    @Query("SELECT * FROM bookmarks WHERE url LIKE :url")
    fun get(url: String): LiveData<Bookmark?>

    @Query("SELECT * FROM bookmarks WHERE url LIKE :query OR title LIKE :query")
    fun search(query: String): LiveData<List<Bookmark>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg bookmarks: Bookmark)

    @Delete
    suspend fun delete(bookmark: Bookmark)
}
