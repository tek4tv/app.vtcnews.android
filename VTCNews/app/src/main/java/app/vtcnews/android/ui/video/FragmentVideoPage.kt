package app.vtcnews.android.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import app.vtcnews.android.R
import app.vtcnews.android.databinding.LayoutVideoPageBinding
import app.vtcnews.android.ui.article_detail_fragment.ArticleDetailFragment
import app.vtcnews.android.viewmodels.VideoHomeFragViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FragmentVideoPage : Fragment() {
    lateinit var binding: LayoutVideoPageBinding
    val viewModel: VideoHomeFragViewModel by viewModels()
    private val pagingVideoAdapter = VideoMenuPagingAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutVideoPageBinding.inflate(layoutInflater, container, false)
//        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding.rvVideoHome.isVisible = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvVideoHome.adapter = pagingVideoAdapter
        binding.rvVideoHome.layoutManager = layoutManager
        binding.rvVideoHome.setHasFixedSize(true)
        binding.rvVideoHome.isVisible = true
        binding.pbLoading.isVisible = false
        pagingVideoAdapter.clickItemChannel = { video ->
            viewModel.getVideoDetail(video.id).observe(viewLifecycleOwner)
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
                                video.title,
                                video.id,
                                video.categoryID,
                                video.description!!,
                                video.categoryName!!,
                                video.publishedDate
                            ), "fragVideo"
                        ).addToBackStack(null).commit()

                } else {
                    ArticleDetailFragment.openWith(
                        parentFragmentManager,
                        video.id.toInt(),
                        video.categoryID
                    )
                }
            }
        }

        binding.refreshlayoutVideo.setOnRefreshListener {
            binding.refreshlayoutVideo.isRefreshing = false
            requireActivity().supportFragmentManager.beginTransaction().setCustomAnimations(0, 0)
                .replace(R.id.fragment_holder, newInstance())
                .commit()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.pagingData.collectLatest {
                pagingVideoAdapter.submitData(it)
            }
        }
    }


    companion object {
        fun newInstance() = FragmentVideoPage()
    }
}