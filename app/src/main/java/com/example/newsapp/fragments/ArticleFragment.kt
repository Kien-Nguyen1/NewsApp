package com.example.newsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.activities.MainActivity
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.viewmodels.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var newsViewModel: NewsViewModel

    private val args : ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as MainActivity).newsViewModel
        val article = args.article
        binding.webViewArticle.apply {
            webViewClient = WebViewClient()
            article?.let {
                it.url?.let { url ->
                    loadUrl(url)
                }
            }
        }

        binding.fabSave.setOnClickListener {
            article?.let {
                newsViewModel.saveArticle(it)
                Snackbar.make(view, "Article Saved Successfully!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}