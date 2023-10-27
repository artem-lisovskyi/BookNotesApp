package com.booknotes.booknotesapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.booknotes.booknotesapp.data.room.StringListConverter


@Database(entities = [Favorites::class], version = 1)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookItemDao(): BookItemDao

    companion object {
        private var database: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return if (database == null) {
                database = Room
                    .databaseBuilder(context, AppDatabase::class.java, "books_database")
                    .build()
                database as AppDatabase
            } else {
                database as AppDatabase
            }

        }
    }
}

@Entity(tableName = "favourite_books")
data class Favorites(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "authors") val authors: List<String>?,
    @ColumnInfo(name = "publishedDate") val publishedDate: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "pageCount") val pageCount: Int?,
    @ColumnInfo(name = "categories") val categories: List<String>?,
    @ColumnInfo(name = "imageLink") val imageLink: String?,
    @ColumnInfo(name = "previewLink") val previewLink: String?
)

@Dao
interface BookItemDao {
    @Insert
    suspend fun insertFavoriteBook(bookItem: Favorites)

    @Delete
    suspend fun deleteFavoriteBook(bookItem: Favorites)

    @Query("SELECT * FROM favourite_books")
    fun getAllFavouriteBooks(): LiveData<List<Favorites>>

//    @Insert
//    fun insertAll(vararg bookItems: Favorites)
//
//    @Query("SELECT (SELECT COUNT(*) FROM Favorites) == 0")
//    fun isEmpty(): Boolean
}