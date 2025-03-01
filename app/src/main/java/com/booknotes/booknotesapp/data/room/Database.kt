package com.booknotes.booknotesapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.booknotes.booknotesapp.data.room.BookEntity
import com.booknotes.booknotesapp.data.room.StringListConverter


@Database(entities = [BookEntity::class], version = 1)
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

@Dao
interface BookItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteBook(bookItem: BookEntity)

    @Delete
    suspend fun deleteFavoriteBook(bookItem: BookEntity)

    @Query("SELECT * FROM favourite_books WHERE userId = :userId ")
    fun getAllFavouriteBooks(userId: String): LiveData<List<BookEntity>>

    @Query("SELECT * FROM favourite_books WHERE userId = :userId ")
    fun getAllFavouriteBooksList(userId: String): List<BookEntity>

    @Query("SELECT COUNT(*) FROM favourite_books WHERE userId = :userId")
    fun getCount(userId: String): Int

//    @Insert
//    fun insertAll(vararg bookItems: Favorites)
//
//    @Query("SELECT (SELECT COUNT(*) FROM Favorites) == 0")
//    fun isEmpty(): Boolean
}