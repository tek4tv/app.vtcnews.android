package app.vtcnews.android.ui.trang_chu_sub_section

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.vtcnews.android.model.MenuItem

class SubMenuStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    var menuList = listOf<MenuItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = menuList.size

    override fun createFragment(position: Int): Fragment {
        return ArticlesFragment.newInstance(menuList[position].id!!)
    }
}