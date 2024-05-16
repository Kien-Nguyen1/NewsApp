package com.example.newsapp.repository

import com.example.newsapp.api.NewsApi
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.models.NewsResponseItem

class NewsRepository(private val database: ArticleDatabase, private val api: NewsApi) {

    suspend fun getNews(category: String, page: Int, pageSize: Int) =
        api.getNews(category, page, pageSize)

    suspend fun searchNews(query: String) = api.searchNews(query)

    suspend fun saveArticle(article: NewsResponseItem) = database.articleDao().saveArticle(article)

    suspend fun deleteArticle(article: NewsResponseItem) = database.articleDao().deleteArticle(article)

    fun getSavedArticles() = database.articleDao().getSavedArticles()
}