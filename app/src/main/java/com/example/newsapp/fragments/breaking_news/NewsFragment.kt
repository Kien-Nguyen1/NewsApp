package com.example.newsapp.fragments.breaking_news

import androidx.fragment.app.Fragment
import com.example.newsapp.R

class NewsFragment: Fragment(R.layout.fragment_news) {
    companion object {
        fun newInstance(): NewsFragment = NewsFragment()
    }
}