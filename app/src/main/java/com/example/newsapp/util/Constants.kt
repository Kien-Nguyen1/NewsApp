package com.example.newsapp.util

class Constants {
    companion object {
        const val SEARCH_NEWS_TIME_DELAY = 500L
        const val KEY_ARTICLE = "KEY_ARTICLE"
        const val ARG_CATEGORY = "category"

        const val BASE_URL = "http://192.168.1.15/android_webservice/" //cho emulator
//        const val BASE_URL = "http://192.168.1.10/android_webservice/" //cho emulator
        // Nếu sử dụng thiết bị thật, hãy thay thế bằng địa chỉ IP cục bộ của máy bạn
        // private const val BASE_URL = "http://192.168.1.xxx/android_webservice/"
    }
}