package com.tbhero.application.adapters

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.tbhero.application.components.Fragment

class MainPagerAdapter(
    manager: FragmentManager,
    private val fragments: MutableList<Fragment>,
    private val titles: MutableList<String>
) : FragmentPagerAdapter(manager) {

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? = titles[position]
}