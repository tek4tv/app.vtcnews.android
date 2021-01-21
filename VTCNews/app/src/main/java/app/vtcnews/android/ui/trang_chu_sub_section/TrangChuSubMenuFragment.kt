package app.vtcnews.android.ui.trang_chu_sub_section

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentTrangChuSubSectionBinding
import app.vtcnews.android.viewmodels.TrangChuSubMenuViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.lang.NullPointerException

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class TrangChuSubMenuFragment : Fragment() {

    private var parentMenuId: Int = 0
    private val viewModel by viewModels<TrangChuSubMenuViewModel>()
    private lateinit var vpAdapter : SubMenuStateAdapter
    private lateinit var binding : FragmentTrangChuSubSectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            parentMenuId = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrangChuSubSectionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMenuItem(parentMenuId)
        setupViewPager()
        setupObservers()

    }

    override fun onResume() {
        super.onResume()
        binding.root.post {
            binding.root.post {
                binding.tabMenuItem.scrollTo(0, 0)
            }
        }
    }

    private fun setupObservers()
    {
        viewModel.menuItem.observe(viewLifecycleOwner)
        {
            if(it.isNotEmpty()) {
                binding.tvNodata.isVisible = false
                vpAdapter.menuList = it
            }
            else
            {
                binding.tvNodata.isVisible = true
            }
        }
    }

    private fun setupViewPager()
    {
        vpAdapter = SubMenuStateAdapter(this)
        binding.vpSubSectionDetail.adapter = vpAdapter

        TabLayoutMediator(binding.tabMenuItem, binding.vpSubSectionDetail){
            tab, pos->
            tab.text = viewModel.menuItem.value!![pos].title
        }.attach()
    }

    companion object {
        @JvmStatic
        fun newInstance(parentMenuId:Int) =
            TrangChuSubMenuFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, parentMenuId)
                }
            }
    }

}