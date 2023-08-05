package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.models.Article

class NewsRepository(private val database: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode : String, pageNumber : Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery : String, pageNumber: Int) =
        RetrofitInstance.api.searchNews(searchQuery, pageNumber)

    suspend fun saveArticle(article: Article) = database.articleDao().saveArticle(article)

    suspend fun deleteArticle(article: Article) = database.articleDao().deleteArticle(article)

    fun getSavedArticles() = database.articleDao().getSavedArticles()
}