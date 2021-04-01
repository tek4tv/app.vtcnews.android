package app.vtcnews.android.ui.article_detail_fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentArticleDetailBinding
import app.vtcnews.android.model.Article
import app.vtcnews.android.model.ArticleDetail
import app.vtcnews.android.ui.comment.CommentFragment
import app.vtcnews.android.ui.trang_chu.ArticleItemAdapter
import app.vtcnews.android.ui.trang_chu_sub_section.ArticlesFragment
import app.vtcnews.android.ui.trang_chu_sub_section.getDateDiff
import app.vtcnews.android.ui.video.FragmentChitietVideo
import app.vtcnews.android.viewmodels.ArticleDetailViewModel
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
        viewModel.articleId = articleId

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleDetailBinding.inflate(inflater, container, false)

        //xoa frag video neu mo article
        val fragment = requireActivity().supportFragmentManager.findFragmentByTag("fragVideo")
        if (fragment != null) {
            requireActivity().supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getArticleDetail()

        //lay list cung chuyen muc
        viewModel.getArticleByCategory(1, requireArguments().getLong("categoryId"))
        registerObservers()
        addCommentFrag()
        addNewNews()
        buttonClick()
        binding.articleDetailRoot.isVisible = true
    }

    private fun addCommentFrag() {
        parentFragmentManager.beginTransaction()
            .add(
                R.id.comment_frag_holder,
                CommentFragment.newInstance(requireArguments().getInt(ARG_PARAM1).toLong())
            )
            .commit()
    }

    private fun addNewNews() {
        parentFragmentManager.beginTransaction()
            .add(R.id.newNews_frag_holder, ArticlesFragment.newInstance(-1))
            .commit()
    }

    private fun buttonClick() {
        binding.txtCategory.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right,
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
                .add(
                    R.id.fragment_holder,
                    ArticleSameCategoryFragment.newInstance(
                        requireArguments().getLong("categoryId").toInt()
                    )
                )
                .addToBackStack(null)
                .commit()
        }
    }

    private fun registerObservers() {
        viewModel.articleDetail.observe(viewLifecycleOwner)
        {
            if (it != null) {
                binding.svContent.isVisible = true
                populateArticleUi(it)
                setupRvMoreArticle(it.relatedArticleList)
                lifecycleScope.launch {
                    delay(500)
                    binding.btnShareArticle.isVisible = true
                    binding.btnArticleLike.isVisible = true
                }
            } else {
                binding.svContent.isVisible = false
            }

        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

    }


    private fun setupRvMoreArticle(listArticle: List<Article>) {
        //list lien quan/them thong tin
        if (listArticle.isNotEmpty()) {
            binding.tvThemThongTin.visibility = View.VISIBLE
            val adapterMoreArticle = ArticleItemAdapter(listArticle)
            val layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.rvMoreArticle.adapter = adapterMoreArticle
            binding.rvMoreArticle.layoutManager = layoutManager
            binding.rvMoreArticle.setHasFixedSize(true)

            adapterMoreArticle.articleClickListener =
                { it ->
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
                                viewModel.getArticleDetailIsNull()
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
                                            Toast.makeText(
                                                context,
                                                R.string.videoerr,
                                                Toast.LENGTH_SHORT
                                            )
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
                        openWith(fragmentManager, it.id, it.categoryID!!)
                    }
                }
        } else {
            binding.tvThemThongTin.visibility = View.GONE
        }


        //list cung chuyen muc
        val layoutManagerSameCate = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        viewModel.articleListByCategory.observe(viewLifecycleOwner)
        {
            var adapterSameArticle = ArticleItemAdapter(it.take(8))
            binding.rvSameCategory.adapter = adapterSameArticle
            binding.rvSameCategory.layoutManager = layoutManagerSameCate
            binding.rvSameCategory.setHasFixedSize(true)
            adapterSameArticle.articleClickListener =
                { Article ->
                    openWith(parentFragmentManager, Article.id, Article.categoryID!!)
                }

        }
    }

    private fun populateArticleUi(articleDetail: ArticleDetail) {

        binding.apply {
            val detailData = articleDetail.detailData
            txtArticleTitle.text = detailData.title
            txtDate.text =
                getDateDiff(detailData.publishedDate!!, requireContext().resources)
            txtCategory.text = detailData.categoryName
            txtDescription.text = detailData.description
            txtArticleAuthor.text = detailData.author ?: ""

            binding.layoutTag.removeAllViews()
            for (i in articleDetail.listTag.indices) {
                val tvTag = TextView(context)
                if (i == 0) {
                    tvTag.text = articleDetail.listTag[0].tagName!!.trim()
                } else {
                    tvTag.text = ", ".plus(articleDetail.listTag[i].tagName).trim()
                }

                tvTag.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.blue
                    )
                )
                tvTag.textSize = 16f

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                tvTag.layoutParams = params
                binding.layoutTag.addView(tvTag)
                tvTag.setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right,
                            android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right
                        )
                        .add(
                            R.id.fragment_holder,
                            ListByTagFragment.newInstance(
                                articleDetail.listTag[i].tagTitleWithoutUnicode!!
                            )
                        )
                        .addToBackStack(null)
                        .commit()
                }
            }

            btnArticleLike.text =
                resources.getString(R.string.like, detailData.likeCount ?: 0L)

            btnShareArticle.setOnClickListener {
                val url = "https://vtc.vn/${detailData.seoSlug}-ar${detailData.id}.html"

                val intent = Intent(Intent.ACTION_SEND)
                intent.apply {
                    type = "text/*"
                    putExtra(Intent.EXTRA_TEXT, url)
                    putExtra(Intent.EXTRA_TITLE, detailData.title)
                    startActivity(Intent.createChooser(intent, "Chia sẻ"))
                }
            }
        }

        displayContent(articleDetail.detailData.content!!)
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun displayContent(data: String) {

        binding.webContent.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadsImagesAutomatically = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING

            val imageStyle = "<style>" +
                    ".img-fluid {max-width: 100%; height:auto;}" +
                    "p{padding-left:10px;padding-right:10px;}" +
                    ".expEdit {color:rgb(81, 81, 81);background:rgb(243, 243, 243);font-size: 14px;padding: 10px;line-height: 1.4;}" +
                    "</style>"

            val js = """
             ${'$'}(".lazy").each(function () {
                    ${'$'}(this).attr("src", ${'$'}(this).attr("data-src"));
                    ${'$'}(this).removeAttr("data-src");
                    ${'$'}(this).addClass('img-fluid');
                });
                
                var dataId = ${'$'}(".video-element").data("id");
                var text = '<div id="loadding" class="hidden d-flex justify-content-center" style="margin-bottom:60px; align-items:center">';
                text + '=  < i class="demo-icon icon-spin5 animate-spin" ></i >';
                text += '</div >';
                ${'$'}(".video-element").html(text);
                if (dataId) {
                    if (dataId.length > 0) {
                        var html = '';
                        ${'$'}.ajax({
                            url: "https://api.vtcnews.tek4tv.vn/api/home/video/GetVideoById?text=" + dataId,
                            type: 'GET'
                        }).done(function (data) {
                            html += '<script src="https://vjs.zencdn.net/ie8/1.1.2/videojs-ie8.min.js"></script>';
                            html += '<script src="https://vjs.zencdn.net/7.8.4/video.js"></script>'
                            html += '<video id="my-video"';
                            html += 'class="video-js lazy"';
                            html += 'controls';
                            html += ' preload="auto"';
                            html += ' height="300" ';
                            html += 'poster="MY_VIDEO_POSTER.jpg"';
                            html += ' data-setup="{}" style="width:100%">';
                            html += '  <source src="https://media.vtc.vn' + data[0].URL + '" type="video/mp4" />';
                            html += '   <source src="MY_VIDEO.webm" type="video/webm" />';
                            html += '</video> ';
                            ${'$'}(".video-element").html(html);
                        })
                    }
                }
            """.trimIndent()
            val script = """
                <link href="https://vjs.zencdn.net/7.10.2/video-js.css" rel="stylesheet" />
                <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
                <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
                <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
                <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/jquery.lazy/1.7.10/jquery.lazy.min.js"></script>
                <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/jquery.lazy/1.7.10/jquery.lazy.plugins.min.js"></script>
                <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
                <script src="https://vjs.zencdn.net/7.10.2/video.min.js"></script>
            """.trimIndent()
            webViewClient = object : WebViewClient() {

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    evaluateJavascript(js, null)
                }

                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    try {
                        Log.i("url", "link:  $url")
                        val browserIntent =
                            Intent("android.intent.action.VIEW", Uri.parse(url))
                        startActivity(browserIntent)
                        loadData(
                            "$imageStyle $script $data",
                            "text/html", "UTF-8"
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return false
                }


            }
            loadData(
                "$imageStyle $script $data",
                "text/html", "UTF-8"
            )


        }
    }


    companion object {
        @JvmStatic
        fun newInstance(articleId: Int, categogyId: Long) =
            ArticleDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, articleId)
                    putLong("categoryId", categogyId)
                }
            }

        fun openWith(fragmentManager: FragmentManager, articleId: Int, categogyId: Long) {
            fragmentManager.beginTransaction()
                .setCustomAnimations(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right,
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
                .replace(R.id.fragment_holder, newInstance(articleId, categogyId), "article")
                .addToBackStack(null)
                .commit()
        }
    }

}

