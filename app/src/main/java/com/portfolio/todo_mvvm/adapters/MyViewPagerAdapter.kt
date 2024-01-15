package com.portfolio.todo_mvvm.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.portfolio.todo_mvvm.ui.calendar.ImageFragment
import com.portfolio.todo_mvvm.ui.calendar.ImageFragment2

class MyViewPagerAdapter(activity: FragmentActivity?): FragmentStateAdapter(activity!!) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if(position == 0){
            ImageFragment()
        } else {
            ImageFragment2()
        }
    }
}