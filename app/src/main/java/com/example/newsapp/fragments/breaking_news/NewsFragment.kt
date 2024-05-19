package com.example.newsapp.fragments.breaking_news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.activities.MainActivity
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.util.Constants.Companion.KEY_ARTICLE
import com.example.newsapp.util.Resource
import com.example.newsapp.viewmodels.NewsViewModel

class NewsFragment : Fragment(R.layout.fragment_news) {

    private lateinit var binding: FragmentNewsBinding
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    companion object {
        const val ARG_CATEGORY = "category"

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
        newsViewModel.getNews(category, 1, 20)

        newsViewModel.newsResponses[category]?.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsAdapter.differ.currentList + newsResponse.articles)
                        isLastPage = newsResponse.articles.size < 20
                    }
                    isLoading = false
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG).show()
                    }
                    isLoading = false
                }
                is Resource.Loading -> {
                    showProgressBar()
                    isLoading = true
                }
            }
        })

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

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@NewsFragment.scrollListener)
            setPadding(0, 0, 0, 0)
            clipToPadding = false
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 20
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                val category = arguments?.getString(ARG_CATEGORY) ?: "Universal"
                newsViewModel.loadMoreNews(category)
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun showProgressBar() {
        isLoading = true
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        isLoading = false
        binding.progressBar.visibility = View.GONE
    }
}