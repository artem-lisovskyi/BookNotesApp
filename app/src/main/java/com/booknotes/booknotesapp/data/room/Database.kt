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
import com.booknotes.booknotesapp.data.retrofit.Book
import com.booknotes.booknotesapp.data.room.StringListConverter


@Database(entities = [Book::class], version = 1)
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
    suspend fun insertFavoriteBook(bookItem: Book)

    @Delete
    suspend fun deleteFavoriteBook(bookItem: Book)

    @Query("SELECT * FROM favourite_books")
    fun getAllFavouriteBooks(): LiveData<List<Book>>

//    @Insert
//    fun insertAll(vararg bookItems: Favorites)
//
//    @Query("SELECT (SELECT COUNT(*) FROM Favorites) == 0")
//    fun isEmpty(): Boolean
}