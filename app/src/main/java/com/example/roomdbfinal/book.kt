// BookActivity.kt
package com.example.roomdbfinal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.widget.*
import com.example.roomdblatest.AppDatabase
import com.example.roomdblatest.Book
import com.example.roomdblatest.BookDao

class BookActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var bookDao: BookDao
    private lateinit var titleInput: EditText
    private lateinit var authorInput: EditText
    private lateinit var saveButton: Button
    private lateinit var bookList: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        db = AppDatabase.getDatabase(this)
        bookDao = db.bookDao()

        titleInput = findViewById(R.id.editTextTitle)
        authorInput = findViewById(R.id.editTextAuthor)
        saveButton = findViewById(R.id.buttonSaveBook)
         bookList = findViewById(R.id.textViewBooks)

        val goToBooksBtn = findViewById<Button>(R.id.buttonUser)
        goToBooksBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        saveButton.setOnClickListener {
            val title = titleInput.text.toString()
            val author = authorInput.text.toString()

            if (title.isNotBlank() && author.isNotBlank()) {
                lifecycleScope.launch {
                    bookDao.insert(Book(title = title, author = author))
                    updateBookList()
                }
                titleInput.text.clear()
                authorInput.text.clear()
            }
        }

        lifecycleScope.launch {
            updateBookList()
        }
    }

    private suspend fun updateBookList() {
        val books = bookDao.getAllBooks()
        val list = books.joinToString("\n") { "${it.id}: ${it.title} by ${it.author}" }
        runOnUiThread {
            bookList.text = list
        }
    }
}
