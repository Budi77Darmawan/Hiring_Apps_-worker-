package com.example.hirejob.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class DetailViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTittleList = ArrayList<String>()


    override fun getCount() = mFragmentList.size

    override fun getItem(position: Int) = mFragmentList[position]

    override fun getPageTitle(position: Int) : CharSequence? {
        return mFragmentTittleList[position]
    }

    fun addFragment(fragment: Fragment, tittle: String) {
        mFragmentList.add(fragment)
        mFragmentTittleList.add(tittle)
    }
}