// BookDao.kt
package com.example.roomdblatest

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {
    @Insert
    suspend fun insert(book: Book)

    @Query("SELECT * FROM Book")
    suspend fun getAllBooks(): List<Book>
}
