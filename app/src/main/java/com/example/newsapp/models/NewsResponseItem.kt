package com.example.newsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "articles")
data class NewsResponseItem(
    @PrimaryKey
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("description")
    @Expose
    val description: String? = null,
    @SerializedName("detailUrl")
    @Expose
    val detailUrl: String? = null,
    @SerializedName("fullAvatarUrl")
    @Expose
    val fullAvatarUrl: String? = null,
    @SerializedName("title")
    @Expose
    val title: String? = null
) : Serializable