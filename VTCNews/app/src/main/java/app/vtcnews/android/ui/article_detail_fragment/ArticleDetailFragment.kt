package app.vtcnews.android.ui.article_detail_fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.webkit.WebSettings
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentArticleDetailBinding
import app.vtcnews.android.model.Article
import app.vtcnews.android.model.ArticleDetail
import app.vtcnews.android.model.ArticleVideo
import app.vtcnews.android.ui.audio.AudioHomeFragment
import app.vtcnews.android.ui.comment.CommentFragment
import app.vtcnews.android.ui.share.ShareFragment
import app.vtcnews.android.ui.trang_chu.ArticleItemAdapter
import app.vtcnews.android.ui.trang_chu.TrangChuFragment
import app.vtcnews.android.ui.trang_chu_sub_section.ArticlesFragment
import app.vtcnews.android.ui.trang_chu_sub_section.getDateDiff
import app.vtcnews.android.viewmodels.ArticleDetailViewModel
import com.example.vtclive.Video.FragmentVideoPage
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class ArticleDetailFragment : Fragment() {
    private var articleId: Int = 0
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var binding: FragmentArticleDetailBinding
    private var curFrag = "home"
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
        toolbar = activity?.findViewById(R.id.toolbar)!!
        val fragment = requireActivity().supportFragmentManager.findFragmentByTag("fragVideo")
        if (fragment != null) {
            requireActivity().supportFragmentManager.beginTransaction().remove(fragment).commit()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getArticleDetail()
//      viewModel.getArticleByCategory(1, requireArguments().getLong("categoryId"))
        registerObservers()
        addCommentFrag()
        buttonClick()
        binding.articleDetailRoot.isVisible = true
        val param = binding.articleVideo.layoutParams
        param.height = resources.getDimension(R.dimen._170sdp).toInt()
        binding.articleVideo.layoutParams = param
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_BACK) {
                val navBottom =
                    activity?.findViewById<BottomNavigationView>(R.id.main_bottom_nav)
                val orientation = resources.configuration.orientation
                requireActivity().requestedOrientation =
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                        navBottom?.isVisible = true
                        toolbar.visibility = View.VISIBLE
                        binding.articleDetailRoot.isVisible = true
                        val param = binding.articleVideo.layoutParams
                        param.height = resources.getDimension(R.dimen._170sdp).toInt()
                        binding.articleVideo.layoutParams = param
                        val r = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        r
                    } else {
                        val r = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//                        activity?.onBackPressed()
                        r
                    }
                true
            }
            false
        })
    }

    private fun addCommentFrag() {
        parentFragmentManager.beginTransaction()
            .add(
                R.id.comment_frag_holder,
                CommentFragment.newInstance(requireArguments().getInt(ARG_PARAM1).toLong())
            )
            .commit()
    }


    private fun setupNavDrawer() {
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val mainNavDrawer = requireActivity().findViewById<NavigationView>(R.id.main_nav_drawer)
        val mainBottomNav =
            requireActivity().findViewById<BottomNavigationView>(R.id.main_bottom_nav)
        val toggle = ActionBarDrawerToggle(
            requireActivity(),
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        toolbar.setNavigationIcon(R.drawable.ic_baseline_hambug)
        drawerLayout.addDrawerListener(toggle)
        requireActivity().actionBar?.setHomeButtonEnabled(true)
        toggle.syncState()

        mainNavDrawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_nav_drawer_trang_chu -> {
                    switchToTrangChu()
                    mainBottomNav.selectedItemId = R.id.menuTrangChu
                }

                R.id.item_nav_drawer_trending -> {
                    switchToTrending()
                    mainBottomNav.selectedItemId = R.id.menuTrending
                }

                R.id.item_nav_drawer_audio -> {
                    switchToAudio()
                    mainBottomNav.selectedItemId = R.id.menuAudio
                }
                R.id.item_nav_drawer_video -> {
                    switchToVideo()
                    mainBottomNav.selectedItemId = R.id.menuVideo
                }

            }

            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun switchToTrangChu() {
        if (
            curFrag == "home") {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_holder, TrangChuFragment.newInstance())
                .commit()
            curFrag = "home"
        } else {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .replace(R.id.fragment_holder, TrangChuFragment.newInstance())
                .commit()
            curFrag = "home"
        }

    }

    private fun switchToTrending() {

        if (curFrag == "trending") return

        requireActivity().supportFragmentManager.beginTransaction()
            ?.setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
            .replace(R.id.fragment_holder, ArticlesFragment.newInstance(-1))
            .commit()
        curFrag = "trending"

    }

    private fun switchToAudio() {


        if (curFrag == "audio") return

        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
            .replace(R.id.fragment_holder, AudioHomeFragment.newInstance())
            .commit()
        curFrag = "audio"

    }

    private fun switchToVideo() {

        if (curFrag == "video") return

        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
            .replace(R.id.fragment_holder, FragmentVideoPage.newInstance())
            .commit()
        curFrag = "video"

    }

    private fun switchToShare() {
        if (curFrag == "share") return

        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
            .replace(R.id.fragment_holder, ShareFragment.newInstance())
            .commit()
        curFrag = "share"

    }


    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }

    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24) {
            initPlayer()
        }
        val btPlay = activity?.findViewById<ImageView>(R.id.bt_exo_play)
        if (player!!.isPlaying) {

            player!!.pause()
            btPlay?.setImageResource(R.drawable.ic_baseline_play_arrow)

        } else {
            player!!.play()
            btPlay?.setImageResource(R.drawable.ic_baseline_pause)
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
            if (it != null) {
                binding.pb.visibility = View.GONE
                binding.svContent.isVisible = true
                populateArticleUi(it)
                setupRvMoreArticle(it.relatedArticleList)
            } else {
                binding.pb.isVisible = true
                binding.svContent.isVisible = false
            }

        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.articleVideos.observe(viewLifecycleOwner)
        {
            setupVideoList(it)
        }
    }


    private fun setupRvMoreArticle(listArticle: List<Article>) {
        val adapterMoreArticle = ArticleItemAdapter(listArticle.take(8))
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvMoreArticle.adapter = adapterMoreArticle
        binding.rvMoreArticle.layoutManager = layoutManager

        adapterMoreArticle.articleClickListener =
            { Article ->
                parentFragmentManager.popBackStack()
                openWith(parentFragmentManager, Article.id, Article.categoryID!!)
            }
    }

    private fun setupVideoList(videoList: List<ArticleVideo>) {
        viewModel.curVideo = videoList[0]
        player?.clearMediaItems()
        playVideo(videoList.first())
        videoList.drop(1).forEach {
            val mediaItem = MediaItem.fromUri("https://media.vtc.vn${it.url}")
            player?.addMediaItem(mediaItem)
        }
    }

    private fun initPlayer() {
        player = SimpleExoPlayer.Builder(requireContext())
            .build()
        binding.articleVideo.player = player
        player?.playWhenReady = true
        playVideo(viewModel.curVideo)
        binding.articleVideo.hideController()
        player?.play()
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
            val rootLayout = binding.articleDetailRoot
            binding.articleVideo.visibility = View.VISIBLE

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
        }

        binding.apply {
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

        binding.webContent.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadsImagesAutomatically = true
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING

            val imageStyle =
                "<style>img{max-width:100%;height: auto; }</style>"

            loadData(
                "$imageStyle $imgTags",
                "text/html", "UTF-8",
            )
        }
    }

    private fun buttonClick() {
        val navBottom = activity?.findViewById<BottomNavigationView>(R.id.main_bottom_nav)
        val btPlay = activity?.findViewById<ImageView>(R.id.bt_exo_play)
        val btFullsc = activity?.findViewById<ImageView>(R.id.bt_exo_fullscreen)
        btFullsc?.setOnClickListener {
            val orientation = resources.configuration.orientation
            requireActivity().requestedOrientation =
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    navBottom?.isVisible = true
                    toolbar.visibility = View.VISIBLE
                    binding.articleDetailRoot.isVisible = true
                    val param = binding.articleVideo.layoutParams
                    param.height = resources.getDimension(R.dimen._170sdp).toInt()
                    binding.articleVideo.layoutParams = param
                    val r = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    r
                } else {
                    activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    binding.articleDetailRoot.isVisible = false
                    navBottom?.isVisible = false
                    toolbar.visibility = View.GONE
                    val param = binding.articleVideo.layoutParams
                    param.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                    binding.articleVideo.layoutParams = param
                    val r = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    r
                }
        }
        btPlay!!.setOnClickListener {
            if (player!!.isPlaying) {
                player!!.pause()
                btPlay.setImageResource(R.drawable.ic_baseline_play_arrow)
            } else {
                player!!.play()
                btPlay.setImageResource(R.drawable.ic_baseline_pause)
            }
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
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .add(R.id.fragment_holder, newInstance(articleId, categogyId), "article")
                .addToBackStack(null)
                .commit()
        }
    }

}

