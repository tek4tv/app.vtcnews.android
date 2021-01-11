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
import app.vtcnews.android.viewmodels.TrangChuFragViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListHotArticleFragment : Fragment() {
    lateinit var binding: ListHotarticleFragmentBinding
    private val viewModel: TrangChuFragViewModel by viewModels()
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
        viewModel.getData()
        setUpListHotObeser()
    }
    fun setUpListHotObeser()
    {
        viewModel.data.observe(viewLifecycleOwner)
        {
            val adapter = ListHotArticleAdapter(it.hotArticles)
            val layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
           binding.rvListHotArticle.adapter = adapter
           binding.rvListHotArticle.layoutManager = layoutManager
            adapter.onClickItem = {
                ArticleDetailFragment.openWith(parentFragmentManager, it.id,it.categoryID!!)
            }
        }
    }

    companion object {
        fun newInstance() = ListHotArticleFragment()
    }
}