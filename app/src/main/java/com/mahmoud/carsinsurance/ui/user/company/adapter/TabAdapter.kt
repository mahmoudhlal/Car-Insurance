package com.mahmoud.carsinsurance.ui.user.company.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mahmoud.carsinsurance.ui.user.company.tabs.InsuranceRequestFragment
import com.mahmoud.carsinsurance.ui.user.company.tabs.RenewCarFragment

class TabAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {

    private var fragmentList : ArrayList<Fragment> = ArrayList()
    private val NUM_PAGES = 2

    fun putFragmentList(fragments: ArrayList<Fragment>){
        fragmentList = fragments
    }

/*
    init {
        fragmentList = arrayListOf(InsuranceRequestFragment(),RenewCarFragment())
    }
*/

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment = fragmentList[position]

}