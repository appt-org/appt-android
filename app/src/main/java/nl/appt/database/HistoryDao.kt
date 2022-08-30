package nl.appt.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY updated_at DESC")
    fun all(): LiveData<List<History>>

    @Query("SELECT * FROM history WHERE url LIKE :url ORDER BY updated_at DESC")
    fun get(url: String): LiveData<History?>

    @Query("SELECT * FROM history WHERE url LIKE :query OR title LIKE :query ORDER BY updated_at DESC")
    fun search(query: String): LiveData<List<History>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg pages: History)

    @Update
    suspend fun update(bookmark: History)

    @Delete
    suspend fun delete(bookmark: History)
}
