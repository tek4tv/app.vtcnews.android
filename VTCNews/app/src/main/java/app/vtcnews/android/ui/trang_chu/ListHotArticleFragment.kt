package app.vtcnews.android.ui.trang_chu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import app.vtcnews.android.databinding.ListHotarticleFragmentBinding
import app.vtcnews.android.ui.article_detail_fragment.ArticleDetailFragment
import app.vtcnews.android.viewmodels.ChannelPagingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListHotArticleFragment : Fragment() {
    lateinit var binding: ListHotarticleFragmentBinding
    private val viewModel: ChannelPagingViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListHotarticleFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getChannelPaging(1, requireArguments().getLong("id"))
        setUpListHotObeser()
    }

    fun setUpListHotObeser() {
        viewModel.listChannelPaging.observe(viewLifecycleOwner)
        {
            val adapter = ListHotArticleAdapter(it)
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.rvListHotArticle.adapter = adapter
            binding.rvListHotArticle.layoutManager = layoutManager
            adapter.onClickItem = { itemChannel ->
                ArticleDetailFragment.openWith(
                    parentFragmentManager,
                    itemChannel.id,
                    itemChannel.categoryID
                )
            }
        }
    }

    companion object {
        fun newInstance(id: Long? = null) = ListHotArticleFragment().apply {
            arguments = Bundle().apply {
                if (id != null) {
                    putLong("id", id)
                }
            }
        }
    }
}