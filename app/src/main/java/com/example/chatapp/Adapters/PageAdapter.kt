package com.example.chatapp.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.chatapp.homefragments.ViewAllActive
import com.example.chatapp.homefragments.ViewAllFragment

class PageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {

        return 2

    }

    override fun getItem(position: Int): Fragment {

        when(position)
        {

            0 -> {
                return ViewAllFragment()
            }

            1->
            {
                return  ViewAllActive()
            }

            else ->
            {
                return ViewAllFragment()
            }

        }


    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Message"
            }
            1 -> {
                return "Active"
            }

        }
        return super.getPageTitle(position)
    }
}