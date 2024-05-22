package com.example.newsapp.api

import com.example.newsapp.models.NewsResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApi {

    @GET("{category}.php")
    suspend fun getNews(
        @Path("category") category: String,
        @Query("page") page: Int = Int.MAX_VALUE,
        @Query("pageSize") pageSize: Int = Int.MAX_VALUE
    ): Response<NewsResponse>

    @POST("search_news.php")
    @FormUrlEncoded
    suspend fun searchNews(
        @Field("keyword") keyword: String
    ): Response<NewsResponse>
}
