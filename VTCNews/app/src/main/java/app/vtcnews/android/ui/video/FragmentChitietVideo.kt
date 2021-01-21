package app.vtcnews.android.ui.video

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.ImageView
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
import app.vtcnews.android.viewmodels.VideoHomeFragViewModel
import com.google.android.exoplayer2.MediaItem
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
        navBottom = requireActivity().findViewById(R.id.main_bottom_nav)
        toolbar = activity?.findViewById(R.id.toolbar)!!

        //delete when video is played
        val fragment = requireActivity().supportFragmentManager.findFragmentByTag("player")
        if(fragment != null)
        {
            requireActivity().supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
        lifecycleScope.launchWhenCreated {
            delay(1500)
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        }
        return binding.root
    }
    //rotate change
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        lifecycleScope.launchWhenCreated {
            delay(1500)
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        }
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            navBottom.isVisible = false
            toolbar.visibility = View.GONE
            binding.rootLayout.setTransition(R.id.start, R.id.start)
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
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                binding.videoView.requestLayout()
            }
            binding.rootLayout.setTransition(R.id.transitonVideo)
            navBottom.isVisible = true
            toolbar.visibility = View.VISIBLE
        }

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
                        binding.rootLayout.getConstraintSet(R.id.start)?.let {
                            it.constrainHeight(
                                R.id.video_view,
                                resources.getDimension(R.dimen._170sdp).toInt()
                            )
                            binding.videoView.requestLayout()
                        }
                        binding.rootLayout.getTransition(R.id.transitonVideo).setEnable(true)
                        navBottom?.isVisible = true
                        toolbar.visibility = View.VISIBLE
                        val r = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        r
                    } else {
                        val r = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//                        activity?.onBackPressed()
                        r
                    }
            }
            false
        })

    }

    private fun dataVideoDetailObser() {
        viewModel.videoDetail.observe(viewLifecycleOwner)
        {
            it.forEach()
            { videoDetail ->
                val url = videoDetail.videoURL
                if(url.isNotEmpty())
                {
                binding.videoView.player = player
                val mediaItem: MediaItem =
                    MediaItem.fromUri("https://media.vtc.vn/$url")
                player?.setMediaItem(mediaItem)
                player?.prepare()
                player?.playWhenReady = true
                binding.videoView.hideController()
                player?.play()
            }else{
                binding.videoView.setShowBuffering(SHOW_BUFFERING_ALWAYS)
            }
            }

        }
    }

    fun dataListNextVideoObser() {
        viewModel.listVideoByCategory.observe(viewLifecycleOwner)
        { listVideoByCategory ->
            val adapter = VideoItemAdapter(listVideoByCategory.take(12))
            val layoutManager = GridLayoutManager(context, 2)
            binding.rvVideoNext.adapter = adapter
            binding.rvVideoNext.layoutManager = layoutManager

            adapter.clickListen = { article: Article ->
                player?.release()
                requireActivity().supportFragmentManager.popBackStack()
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(0, 0, 0, R.anim.exit_to_right)
                    .replace(
                        R.id.frame_player_podcast,
                        newInstance(
                            article.title!!,
                            article.id.toLong(),
                            article.categoryID!!
                        ), "fragVideo"
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
            requireActivity().supportFragmentManager.popBackStack()
        }
        btFullsc?.setOnClickListener {
            val orientation = resources.configuration.orientation
            requireActivity().requestedOrientation =
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
                    navBottom.isVisible = true
                    toolbar.visibility = View.VISIBLE
                    val r = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    r
                } else {
                    activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    navBottom.isVisible = false
                    toolbar.visibility = View.GONE
                    binding.rootLayout.setTransition(R.id.start, R.id.start)
                    binding.rootLayout.getConstraintSet(R.id.start)?.let {
                        it.constrainHeight(
                            R.id.video_view,
                            ConstraintLayout.LayoutParams.MATCH_PARENT
                        )
                        binding.videoView.requestLayout()
                    }
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