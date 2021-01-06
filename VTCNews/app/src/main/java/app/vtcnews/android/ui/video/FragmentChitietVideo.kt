package app.vtcnews.android.ui.video

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.app.NotificationCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import app.vtcnews.android.R
import app.vtcnews.android.databinding.VideoFragmentMotionPlayerBinding
import app.vtcnews.android.model.Article
import app.vtcnews.android.viewmodels.VideoHomeFragViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random


@AndroidEntryPoint
class FragmentChitietVideo : Fragment() {
    lateinit var binding: VideoFragmentMotionPlayerBinding
    lateinit var player: SimpleExoPlayer
    val viewModel: VideoHomeFragViewModel by viewModels()
    private lateinit var navBottom: BottomNavigationView

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

    lateinit var notificationChannel: NotificationChannel
    lateinit var notificationManager: NotificationManager
    lateinit var builder: Notification.Builder
    private val channelId = "12345"
    private val description = "Test Notification"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VideoFragmentMotionPlayerBinding.inflate(layoutInflater, container, false)

        val intent = Intent()
        val penIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
        notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mbuild = NotificationCompat.Builder(requireContext(), "1")
        mbuild.setSmallIcon(R.drawable.ic_play_arrow)
        mbuild.setContentTitle("Player")
        mbuild.setContentText("...")
        mbuild.addAction(R.drawable.exo_icon_previous, "", penIntent)
        notificationManager.notify(1, mbuild.build())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getVideoDetail(requireArguments().getLong("idvideodetail"))
        Log.i("idaCurrent", "" + requireArguments().getLong("idvideodetail"))
        binding.tvTitleVideo.setText(requireArguments().getString("title"))
        binding.txtVideoTitleMini.setText(requireArguments().getString("title"))
        binding.txtVideoTitleMini.isSelected = true

        dataListNextVideoObser()
        dataVideoDetailObser()
        val pageRandom = Random.nextInt(2, 4)
        viewModel.getVideoByCategory(pageRandom, requireArguments().getLong("categoryid"))
        buttonClick()

        navBottom = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottom_nav)
        navBottom.isVisible = false
        view.setFocusableInTouchMode(true)
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_BACK) {
                navBottom.isVisible = true
            }
            false
        })

    }

    fun dataVideoDetailObser() {
        viewModel.videoDetail.observe(viewLifecycleOwner)
        {
            player = SimpleExoPlayer.Builder(requireContext()).build()
            val url = it[0].videoURL
            Log.i("idaUrl", url)
            binding.videoView.setPlayer(player)
            val mediaItem: MediaItem =
                MediaItem.fromUri("https://media.vtc.vn/" + url)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
            player.play()
        }
    }

    fun dataListNextVideoObser() {
        viewModel.listVideoByCategory.observe(viewLifecycleOwner)
        { listVideoByCategory ->
            val adapter = VideoItemAdapter(listVideoByCategory.take(8))

            val layoutManager = GridLayoutManager(context, 2)
            binding.rvVideoNext.adapter = adapter
            binding.rvVideoNext.layoutManager = layoutManager

            adapter.clickListen = { article: Article ->

                val frame_player =
                    requireActivity().findViewById<FrameLayout>(R.id.frame_player_podcast)
                val frame_hoder = requireActivity().findViewById<FrameLayout>(R.id.fragment_holder)
                val params = frame_player.layoutParams
                params.width = FrameLayout.LayoutParams.MATCH_PARENT
                params.height = frame_hoder.height + navBottom.height
                frame_player.layoutParams = params
                player.release()
                requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(0, 0, 0, R.anim.exit_to_right)
                    .add(
                        R.id.frame_player_podcast,
                        newInstance(
                            article.title,
                            article.id.toLong(),
                            article.categoryID
                        )
                    ).addToBackStack(null).commit()
            }
        }
    }

    fun buttonClick() {
        binding.btnPlayPauseMini.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
                binding.btnPlayPauseMini.setImageResource(R.drawable.ic_play_arrow)
            } else {
                player.play()
                binding.btnPlayPauseMini.setImageResource(R.drawable.ic_pause)
            }
        }
        binding.closeMiniPlayer.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }
    }

    override fun onStop() {
        super.onStop()
        navBottom.isVisible = true
        player.release()
    }

    override fun onPause() {
        super.onPause()
        player.release()
    }


}