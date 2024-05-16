package com.example.newsapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.models.NewsResponseItem

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(article : NewsResponseItem) : Long

    @Delete
    suspend fun deleteArticle(article: NewsResponseItem)

    @Query("SELECT * FROM articles")
    fun getSavedArticles() : LiveData<List<NewsResponseItem>>
}