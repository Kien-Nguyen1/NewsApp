package com.example.newsapp.api

import com.example.newsapp.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApi {

    @GET("{category}.php")
    suspend fun getNews(
        @Path("category") category: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): Response<NewsResponse>

    @GET("search.php")
    suspend fun searchNews(
        @Query("query") query: String
    ): Response<NewsResponse>
}
