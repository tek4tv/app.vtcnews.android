package app.vtcnews.android.ui.trang_chu_sub_section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.vtcnews.android.MainActivity
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentArticlesBinding
import app.vtcnews.android.ui.article_detail_fragment.ArticleDetailFragment
import app.vtcnews.android.ui.video.FragmentChitietVideo
import app.vtcnews.android.viewmodels.PagingArticleFragmentViewModel
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.AndroidEntryPoint
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvArticlesList.adapter = pagingAdapter
        pagingAdapter.articleClickListener = {
            val fragmentManager = requireActivity().supportFragmentManager
            if (it.isVideoArticle == 1L) {
                val frame_player =
                    requireActivity().findViewById<FrameLayout>(R.id.frame_player_podcast)
                val params = frame_player.layoutParams
                params.width = FrameLayout.LayoutParams.MATCH_PARENT
                params.height = FrameLayout.LayoutParams.MATCH_PARENT
                frame_player.layoutParams = params
                val player = SimpleExoPlayer.Builder(requireContext()).build()
                player.release()
                fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                    .replace(
                        R.id.frame_player_podcast, FragmentChitietVideo.newInstance(
                            it.title ?: "",
                            it.id.toLong(),
                            it.categoryID!!.toLong()
                        ),"fragVideo"
                    )
                    .addToBackStack(null)
                    .commit()
            } else if (it.isVideoArticle == 0L) {
                ArticleDetailFragment.openWith(fragmentManager, it.id, it.categoryID!!)
            } else {
                //
            }
        }
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.pagingData.collectLatest {
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