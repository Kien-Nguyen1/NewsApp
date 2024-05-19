package com.example.newsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.activities.MainActivity
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.models.NewsResponseItem
import com.example.newsapp.util.Constants.Companion.KEY_ARTICLE
import com.example.newsapp.viewmodels.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var newsViewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as MainActivity).newsViewModel

        val article = arguments?.getSerializable(KEY_ARTICLE) as NewsResponseItem?

        if (article != null) {
            binding.webViewArticle.apply {
                webViewClient = WebViewClient()
                article.detailUrl?.let { loadUrl(it) }
            }

            binding.fabSave.setOnClickListener {
                newsViewModel.saveArticle(article)
                Snackbar.make(view, "Article Saved Successfully!", Snackbar.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "No Article Data Available", Toast.LENGTH_SHORT).show()
        }
    }
}