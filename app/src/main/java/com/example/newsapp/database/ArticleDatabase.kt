package com.example.newsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newsapp.database.dao.ArticleDao
import com.example.newsapp.models.NewsResponseItem

@Database(entities = [NewsResponseItem::class], version = 3, exportSchema = false)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {
        @Volatile
        private var instance: ArticleDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ArticleDatabase::class.java,
            "articles_db"
        )
            .fallbackToDestructiveMigration() // Tạm thời phá hủy và tái tạo database khi có thay đổi schema
            .build()
    }
}
