package app.vtcnews.android.ui.video

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.vtcnews.android.MainActivity
import app.vtcnews.android.R
import app.vtcnews.android.databinding.VideoFragmentMotionPlayerBinding
import app.vtcnews.android.model.Article
import app.vtcnews.android.viewmodels.VideoHomeFragViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentChitietVideo : Fragment() {
    private lateinit var binding: VideoFragmentMotionPlayerBinding
    private var player: SimpleExoPlayer? = null
    private val viewModel: VideoHomeFragViewModel by viewModels()
    private lateinit var navBottom: BottomNavigationView
    private var refreshLayout: SwipeRefreshLayout? = null

    companion object {
        fun newInstance(title: String, idVideoDetail: Long, categoryId: Long) =
            FragmentChitietVideo().apply {
                arguments = Bundle().apply {
                    putString("title", title)
                    putLong("idvideodetail", idVideoDetail)
                    putLong("categoryid", categoryId)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VideoFragmentMotionPlayerBinding.inflate(layoutInflater, container, false)
        (requireActivity() as MainActivity).supportActionBar?.hide()
        refreshLayout = activity?.findViewById(R.id.refreshlayout)
        refreshLayout?.isEnabled = false
        navBottom = requireActivity().findViewById(R.id.main_bottom_nav)
        navBottom.isVisible = false
        val frame_player =
            requireActivity().findViewById<FrameLayout>(R.id.frame_player_podcast)
        val params = frame_player.layoutParams
        params.width = FrameLayout.LayoutParams.MATCH_PARENT
        params.height = FrameLayout.LayoutParams.MATCH_PARENT
        frame_player.layoutParams = params


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        player = SimpleExoPlayer.Builder(requireContext()).build()
        viewModel.getVideoDetail(requireArguments().getLong("idvideodetail"))
        binding.tvTitleVideo.text = requireArguments().getString("title")
        binding.txtVideoTitleMini.text = requireArguments().getString("title")
        binding.txtVideoTitleMini.isSelected = true

        dataListNextVideoObser()
        dataVideoDetailObser()
        //val pageRandom = Random.nextInt(2, 3)
        viewModel.getVideoByCategory(1, requireArguments().getLong("categoryid"))

        binding.rootLayout.getConstraintSet(R.id.start)?.let {
            it.constrainHeight(
                R.id.video_view,
                resources.getDimension(R.dimen._170sdp).toInt()
            )
            binding.videoView.requestLayout()
        }
        binding.tvTitleVideo.isVisible = true
        binding.layoutTimeCate.isVisible = true
        binding.rvVideoNext.isVisible = true
        binding.tvVideoLQ.isVisible = true

        buttonClick()

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isEnabled) {
                        val orientation = resources.configuration.orientation
                        requireActivity().requestedOrientation =
                            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                val r = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                                binding.rootLayout.getConstraintSet(R.id.start)?.let {
                                    it.setVisibility(R.id.layoutMenu, View.VISIBLE)
                                    binding.layoutMenu.requestLayout()
                                }
                                r
                            } else {
                                isEnabled = false
                                navBottom.isVisible = true
                                refreshLayout?.isEnabled = true
                                (requireActivity() as MainActivity).supportActionBar?.show()
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .setCustomAnimations(
                                        0,
                                        R.anim.exit_to_right,
                                        0,
                                        R.anim.exit_to_right
                                    )
                                    .remove(
                                        this@FragmentChitietVideo
                                    )
                                    .commit()
                            }
                    }
                }
            }
            )
    }

    fun dataVideoDetailObser() {
        viewModel.videoDetail.observe(viewLifecycleOwner)
        {
            it.forEach()
            { videoDetail ->

                val url = videoDetail.videoURL
                binding.videoView.player = player
                val mediaItem: MediaItem =
                    MediaItem.fromUri("https://media.vtc.vn/$url")
                player?.setMediaItem(mediaItem)
                player?.prepare()
                player?.playWhenReady = true
                binding.videoView.hideController()
                player?.play()
            }
        }
    }

    fun dataListNextVideoObser() {
        viewModel.listVideoByCategory.observe(viewLifecycleOwner)
        { listVideoByCategory ->
            val adapter = VideoItemAdapter(listVideoByCategory.take(4))
            val layoutManager = GridLayoutManager(context, 2)
            binding.rvVideoNext.adapter = adapter
            binding.rvVideoNext.layoutManager = layoutManager

            adapter.clickListen = { article: Article ->
                val frame_player =
                    requireActivity().findViewById<FrameLayout>(R.id.frame_player_podcast)
                val params = frame_player.layoutParams
                params.width = FrameLayout.LayoutParams.MATCH_PARENT
                params.height = FrameLayout.LayoutParams.MATCH_PARENT
                frame_player.layoutParams = params
                player?.release()
                requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(0, 0, 0, R.anim.exit_to_right)
                    .replace(
                        R.id.frame_player_podcast,
                        newInstance(
                            article.title!!,
                            article.id.toLong(),
                            article.categoryID!!
                        )
                    ).addToBackStack(null).commit()
            }
        }
    }


    fun buttonClick() {
        val btPlay = activity?.findViewById<ImageView>(R.id.bt_exo_play)
        val btFullsc = activity?.findViewById<ImageView>(R.id.bt_exo_fullscreen)

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
            refreshLayout?.isEnabled = true
            (requireActivity() as MainActivity).supportActionBar?.show()

            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(0, R.anim.exit_to_right, 0, R.anim.exit_to_right).remove(this)
                .commit()
        }
        binding.btBack.setOnClickListener {
            (requireActivity() as MainActivity).supportActionBar?.show()
            navBottom.isVisible = true
            refreshLayout?.isEnabled = true
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(0, R.anim.exit_to_right, 0, R.anim.exit_to_right).remove(this)
                .commit()
        }
        btFullsc?.setOnClickListener {
            val orientation = resources.configuration.orientation
            requireActivity().requestedOrientation =
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    binding.rootLayout.getConstraintSet(R.id.start)?.let {
                        it.setVisibility(R.id.layoutMenu, View.VISIBLE)
                        it.constrainHeight(
                            R.id.video_view,
                            resources.getDimension(R.dimen._170sdp).toInt()
                        )
                        binding.layoutMenu.requestLayout()
                        binding.videoView.requestLayout()
                    }
                    val r = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    r
                } else {
                    activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    val r = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    binding.rootLayout.getConstraintSet(R.id.start)?.let {
                        it.setVisibility(R.id.layoutMenu, View.GONE)
                        it.constrainHeight(
                            R.id.video_view,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                        )
                        binding.layoutMenu.requestLayout()
                        binding.videoView.requestLayout()
                    }
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

    override fun onPause() {
        super.onPause()
        val frame_player =
            requireActivity().findViewById<FrameLayout>(R.id.frame_player_podcast)
        val params = frame_player.layoutParams
        params.width = FrameLayout.LayoutParams.MATCH_PARENT
        params.height = FrameLayout.LayoutParams.MATCH_PARENT
        frame_player.layoutParams = params
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        if (requireActivity().supportFragmentManager.findFragmentByTag("fragVideo") != null) {
            refreshLayout?.isEnabled = false
            navBottom.isVisible = false
            (requireActivity() as MainActivity).supportActionBar?.hide()
        } else {
            refreshLayout?.isEnabled = true
            navBottom.isVisible = true
            (requireActivity() as MainActivity).supportActionBar?.show()
        }

    }

}