package app.vtcnews.android.ui.trang_chu_sub_section

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentArticlesBinding
import app.vtcnews.android.ui.article_detail_fragment.ArticleDetailFragment
import app.vtcnews.android.ui.video.FragmentChitietVideo
import app.vtcnews.android.viewmodels.PagingArticleFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val ARG_CATEGORY_ID = "param1"

@AndroidEntryPoint
class ArticlesFragment : Fragment() {
    private var categoryId: Int = -1
    private lateinit var binding : FragmentArticlesBinding
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
            val fragmentManager = if(categoryId == -1) parentFragmentManager
            else requireParentFragment().requireActivity().supportFragmentManager

            if(it.isVideoArticle == 1L)
            {
                fragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_holder, FragmentChitietVideo.newInstance(
                        it.title ?: "",
                        it.id.toLong(),
                        it.categoryID!!.toLong()
                    ))
                    .addToBackStack(null)
                    .commit()
            }
            else
            {
                ArticleDetailFragment.openWith(fragmentManager, it.id)
            }
        }
        setupObservers()
    }

    private fun setupObservers()
    {
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