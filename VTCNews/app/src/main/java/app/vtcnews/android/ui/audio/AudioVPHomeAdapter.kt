package app.vtcnews.android.ui.audio

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter

class AudioVPHomeAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {
    private var fragmentList: MutableList<Fragment> = ArrayList()
    override fun getItemCount()= fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFrag(fragment: Fragment) {
        fragmentList.add(fragment)
        notifyDataSetChanged()
    }
}