package nl.appt.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PageDao {
    @Query("SELECT * FROM history ORDER BY updated_at DESC")
    fun all(): LiveData<List<Page>>

    @Query("SELECT * FROM history WHERE url LIKE :url")
    fun get(url: String): LiveData<Page?>

    @Query("SELECT * FROM history WHERE url LIKE :query OR title LIKE :query")
    fun search(query: String): LiveData<List<Page>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg pages: Page)

    @Update
    suspend fun update(bookmark: Page)

    @Delete
    suspend fun delete(bookmark: Page)
}
