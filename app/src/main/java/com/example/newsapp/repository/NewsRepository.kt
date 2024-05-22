package com.example.newsapp.repository

import com.example.newsapp.api.NewsApi
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.models.NewsResponseItem
import retrofit2.Response

class NewsRepository(private val database: ArticleDatabase, private val api: NewsApi) {

    suspend fun getNews(category: String, page: Int = 1, pageSize: Int = 100) =
        api.getNews(category, page, pageSize)

    suspend fun searchNews(keyword: String): Response<NewsResponse> =
        api.searchNews(keyword)

    suspend fun saveArticle(article: NewsResponseItem) = database.articleDao().saveArticle(article)

    suspend fun deleteArticle(article: NewsResponseItem) = database.articleDao().deleteArticle(article)

    fun getSavedArticles() = database.articleDao().getSavedArticles()
}