package app.vtcnews.android.ui.trang_chu

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentTrangChuBinding
import app.vtcnews.android.model.TrangChuData
import app.vtcnews.android.ui.article_detail_fragment.ArticleDetailFragment
import app.vtcnews.android.ui.audio.AudioHomeFragment
import app.vtcnews.android.ui.audio.FragmentChiTietAudio
import app.vtcnews.android.ui.trang_chu_sub_section.TrangChuSubMenuFragment
import app.vtcnews.android.ui.video.FragmentChitietVideo
import app.vtcnews.android.viewmodels.TrangChuFragViewModel
import com.example.vtclive.Video.FragmentVideoPage
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
        binding = FragmentTrangChuBinding.inflate(layoutInflater, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycleView()
        setupTab()
        viewModel.getMenuList()
        viewModel.getData()
        registerObservers()
        binding.refreshlayoutHome.setOnRefreshListener{
            binding.refreshlayoutHome.isRefreshing = false
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_holder, TrangChuFragment.newInstance())
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.post {
            binding.root.post {
                binding.tabMenus.scrollTo(0, 0)
            }
        }
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
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.nointernet),
                    Toast.LENGTH_SHORT
                ).show()
            }

            viewModel.data.observe(viewLifecycleOwner)
            {
                data = it
                resetData()
            }
        }
    }

    private fun setupRecycleView() {
        controller.articleClickListener = {
            if (it.isVideoArticle == 1L) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_right,
                        0,
                        R.anim.exit_to_right
                    )
                    .replace(
                        R.id.frame_player_podcast,
                        FragmentChitietVideo.newInstance(
                            it.title ?: "",
                            it.id.toLong(),
                            it.categoryID ?: 0.toLong()
                        ), "fragVideo"
                    ).addToBackStack(null).commit()

            } else {
                ArticleDetailFragment.openWith(parentFragmentManager, it.id, it.categoryID!!)
            }
        }

        controller.hotChannelClickListener = {
            parentFragmentManager.beginTransaction()
                .add(R.id.fragment_holder, ListHotArticleFragment.newInstance(it.id))
                .addToBackStack(null)
                .commit()
        }
        controller.hotChannelHeaderClickListener ={
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_holder, AllChannelFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
        controller.videoClickListener =
            {
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_right,
                        0,
                        R.anim.exit_to_right
                    )
                    .replace(
                        R.id.frame_player_podcast,
                        FragmentChitietVideo.newInstance(it.title, it.id, it.categoryID),
                        "fragVideo"
                    ).addToBackStack(null).commit()
            }
        controller.btXemThemVideoClickListener =
            {
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                    .replace(R.id.fragment_holder, FragmentVideoPage.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        controller.audioClickListener =
            {
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(
                        R.id.fragment_holder,
                        FragmentChiTietAudio.newInstance(it.id)
                    )
                    .addToBackStack(null).commit()
            }
        controller.btXemThemAudioClickListener = {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .replace(R.id.fragment_holder, AudioHomeFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        binding.rvTrangchu.setController(controller)
        binding.rvTrangchu.setHasFixedSize(true)
        binding.rvTrangchu.setItemViewCacheSize(10)
    }

    private fun resetData() {
        controller.setData(data)
    }

    private fun setupTab() {

        binding.tabMenus.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) return
                if (tab == null) return

                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_holder,
                        TrangChuSubMenuFragment.newInstance(viewModel.menuList.value!![tab.position].id!!)
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