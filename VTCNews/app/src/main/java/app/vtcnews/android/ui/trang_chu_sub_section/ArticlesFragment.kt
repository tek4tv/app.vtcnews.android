package app.vtcnews.android.ui.trang_chu_sub_section

import android.content.pm.ActivityInfo
import android.os.Bundle
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
import app.vtcnews.android.databinding.FragmentArticlesBinding
import app.vtcnews.android.ui.article_detail_fragment.ArticleDetailFragment
import app.vtcnews.android.ui.video.FragmentChitietVideo
import app.vtcnews.android.viewmodels.PagingArticleFragmentViewModel
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val ARG_CATEGORY_ID = "param1"

@AndroidEntryPoint
class ArticlesFragment : Fragment() {
    private var categoryId: Int = 0
    private lateinit var binding: FragmentArticlesBinding
    private val viewModel by viewModels<PagingArticleFragmentViewModel>()
    private val pagingAdapter = ArticleAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getInt(ARG_CATEGORY_ID)
            viewModel.categoryId = categoryId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentArticlesBinding.inflate(inflater, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding.rvArticlesList.isVisible = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //refres adapter
        binding.refreshlayoutArticle.isEnabled = false
        val fragment = activity?.supportFragmentManager?.findFragmentByTag("trending")
        if (fragment != null) {
            binding.refreshlayoutArticle.isEnabled = true
            binding.refreshlayoutArticle.setOnRefreshListener {
                binding.refreshlayoutArticle.isRefreshing = false
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_holder, ArticlesFragment.newInstance(-1), "trending")
                    .commit()
            }
        }
        binding.rvArticlesList.adapter = pagingAdapter
        binding.rvArticlesList.setHasFixedSize(true)
//        binding.rvArticlesList.setItemViewCacheSize(15)


        pagingAdapter.articleClickListener = {
            val fragmentManager = requireActivity().supportFragmentManager
            if (it.isVideoArticle == 1L) {
                val player = SimpleExoPlayer.Builder(requireContext()).build()
                player.release()
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

                    } else {
                        viewModel.articleId = it.id
                        viewModel.getArticleDetail()
                        viewModel.boolean.observe(viewLifecycleOwner)
                        { isSucces ->
                            if (isSucces == true) {
                                ArticleDetailFragment.openWith(
                                    parentFragmentManager,
                                    it.id,
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

            } else if (it.isVideoArticle == 0L) {
                ArticleDetailFragment.openWith(fragmentManager, it.id, it.categoryID!!)
            }
        }
        setupObservers()

    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.pagingData.collectLatest {
                binding.rvArticlesList.isVisible = true
                binding.pbLoading.isVisible = false
                binding.tvNodata.visibility = View.GONE
                pagingAdapter.submitData(it)

            }
        }
    }

    companion object {
        //-1 for trending article
        @JvmStatic
        fun newInstance(categoryId: Int) =
            ArticlesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CATEGORY_ID, categoryId)
                }
            }
    }
}