package com.example.newsapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.newsapp.fragments.breaking_news.BusinessNewsFragment
import com.example.newsapp.fragments.breaking_news.CultureAndArtNewsFragment
import com.example.newsapp.fragments.breaking_news.EducationNewsFragment
import com.example.newsapp.fragments.breaking_news.EntertainmentNewsFragment
import com.example.newsapp.fragments.breaking_news.InternationalNewsFragment
import com.example.newsapp.fragments.breaking_news.NewsFragment
import com.example.newsapp.fragments.breaking_news.PoliticsNewsFragment
import com.example.newsapp.fragments.breaking_news.SportsNewsFragment

class BreakingNewsAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    enum class NewsType {
        INTERNATIONAL_NEWS,
        BUSINESS,
        CULTURE_AND_ART,
        EDUCATION,
        ENTERTAINMENT,
        NEWS,
        POLITICS,
        SPORTS
    }

    override fun getCount(): Int {
        return NewsType.values().size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            NewsType.INTERNATIONAL_NEWS.ordinal -> InternationalNewsFragment.newInstance()
            NewsType.BUSINESS.ordinal -> BusinessNewsFragment.newInstance()
            NewsType.CULTURE_AND_ART.ordinal -> CultureAndArtNewsFragment.newInstance()
            NewsType.EDUCATION.ordinal -> EducationNewsFragment.newInstance()
            NewsType.ENTERTAINMENT.ordinal -> EntertainmentNewsFragment.newInstance()
            NewsType.NEWS.ordinal -> NewsFragment.newInstance()
            NewsType.POLITICS.ordinal -> PoliticsNewsFragment.newInstance()
            NewsType.SPORTS.ordinal -> SportsNewsFragment.newInstance()
            else -> BlankFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            NewsType.INTERNATIONAL_NEWS.ordinal -> "Tin thế giới"
            NewsType.BUSINESS.ordinal -> "Kinh doanh"
            NewsType.CULTURE_AND_ART.ordinal -> "Văn hóa và nghệ thuật"
            NewsType.EDUCATION.ordinal -> "Giáo dục"
            NewsType.ENTERTAINMENT.ordinal -> "Giải trí"
            NewsType.NEWS.ordinal -> "Thời sự"
            NewsType.POLITICS.ordinal -> "Chính trị"
            NewsType.SPORTS.ordinal -> "Thể thao"
            else -> ""
        }
    }

    inner class BlankFragment : Fragment()
}