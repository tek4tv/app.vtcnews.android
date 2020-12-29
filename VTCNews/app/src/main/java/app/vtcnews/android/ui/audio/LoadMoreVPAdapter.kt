package app.vtcnews.android.ui.audio

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.*

class LoadMoreVPAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {
    private val mFragmentList: MutableList<Fragment> =
        ArrayList()

    override fun getItemCount(): Int = mFragmentList.size


    override fun createFragment(position: Int): Fragment = mFragmentList[position]

    fun addFrag(fragment: Fragment) {
        mFragmentList.add(fragment)
        notifyDataSetChanged()
    }


}