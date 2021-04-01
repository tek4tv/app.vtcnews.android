package app.vtcnews.android.ui.trang_chu

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentTrangChuBinding
import app.vtcnews.android.model.TrangChuData
import app.vtcnews.android.ui.article_detail_fragment.ArticleDetailFragment
import app.vtcnews.android.ui.audio.AudioWithoutTab
import app.vtcnews.android.ui.audio.FragmentChiTietAudio
import app.vtcnews.android.ui.trang_chu_sub_section.TrangChuSubMenuFragment
import app.vtcnews.android.ui.video.FragmentChitietVideo
import app.vtcnews.android.ui.video.FragmentVideoPage
import app.vtcnews.android.viewmodels.TrangChuFragViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TrangChuFragment : Fragment() {
    private lateinit var binding: FragmentTrangChuBinding
    private val viewModel by viewModels<TrangChuFragViewModel>()
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
        binding.rvTrangchu.isVisible = false
        setupRecycleView()
        setupTab()
        viewModel.getMenuList()
        viewModel.getData()
        registerObservers()
        binding.refreshlayoutHome.setOnRefreshListener {
            binding.refreshlayoutHome.isRefreshing = false
            requireActivity().supportFragmentManager.beginTransaction().setCustomAnimations(0, 0)
                .replace(R.id.fragment_holder, newInstance())
                .commit()
        }
    }


    private fun registerObservers() {
        binding.apply {
            viewModel.menuList.observe(viewLifecycleOwner)
            {
                tabMenus.removeAllTabs()
                tabMenus.addTab(binding.tabMenus.newTab().setIcon(R.drawable.ic_baseline_home_24))
                it.forEach()
                { menuItem ->
                    Log.i("menu", "menu" + menuItem.title)
                    val tab = tabMenus.newTab().setText(menuItem.title)
                    tabMenus.addTab(tab)
                }
                binding.rvTrangchu.isVisible = true
                binding.pbLoading.isVisible = false
            }
            viewModel.error.observe(viewLifecycleOwner)
            {
                Log.e("errHome", "Error: $it")
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
                viewModel.getVideoDetail(it.id.toLong()).observe(viewLifecycleOwner)
                { listVideoDetail ->
                    if (listVideoDetail.isNotEmpty()) {
                        if (requireActivity().supportFragmentManager.findFragmentByTag("fragVideo") != null) {
                            requireActivity().supportFragmentManager.popBackStack()
                        }
                        requireActivity().supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right,
                                android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right
                            )
                            .replace(
                                R.id.frame_player_podcast,
                                FragmentChitietVideo.newInstance(
                                    it.title!!,
                                    it.id.toLong(),
                                    it.categoryID!!,
                                    it.description!!,
                                    it.categoryName!!,
                                    it.publishedDate!!
                                ), "fragVideo"
                            ).addToBackStack(null).commit()
                        //view?.findNavController()?.navigate(R.id.action_trangChuFragment_to_fragmentChitietVideo)

                    } else {
                        viewModel.articleId = it.id
                        viewModel.getArticleDetail()
                        viewModel.boolean.observe(viewLifecycleOwner)
                        { isSucces ->
                            if (isSucces == true) {
                                ArticleDetailFragment.openWith(
                                    parentFragmentManager,
                                    it.id.toInt(),
                                    it.categoryID!!
                                )
                            } else {
                                val toast =
                                    Toast.makeText(context, R.string.videoerr, Toast.LENGTH_SHORT)
                                toast.show()
                                lifecycleScope.launch()
                                {
                                    delay(200)
                                    toast.cancel()
                                }
                            }
                        }

                    }
                }

            } else {
                ArticleDetailFragment.openWith(parentFragmentManager, it.id, it.categoryID!!)
            }
        }

        controller.hotChannelClickListener = {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .add(R.id.fragment_holder, ListHotArticleFragment.newInstance(it.id))
                .addToBackStack(null)
                .commit()
        }
        controller.hotChannelHeaderClickListener = {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .add(R.id.fragment_holder, AllChannelFragment.newInstance())
                .addToBackStack(null)
                .commit()
            //view?.findNavController()?.navigate(R.id.action_trangChuFragment_to_articleDetailFragment,bundle)
        }
        controller.videoClickListener =
            {
                viewModel.getVideoDetail(it.id).observe(viewLifecycleOwner)
                { listVideoDetail ->
                    if (listVideoDetail.isNotEmpty()) {
                        if (requireActivity().supportFragmentManager.findFragmentByTag("fragVideo") != null) {
                            requireActivity().supportFragmentManager.popBackStack()
                        }
                        requireActivity().supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right,
                                android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right
                            )
                            .replace(
                                R.id.frame_player_podcast,
                                FragmentChitietVideo.newInstance(
                                    it.title,
                                    it.id,
                                    it.categoryID,
                                    it.description!!,
                                    it.categoryName!!,
                                    it.publishedDate
                                ), "fragVideo"
                            ).addToBackStack(null).commit()

                    } else {
                        viewModel.articleId = it.id.toInt()
                        viewModel.getArticleDetail()
                        viewModel.boolean.observe(viewLifecycleOwner)
                        { isSucces ->
                            if (isSucces == true) {
                                ArticleDetailFragment.openWith(
                                    parentFragmentManager,
                                    it.id.toInt(),
                                    it.categoryID
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "Video bị lỗi, không thể mở",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        controller.btXemThemVideoClickListener =
            {
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                    .add(R.id.fragment_holder, FragmentVideoPage.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        controller.audioClickListener =
            {
                viewModel.checkAlbumDetail(it.id).observe(viewLifecycleOwner)
                { isSucces ->
                    if (isSucces) {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right,
                                android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right
                            )
                            .replace(
                                R.id.fragment_holder,
                                FragmentChiTietAudio.newInstance(
                                    it.id,
                                    it.channelID
                                )
                            )
                            .addToBackStack("audio").commit()
                    } else {
                        val toast = Toast.makeText(context, R.string.audioerr, Toast.LENGTH_SHORT)
                        toast.show()
                        lifecycleScope.launch()
                        {
                            delay(200)
                            toast.cancel()
                        }
                    }


                }
            }
        controller.btXemThemAudioClickListener = {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .add(R.id.fragment_holder, AudioWithoutTab.newInstance())
                .addToBackStack(null)
                .commit()
        }

        binding.rvTrangchu.setController(controller)
        binding.rvTrangchu.setHasFixedSize(true)
    }

    private fun resetData() {
        controller.setData(data)
    }

    private fun setupTab() {

        binding.tabMenus.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) return
                if (tab == null) return
                if (tab.text!! == "Du Lịch") {
                    val browserIntent =
                        Intent("android.intent.action.VIEW", Uri.parse("https://duclich.vtc.vn"))
                    startActivity(browserIntent)
                } else {
                    parentFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right,
                            android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right
                        )
                        .replace(
                            R.id.fragment_holder,
                            TrangChuSubMenuFragment.newInstance(viewModel.menuList.value!![tab.position - 1].id!!)
                        )
                        .addToBackStack(null)
                        .commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    override fun onStart() {
        super.onStart()
        requireActivity().overridePendingTransition(0, 0)
    }

    override fun onResume() {
        super.onResume()
        binding.root.post {
            binding.root.post {
                binding.tabMenus.scrollTo(0, 0)

            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = TrangChuFragment()
    }
}