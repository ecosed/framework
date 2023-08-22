package io.ecosed.libecosed.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.ecosed.libecosed.fragment.EmptyFragment
import io.ecosed.libecosed.fragment.SettingsFragment

internal class PagerAdapter(
    activity: FragmentActivity,
    mainFragment: Fragment?,
    settingsFragment: Fragment?,
) : FragmentStateAdapter(activity) {

    private val mMainFragment: Fragment? = mainFragment
    private val mSettingsFragment: Fragment? = settingsFragment

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> mMainFragment ?: EmptyFragment.newInstance()
            1 -> mSettingsFragment ?: EmptyFragment.newInstance()
            2 -> SettingsFragment.newInstance()
            else -> EmptyFragment.newInstance()
        }
    }
}