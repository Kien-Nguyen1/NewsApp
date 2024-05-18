package com.example.newsapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("articles")
    @Expose
    val articles: MutableList<NewsResponseItem?>
)