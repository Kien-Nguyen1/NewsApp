package com.example.newsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "articles")
data class NewsResponseItem(
    @PrimaryKey
    val id: String,
    val description: String? = null,
    val detailUrl: String? = null,
    val fullAvatarUrl: String? = null,
    val title: String? = null
): Serializable