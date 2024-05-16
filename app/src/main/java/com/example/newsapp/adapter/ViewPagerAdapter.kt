package com.example.newsapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.newsapp.fragments.breaking_news.NewsFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    enum class NewsType {
        UNIVERSAL,
        BUSINESS,
        CULTURE,
        EDUCATION,
        ENTERTAINMENT,
        HEALTH,
        LIFE,
        NEWS,
        POLITICS,
        SPORTS,
    }

    override fun getCount(): Int {
        return NewsType.values().size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            NewsType.UNIVERSAL.ordinal -> NewsFragment.newInstance("Universal")
            NewsType.BUSINESS.ordinal -> NewsFragment.newInstance("Business")
            NewsType.CULTURE.ordinal -> NewsFragment.newInstance("Culture")
            NewsType.EDUCATION.ordinal -> NewsFragment.newInstance("Education")
            NewsType.ENTERTAINMENT.ordinal -> NewsFragment.newInstance("Entertainment")
            NewsType.HEALTH.ordinal -> NewsFragment.newInstance("Health")
            NewsType.LIFE.ordinal -> NewsFragment.newInstance("Life")
            NewsType.NEWS.ordinal -> NewsFragment.newInstance("News")
            NewsType.POLITICS.ordinal -> NewsFragment.newInstance("Politics")
            NewsType.SPORTS.ordinal -> NewsFragment.newInstance("Sports")
            else -> Fragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            NewsType.UNIVERSAL.ordinal -> "Tin thế giới"
            NewsType.BUSINESS.ordinal -> "Kinh doanh"
            NewsType.CULTURE.ordinal -> "Văn hóa và nghệ thuật"
            NewsType.EDUCATION.ordinal -> "Giáo dục"
            NewsType.ENTERTAINMENT.ordinal -> "Giải trí"
            NewsType.HEALTH.ordinal -> "Sức khỏe"
            NewsType.LIFE.ordinal -> "Đời sống"
            NewsType.NEWS.ordinal -> "Thời sự"
            NewsType.POLITICS.ordinal -> "Chính trị"
            NewsType.SPORTS.ordinal -> "Thể thao"
            else -> ""
        }
    }
}