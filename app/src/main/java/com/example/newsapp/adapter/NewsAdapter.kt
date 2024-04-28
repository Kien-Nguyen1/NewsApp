package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.ItemArticleBinding
import com.example.newsapp.models.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(var binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(ItemArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener : ((Article) -> Unit) ?= null

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentArticle = differ.currentList[position]

        holder.binding.apply {
            if(currentArticle.urlToImage.isNullOrEmpty()) {
                Glide.with(holder.itemView.context).load(R.drawable.bg_null).into(imgArticleItem)
            } else {
                Glide.with(holder.itemView.context).load(currentArticle.urlToImage).into(imgArticleItem)
            }
            tvTitleItem.text = currentArticle.title
            tvDesItem.text = currentArticle.description

            root.setOnClickListener {
                onItemClickListener?.let {
                    it(currentArticle)
                }
            }
        }
    }

    fun setOnClickListener(listener : (Article) -> Unit)
    {
        onItemClickListener = listener
    }
}