package com.example.newsapp.fragments.breaking_news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.adapter.BreakingNewsAdapter
import com.example.newsapp.databinding.BreakingNewsFragmentBinding

class BreakingNewsFragment: Fragment(R.layout.breaking_news_fragment) {
    companion object {
        const val INTERNATIONAL_NEWS = 0
        const val BUSINESS = 1
        const val CULTURE_AND_ART = 2
        const val EDUCATION = 3
        const val ENTERTAINMENT = 4
        const val NEWS = 5
        const val POLITICS = 6
        const val SPORTS = 7
        const val EMPTY = -1
    }

    private lateinit var binding: BreakingNewsFragmentBinding
    private lateinit var breakingNewsAdapter: BreakingNewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BreakingNewsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        breakingNewsAdapter = BreakingNewsAdapter(childFragmentManager)
        binding.viewPager.adapter = breakingNewsAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}