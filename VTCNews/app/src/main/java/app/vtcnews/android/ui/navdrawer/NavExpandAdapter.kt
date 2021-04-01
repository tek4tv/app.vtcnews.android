package app.vtcnews.android.ui.navdrawer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import app.vtcnews.android.R
import app.vtcnews.android.model.NavDrawer.NavDrawerModel


class NavExpandAdapter(
    private val context: Context,
    private val listDataHeader: List<NavDrawerModel>,
    private val listDataChild: HashMap<NavDrawerModel, List<NavDrawerModel>>
) : BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
        return this.listDataHeader.size
    }

    override fun getChildrenCount(p0: Int): Int {
        if (listDataHeader[p0].isParent == false) {
            return 0
        } else {
            return this.listDataChild[this.listDataHeader[p0]]!!.size
        }
    }

    override fun getGroup(p0: Int): Any {
        return this.listDataHeader[p0]
    }

    override fun getChild(p0: Int, p1: Int): Any {
        return this.listDataChild[this.listDataHeader[p0]]!![p1]
    }

    override fun getGroupId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getChildId(p0: Int, p1: Int): Long {
        return p1.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view = convertView
        val listTitle = getGroup(listPosition) as NavDrawerModel
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.navdrawer_item_parent, null)
        }
        val listTitleTextView = view!!.findViewById<TextView>(R.id.tvMenu)
        val listic = view.findViewById<ImageView>(R.id.icMenu)
        val isParent = view.findViewById<ImageView>(R.id.icHaveChild)
        isParent.isVisible = listTitle.isParent == true
        listTitleTextView.text = listTitle.titleMenu
        listic.setImageResource(listTitle.ic!!)
        return view
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as NavDrawerModel
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.navdrawer_item_child, null)
        }
        val expandedListTextView = view!!.findViewById<TextView>(R.id.tvMenuChild)
        expandedListTextView.text = expandedListText.titleMenu
        return view
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

}