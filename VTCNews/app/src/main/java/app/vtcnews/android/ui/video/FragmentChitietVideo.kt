package app.vtcnews.android.ui.video

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentChitietVideo : Fragment() {
    lateinit var binding: VideoFragmentMotionPlayerBinding
    lateinit var player: SimpleExoPlayer
    val viewModel: VideoHomeFragViewModel by viewModels()

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
        viewModel.getVideoByCategory(1, requireArguments().getLong("categoryid"))
        buttonClick()

        //back press event
        view.setFocusableInTouchMode(true)
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    requireActivity().supportFragmentManager.beginTransaction().remove(this)
                        .commit()
                }
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
                params.height = frame_hoder.height
                frame_player.layoutParams = params

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frame_player_podcast,
                        FragmentChitietVideo.newInstance(
                            article.title,
                            article.id.toLong(),
                            article.categoryID
                        )
                    ).addToBackStack(null).commit()
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    fun buttonClick() {
        binding.btnPlayPauseMini.setOnClickListener(View.OnClickListener {
            if (player.isPlaying) {
                player.pause()
                binding.btnPlayPauseMini.setImageResource(R.drawable.ic_play_arrow)
            } else {
                player.play()
                binding.btnPlayPauseMini.setImageResource(R.drawable.ic_pause)
            }
        })
        binding.closeMiniPlayer.setOnClickListener(View.OnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    override fun onPause() {
        super.onPause()
        player = SimpleExoPlayer.Builder(requireContext()).build()
        player.stop()
    }


}