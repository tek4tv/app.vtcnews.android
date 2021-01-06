package app.vtcnews.android.ui.article_detail_fragment

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContentResolverCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentArticleDetailBinding
import app.vtcnews.android.model.ArticleDetail
import app.vtcnews.android.model.ArticleVideo
import app.vtcnews.android.ui.trang_chu_sub_section.getDateDiff
import app.vtcnews.android.viewmodels.ArticleDetailViewModel
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import java.io.FileNotFoundException
import java.io.IOException

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class ArticleDetailFragment : Fragment() {
    private var articleId: Int = 0

    private lateinit var binding: FragmentArticleDetailBinding

    private val viewModel by viewModels<ArticleDetailViewModel>()

    private var player: SimpleExoPlayer? = null

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerObservers()
        viewModel.getArticleDetail()
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initPlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24) {
            initPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }


    private fun registerObservers() {
        viewModel.articleDetail.observe(viewLifecycleOwner)
        {
            populateArticleUi(it)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.articleVideos.observe(viewLifecycleOwner)
        {
            setupVideoList(it)
        }
    }


    private fun setupVideoList(videoList: List<ArticleVideo>) {
        viewModel.curVideo = videoList[0]
        player?.clearMediaItems()
        videoList.forEach {
            val mediaItem = MediaItem.fromUri("https://media.vtc.vn${it.url}")
            player?.addMediaItem(mediaItem)
        }
    }

    private fun initPlayer() {
        player = SimpleExoPlayer.Builder(requireContext())
            .build()
        binding.detailLayout.articleVideo.player = player
        player?.playWhenReady = true
        playVideo(viewModel.curVideo)
    }

    private fun releasePlayer() {
        if (player != null) {
            viewModel.apply {
                playbackPosition = player?.currentPosition!!
                currentWindow = player?.currentWindowIndex!!
            }
            player?.release()
            player = null
        }
    }

    private fun playVideo(video: ArticleVideo?) {
        if (video == null) return

        if (video.id != viewModel.curVideo?.id)
            viewModel.resetVideoParams()

        val mediaItem = MediaItem.fromUri("https://media.vtc.vn${video.url}")
        player?.apply {
            setMediaItem(mediaItem)
            seekTo(viewModel.currentWindow, viewModel.playbackPosition)
            prepare()
        }
        viewModel.curVideo = video
    }

    private fun populateArticleUi(articleDetail: ArticleDetail) {
        val videoList = getVideoList(articleDetail.detailData.content!!)

        if (videoList.isNotEmpty()) {
            val rootLayout = binding.detailLayout.articleDetailRoot
            binding.detailLayout.imgArticleThumb.visibility = View.GONE
            binding.detailLayout.articleVideo.visibility = View.VISIBLE

            val rootConstraintSet = ConstraintSet()
            rootConstraintSet.clone(rootLayout)

            rootConstraintSet.connect(
                R.id.txt_article_title,
                ConstraintSet.TOP,
                R.id.article_video,
                ConstraintSet.BOTTOM
            )

            rootConstraintSet.applyTo(rootLayout)

            loadVideo(videoList)
        } else {
            Glide.with(binding.detailLayout.imgArticleThumb)
                .load("https://image.vtc.vn${articleDetail.detailData.imageURL}")
                .into(binding.detailLayout.imgArticleThumb)
        }

        binding.detailLayout.apply {
            val detailData = articleDetail.detailData
            txtArticleTitle.text = detailData.title
            txtDate.text =
                getDateDiff(detailData.publishedDate!!, requireContext().resources)
            txtCategory.text = detailData.categoryName
            txtDescription.text = detailData.description
            txtArticleAuthor.text = detailData.author ?: ""

            txtArticleTags.text =
                articleDetail.listTag.fold("") { str, tag ->
                    str.plus(tag.tagName ?: "").plus(", ")
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

        displayContent(articleDetail.detailData.content)
    }

    private fun loadVideo(videoList: List<String>) {
        viewModel.getVideoList(videoList)
    }

    private fun getVideoList(data: String): List<String> {
        val videoReg = Regex("<div.*?class=\"video-element\".*?>")
        val videoIdReg = Regex("data-id=\".*?\"")
        return videoReg.findAll(data).map {
            videoIdReg.find(it.value)!!.value.drop(9).dropLast(1)
        }.toList()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun displayContent(data: String) {

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

        fun openWith(fragmentManager: FragmentManager, articleId: Int) {
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_holder, newInstance(articleId))
                .addToBackStack(null)
                .commit()
        }
    }
}

