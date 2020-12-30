package app.vtcnews.android.ui.trang_chu_sub_section

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import app.vtcnews.android.databinding.FragmentTrangChuSubSectionBinding
import app.vtcnews.android.viewmodels.TrangChuSubMenuViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

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

    private fun setupObservers()
    {
        viewModel.menuItem.observe(viewLifecycleOwner)
        {
            vpAdapter.menuList = it
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