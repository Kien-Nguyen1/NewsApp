package com.example.newsapp.fragments.breaking_news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.activities.MainActivity
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.util.Constants.Companion.ARG_CATEGORY
import com.example.newsapp.util.Constants.Companion.KEY_ARTICLE
import com.example.newsapp.util.Resource
import com.example.newsapp.viewmodels.NewsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsFragment : Fragment(R.layout.fragment_news) {
    private lateinit var binding: FragmentNewsBinding
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    companion object {

        fun newInstance(category: String): NewsFragment {
            val fragment = NewsFragment()
            val bundle = Bundle().apply {
                putString(ARG_CATEGORY, category)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as MainActivity).newsViewModel

        setUpRecyclerView()

        val category = arguments?.getString(ARG_CATEGORY) ?: "Universal"
        getData(category)

        newsViewModel.newsResponses[category]?.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    binding.tvEmpty.visibility = View.GONE
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG).show()
                        binding.tvEmpty.visibility = View.VISIBLE
                    }
                }
                is Resource.Loading -> {
                    binding.tvEmpty.visibility = View.GONE
                    showProgressBar()
                }
            }
        })

        setUpSwipeRefreshLayout(category)

        newsAdapter.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable(KEY_ARTICLE, it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }
    }

    private fun setUpSwipeRefreshLayout(category: String) {
        binding.swipeLayout.setOnRefreshListener {
            coroutineScope.launch {
                showProgressBar()
                delay(500L)
                withContext(Dispatchers.Main) {
                    hideProgressBar()
                    binding.swipeLayout.isRefreshing = false
                    getData(category)
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            setPadding(0, 0, 0, 0)
        }
    }

    private fun showProgressBar() {
        binding.recyclerView.visibility = View.GONE
        binding.shimmerLoading.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.shimmerLoading.visibility = View.GONE
    }

    private fun getData(category: String) {
        coroutineScope.launch {
            delay(500L)
            withContext(Dispatchers.IO) {
                newsViewModel.getNews(category)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}