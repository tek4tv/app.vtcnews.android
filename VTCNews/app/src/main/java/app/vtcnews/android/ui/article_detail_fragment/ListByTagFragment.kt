package app.vtcnews.android.ui.article_detail_fragment

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
import app.vtcnews.android.databinding.TagFragmentBinding
import app.vtcnews.android.ui.video.FragmentChitietVideo
import app.vtcnews.android.viewmodels.ListByTagPagingViewModel
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListByTagFragment : Fragment() {
    private var categoryId: String = ""
    private lateinit var binding: TagFragmentBinding
    private val viewModel by viewModels<ListByTagPagingViewModel>()
    private val pagingAdapter = ListByTagAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getString("tag").toString()
            viewModel.tag = categoryId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = TagFragmentBinding.inflate(inflater, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding.rvArticlesList.isVisible = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //refres adapter
        binding.rvArticlesList.adapter = pagingAdapter
        binding.rvArticlesList.setHasFixedSize(true)
        binding.rvArticlesList.setItemViewCacheSize(15)
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
                pagingAdapter.submitData(it)

            }
        }
        viewModel.getTag(requireArguments().getString("tag")!!).observe(viewLifecycleOwner)
        {
            binding.tvResultTag.text = it.toString()
            binding.tvTag.text = requireArguments().getString("tag").toString()
        }
    }

    companion object {
        //-1 for trending article
        @JvmStatic
        fun newInstance(tag: String) =
            ListByTagFragment().apply {
                arguments = Bundle().apply {
                    putString("tag", tag)
                }
            }
    }
}