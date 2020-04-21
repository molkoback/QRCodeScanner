package com.molko.qrcodescanner

import androidx.room.*

const val TABLE_NAME: String = "history"

@Entity(tableName = TABLE_NAME)
data class QREntry(
    @PrimaryKey(autoGenerate = true) var uid: Int?,
    @ColumnInfo(name = "time") var time: Long?,
    @ColumnInfo(name = "text") var text: String
)

@Dao
interface HistoryDao {
    @Insert
    fun insertEntry(entry: QREntry)
    
    fun insert(text: String) {
        insertEntry(QREntry(uid = null, text = text, time = System.currentTimeMillis()))
    }
    
    @Query("DELETE FROM $TABLE_NAME WHERE uid=:id")
    fun delete(id: Int)
    
    @Query("DELETE FROM $TABLE_NAME")
    fun clear()
    
    @Query("SELECT * FROM $TABLE_NAME")
    fun getEntries(): List<QREntry>
}

@Database(entities = [QREntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao() : HistoryDao
}
