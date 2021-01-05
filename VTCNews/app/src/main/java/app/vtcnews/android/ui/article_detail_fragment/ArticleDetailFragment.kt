package app.vtcnews.android.ui.article_detail_fragment

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.vtcnews.android.databinding.FragmentArticleDetailBinding
import app.vtcnews.android.model.ArticleDetail
import app.vtcnews.android.ui.trang_chu_sub_section.getDateDiff
import app.vtcnews.android.viewmodels.ArticleDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class ArticleDetailFragment : Fragment() {
    private var articleId: Int = 0

    private lateinit var binding: FragmentArticleDetailBinding

    private val viewModel by viewModels<ArticleDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            articleId = it.getInt(ARG_PARAM1)
        }
        viewModel.articleId = 588860//this.articleId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerObservers()
        viewModel.getArticleDetail()
    }

    private fun registerObservers() {
        viewModel.articleDetail.observe(viewLifecycleOwner)
        {
            populateUi(it)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun populateUi(articleDetail: ArticleDetail) {
        binding.detailLayout.apply {
            txtTitle.text = articleDetail.detailData.title
            txtDate.text =
                getDateDiff(articleDetail.detailData.publishedDate, requireContext().resources)
            txtCategory.text = articleDetail.detailData.categoryName
            txtDescription.text = articleDetail.detailData.description
        }

        displayContent(articleDetail.detailData.content)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun displayContent(data: String) {
        val videoReg = Regex("<div.*?class=\"video-element\".*?><\\/div>")
        val t = videoReg.find(data)?.value!!

        val imgReg = Regex("<img .*?>")
        val srcReg = Regex("data-src=\".*?\"")
        val altReg = Regex(" alt=\".*?\"")
        val imgTags = imgReg.replace(data) {
            var res = "<img "
            res += "${altReg.find(it.value)?.value}  ${srcReg.find(it.value)?.value?.drop(5)}>"
            res
        }

        binding.detailLayout.webContent.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING

            val imageStyle = "<style>img{max-width: 100%; height:auto;}</style>"

            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            loadData(
                "$imageStyle $imgTags",
                "text/html", "UTF-8",
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(articleId: Int) =
            ArticleDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, articleId)
                }
            }
    }
}