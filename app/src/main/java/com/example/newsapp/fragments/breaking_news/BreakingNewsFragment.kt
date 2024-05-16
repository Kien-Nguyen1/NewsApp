package com.example.newsapp.fragments.breaking_news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.activities.MainActivity
import com.example.newsapp.adapter.ViewPagerAdapter
import com.example.newsapp.databinding.BreakingNewsFragmentBinding
import com.example.newsapp.viewmodels.NewsViewModel

class BreakingNewsFragment: Fragment(R.layout.breaking_news_fragment) {

    private lateinit var binding: BreakingNewsFragmentBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var newsViewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BreakingNewsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as MainActivity).newsViewModel
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        binding.viewPager.adapter = viewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}