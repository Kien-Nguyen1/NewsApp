package com.example.newsapp.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.activities.NewsApplication
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.models.NewsResponseItem
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Constants
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    private val application: Application,
    private val newsRepository: NewsRepository
) : AndroidViewModel(application) {

    val businessNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val cultureNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val educationNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val entertainmentNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val healthNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val lifeNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val news: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val politicsNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val sportsNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val universalNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchResults: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    private var currentSearchPage = 1
    private var currentSearchQuery: String? = null

    val newsResponses = mapOf(
        "Business" to businessNews,
        "Culture" to cultureNews,
        "Education" to educationNews,
        "Entertainment" to entertainmentNews,
        "Health" to healthNews,
        "Life" to lifeNews,
        "News" to news,
        "Politics" to politicsNews,
        "Sports" to sportsNews,
        "Universal" to universalNews
    )

    private val newsPages = mutableMapOf<String, Int>()

    init {
        newsResponses.keys.forEach { category ->
            newsPages[category] = 1
        }
    }

    fun getNews(category: String, page: Int, pageSize: Int) = viewModelScope.launch {
        safeNewsCall(category, page, pageSize)
    }

    val savedArticles: LiveData<List<NewsResponseItem>> = newsRepository.getSavedArticles()

    fun saveArticle(article: NewsResponseItem) = viewModelScope.launch {
        newsRepository.saveArticle(article)
    }

    fun deleteArticle(article: NewsResponseItem) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun searchNews(query: String, page: Int = 1, pageSize: Int = 20) = viewModelScope.launch {
        currentSearchQuery = query
        currentSearchPage = page
        searchResults.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val response = newsRepository.searchNews(query, page, pageSize)
                searchResults.postValue(handleSearchNewsResponse(response))
            } else {
                searchResults.postValue(Resource.Error("No Internet Connection!"))
            }
        } catch (t: Throwable) {
            Log.e("NewsViewModel", "Error fetching news", t)
            when (t) {
                is IOException -> searchResults.postValue(Resource.Error("Network Failure!"))
                else -> searchResults.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private suspend fun safeNewsCall(category: String, page: Int, pageSize: Int) {
        newsResponses[category]?.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getNews(category, page, pageSize)
                newsResponses[category]?.postValue(handleNewsResponse(category, response))
            } else {
                newsResponses[category]?.postValue(Resource.Error("No Internet Connection!"))
            }
        } catch (t: Throwable) {
            Log.e("NewsViewModel", "Error fetching news", t)
            when (t) {
                is IOException -> newsResponses[category]?.postValue(Resource.Error("Network Failure!"))
                else -> newsResponses[category]?.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private fun handleNewsResponse(category: String, response: Response<NewsResponse>): Resource<NewsResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                val currentPage = newsPages[category] ?: 1
                newsPages[category] = currentPage + 1
                val oldArticles = newsResponses[category]?.value?.data?.articles ?: mutableListOf()
                val newArticles = resultResponse.articles
                oldArticles.addAll(newArticles)
                return Resource.Success(resultResponse.copy(articles = oldArticles))
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                val oldArticles = searchResults.value?.data?.articles ?: mutableListOf()
                val newArticles = resultResponse.articles
                oldArticles.addAll(newArticles)
                return Resource.Success(resultResponse.copy(articles = oldArticles))
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    fun loadMoreNews(category: String) = viewModelScope.launch {
        val page = newsPages[category] ?: 1
        safeNewsCall(category, page, Constants.QUERY_PAGE_SIZE)
    }

    fun loadMoreSearchResults() = viewModelScope.launch {
        currentSearchQuery?.let { query ->
            currentSearchPage++
            searchResults.postValue(Resource.Loading())

            try {
                if (hasInternetConnection()) {
                    val response = newsRepository.searchNews(query, currentSearchPage, 20)
                    searchResults.postValue(handleSearchNewsResponse(response))
                } else {
                    searchResults.postValue(Resource.Error("No Internet Connection!"))
                }
            } catch (t: Throwable) {
                Log.e("NewsViewModel", "Error fetching news", t)
                when (t) {
                    is IOException -> searchResults.postValue(Resource.Error("Network Failure!"))
                    else -> searchResults.postValue(Resource.Error("Conversion Error!"))
                }
            }
        }
    }
}