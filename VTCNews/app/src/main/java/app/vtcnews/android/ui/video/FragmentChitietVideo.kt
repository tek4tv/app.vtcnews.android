package app.vtcnews.android.ui.video

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import app.vtcnews.android.R
import app.vtcnews.android.databinding.VideoFragmentMotionPlayerBinding
import app.vtcnews.android.model.Article
import app.vtcnews.android.ui.trang_chu_sub_section.getDateDiff
import app.vtcnews.android.viewmodels.VideoHomeFragViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView.SHOW_BUFFERING_ALWAYS
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class FragmentChitietVideo : Fragment() {
    private lateinit var binding: VideoFragmentMotionPlayerBinding
    private var player: SimpleExoPlayer? = null
    private val viewModel: VideoHomeFragViewModel by viewModels()
    private lateinit var navBottom: BottomNavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    companion object {
        fun newInstance(
            title: String,
            idVideoDetail: Long,
            categoryId: Long,
            des: String,
            cateName: String,
            date: String
        ) =
            FragmentChitietVideo().apply {
                arguments = Bundle().apply {
                    putString("title", title)
                    putLong("idvideodetail", idVideoDetail)
                    putLong("categoryid", categoryId)
                    putString("des", des)
                    putString("cateName", cateName)
                    putString("date", date)
                }
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VideoFragmentMotionPlayerBinding.inflate(layoutInflater, container, false)
        navBottom = requireActivity().findViewById(R.id.main_bottom_nav)
        toolbar = activity?.findViewById(R.id.toolbar)!!


        //delete when video is played
        val fragment = requireActivity().supportFragmentManager.findFragmentByTag("player")
        if (fragment != null) {
            requireActivity().supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
        lifecycleScope.launchWhenCreated {
            delay(2000)
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val navBottom =
                activity?.findViewById<BottomNavigationView>(R.id.main_bottom_nav)
            val orientation = resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                binding.rootLayout.getConstraintSet(R.id.start)?.let {
                    it.constrainHeight(
                        R.id.video_view,
                        resources.getDimension(R.dimen._170sdp).toInt()
                    )
                    binding.videoView.requestLayout()
                }
                binding.rootLayout.getTransition(R.id.transitonVideo).setEnable(true)
                navBottom?.isVisible = true
                requireActivity().requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }


        return binding.root
    }

    //rotate change
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        lifecycleScope.launchWhenCreated {
            delay(2000)
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        }
        val view = activity?.findViewById<View>(R.id.navShadow)
        val orientation = resources.configuration.orientation
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            navBottom.isVisible = false
            toolbar.visibility = View.GONE
            view!!.visibility = View.GONE
            binding.rootLayout.setTransition(R.id.start, R.id.start)
            binding.rootLayout.getTransition(R.id.transitonVideo).setEnable(false)
            binding.rootLayout.getConstraintSet(R.id.start)?.let {
                it.constrainHeight(
                    R.id.video_view,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
                )
                binding.videoView.requestLayout()
            }
        } else {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            binding.rootLayout.getConstraintSet(R.id.start)?.let {
                it.constrainHeight(
                    R.id.video_view,
                    resources.getDimension(R.dimen._170sdp).toInt()
                )
                binding.videoView.requestLayout()
            }
            view!!.visibility = View.VISIBLE
            toolbar.visibility = View.VISIBLE
            binding.rootLayout.getTransition(R.id.transitonVideo).setEnable(true)
            navBottom.isVisible = true
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        player = SimpleExoPlayer.Builder(requireContext()).build()
        binding.tvDes.text = requireArguments().getString("des")
        binding.tvTimeDiff.text = getDateDiff(
            requireArguments().getString("date")!!,
            requireContext().resources
        )
        binding.tvCategoryVideo.text = requireArguments().getString("cateName")
        binding.tvTitleVideo.text = requireArguments().getString("title")
        binding.txtVideoTitleMini.text = requireArguments().getString("title")
        binding.txtVideoTitleMini.isSelected = true
        buttonClick()
        dataListNextVideoObser()
        dataVideoDetailObser()


        binding.rootLayout.getConstraintSet(R.id.start)?.let {
            it.constrainHeight(
                R.id.video_view,
                resources.getDimension(R.dimen._170sdp).toInt()
            )
            it.constrainWidth(
                R.id.video_view, ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            binding.videoView.requestLayout()
        }


    }

    private fun dataVideoDetailObser() {
        viewModel.getVideoDetail(requireArguments().getLong("idvideodetail"))
            .observe(viewLifecycleOwner)
            {
                val url = it[0].videoURL
                if (url.isNotEmpty()) {
                    binding.videoView.player = player
                    val mediaItem: MediaItem =
                        MediaItem.fromUri("https://media.vtc.vn/$url")
                    player?.setMediaItem(mediaItem)
                    player?.prepare()
                    player?.playWhenReady = true
                    binding.videoView.hideController()
                    player?.play()
                } else {
                    binding.videoView.setShowBuffering(SHOW_BUFFERING_ALWAYS)
                }
            }
    }

    private fun dataListNextVideoObser() {

        viewModel.getVideoByCategory(1, requireArguments().getLong("categoryid"))
        viewModel.listVideoByCategory.observe(viewLifecycleOwner)
        { listVideoByCategory ->
            val adapter = VideoItemAdapter(listVideoByCategory.take(12))
            val layoutManager = GridLayoutManager(context, 2)
            binding.rvVideoNext.adapter = adapter
            binding.rvVideoNext.layoutManager = layoutManager
            binding.rvVideoNext.setHasFixedSize(true)

            adapter.clickListen = { article: Article ->
                viewModel.getVideoDetail(article.id.toLong()).observe(viewLifecycleOwner)
                {
                    if (it.isNotEmpty()) {
                        player?.release()
                        requireActivity().supportFragmentManager.popBackStack()
                        requireActivity().supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                0,
                                0, 0, android.R.anim.slide_out_right
                            )
                            .replace(
                                R.id.frame_player_podcast,
                                newInstance(
                                    article.title!!,
                                    article.id.toLong(),
                                    article.categoryID!!,
                                    article.description!!,
                                    article.categoryName!!,
                                    article.publishedDate!!
                                ), "fragVideo"
                            ).addToBackStack(null).commit()
                    } else {
                        Toast.makeText(context, "Video bị lỗi, không thể mở", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
            binding.rvVideoNext.isVisible = true
            binding.pbLoading.isVisible = false
        }


    }

    private fun buttonClick() {
        val btPlay = activity?.findViewById<ImageView>(R.id.bt_exo_play)
        val btFullsc = activity?.findViewById<ImageView>(R.id.bt_exo_fullscreen)
        player!!.addListener(object : Player.EventListener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    player!!.pause()
                    player!!.seekTo(0)
                    btPlay!!.setImageResource(R.drawable.ic_baseline_play_arrow)
                }
            }
        })

        btPlay!!.setOnClickListener {
            if (player!!.isPlaying) {
                player!!.pause()
                binding.btnPlayPauseMini.setImageResource(R.drawable.ic_play_arrow)
                btPlay.setImageResource(R.drawable.ic_baseline_play_arrow)
            } else {
                player!!.play()
                binding.btnPlayPauseMini.setImageResource(R.drawable.ic_pause)
                btPlay.setImageResource(R.drawable.ic_baseline_pause)
            }
        }

        binding.btnPlayPauseMini.setOnClickListener {
            if (player!!.isPlaying) {
                player!!.pause()
                binding.btnPlayPauseMini.setImageResource(R.drawable.ic_play_arrow)
                btPlay.setImageResource(R.drawable.ic_baseline_play_arrow)
            } else {
                player!!.play()
                binding.btnPlayPauseMini.setImageResource(R.drawable.ic_pause)
                btPlay.setImageResource(R.drawable.ic_baseline_pause)
            }
        }

        binding.closeMiniPlayer.setOnClickListener {
            navBottom.isVisible = true
            toolbar.visibility = View.VISIBLE
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }
        btFullsc?.setOnClickListener {
            val orientation = resources.configuration.orientation
            requireActivity().requestedOrientation =
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    val r = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    r
                } else {
                    val r = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    r
                }
        }

    }

    override fun onResume() {
        super.onResume()
        val btPlay = activity?.findViewById<ImageView>(R.id.bt_exo_play)
        if (player!!.isPlaying) {
            player?.pause()
            binding.btnPlayPauseMini.setImageResource(R.drawable.ic_play_arrow)
            btPlay?.setImageResource(R.drawable.ic_baseline_play_arrow)
        } else {
            player?.play()
            binding.btnPlayPauseMini.setImageResource(R.drawable.ic_pause)
            btPlay?.setImageResource(R.drawable.ic_baseline_pause)
        }
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

}