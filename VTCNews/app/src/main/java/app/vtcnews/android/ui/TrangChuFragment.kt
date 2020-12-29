package app.vtcnews.android.ui.trang_chu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentTrangChuBinding
import app.vtcnews.android.model.TrangChuData
import app.vtcnews.android.ui.trang_chu_sub_section.TrangChuSubSectionFragment
import app.vtcnews.android.viewmodels.TrangChuFragViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrangChuFragment : Fragment() {
    private lateinit var binding: FragmentTrangChuBinding
    private val viewModel: TrangChuFragViewModel by viewModels()
    private var data = TrangChuData()
    private val controller = TrangChuController()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTrangChuBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerObservers()
        setupRecycleView()
        setupTab()
        viewModel.getMenuList()
        viewModel.getData()
    }

    private fun registerObservers() {
        binding.apply {
            viewModel.menuList.observe(viewLifecycleOwner)
            {
                tabMenus.removeAllTabs()
                it.forEachIndexed { index, menuItem ->
                    val tab = if (index == 0)
                        tabMenus.newTab().setIcon(R.drawable.ic_baseline_home_24)
                    else
                        tabMenus.newTab().setText(menuItem.title)
                    tabMenus.addTab(tab)
                }
            }

            viewModel.error.observe(viewLifecycleOwner)
            {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }

            viewModel.data.observe(viewLifecycleOwner)
            {
                data = it
                resetData()
            }
        }
    }

    private fun setupRecycleView() {
        binding.rvTrangchu.setController(controller)
    }

    private fun resetData() {
        controller.setData(data)
    }

    private fun setupTab() {
        binding.tabMenus.post {
            binding.tabMenus.setScrollPosition(0,0f,true)
        }
        binding.tabMenus.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab?.position == 0) return
                if (tab == null) return

                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_holder,
                        TrangChuSubSectionFragment.newInstance(viewModel.menuList.value!![tab.position].id)
                    )
                    .addToBackStack("open_sub_menu")
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = TrangChuFragment()
    }
}