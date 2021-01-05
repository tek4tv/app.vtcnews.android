package app.vtcnews.android.ui.audio

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentPlayerAudioBinding
import app.vtcnews.android.model.Audio.ListPodcast
import app.vtcnews.android.repos.AudioRepo
import app.vtcnews.android.viewmodels.AudioHomeFragViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentPlayerAudio : Fragment() {
    private val viewModel: AudioHomeFragViewModel by viewModels()
    lateinit var binding: FragmentPlayerAudioBinding
    var currntPos: Int = 0

    @Inject
    lateinit var audioRepo: AudioRepo
    lateinit var mediaPlayer: MediaPlayer
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerAudioBinding.inflate(layoutInflater, container, false)

        binding.ibPlay.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitleChapter.setText(requireArguments().getString("name"))
        binding.tvTitleChapter.isSelected = true
        Picasso.get().load(requireArguments().getString("urlIMG")).into(binding.ivChapter)
        mediaPlayer = MediaPlayer.create(
            requireActivity(),
            Uri.parse("https://media.vtc.vn/" + requireArguments().getString("urlMp3"))
        )
        mediaPlayer.start()
        buttonClick()

    }

    fun buttonClick() {
        binding.apply {
            ibPlay.setOnClickListener(View.OnClickListener {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    ibPlay.setImageResource(R.drawable.ic_play_arrow)

                } else {
                    mediaPlayer.start()
                    ibPlay.setImageResource(R.drawable.ic_pause)
                }
            })
            val list = audioRepo.listAlbumDetail
            currntPos = requireArguments().getInt("curent")
            ibNext.setOnClickListener(View.OnClickListener {
                mediaPlayer.stop()
                if (currntPos >= list.size - 1) {
                    currntPos = list.size - 1
                } else {
                    currntPos++
                }

                mediaPlayer = MediaPlayer.create(
                    requireActivity(),
                    Uri.parse("https://media.vtc.vn/" + list[currntPos].fileURL)
                )
                tvTitleChapter.text = list[currntPos].name
                mediaPlayer.start()

            })
            ibPre.setOnClickListener(View.OnClickListener {
                mediaPlayer.stop()
                if (currntPos <= 0) {
                    currntPos = 0
                } else {
                    currntPos--
                }
                mediaPlayer = MediaPlayer.create(
                    requireActivity(),
                    Uri.parse(
                        "https://media.vtc.vn/" + list[currntPos].fileURL
                    )
                )
                tvTitleChapter.text = list[currntPos].name
                mediaPlayer.start()
            })


        }
    }

    companion object {
        fun newInstance(urlmp3: String, name: String, urlimg: String, position: Int) =
            FragmentPlayerAudio().apply {
                arguments = Bundle().apply {
                    putString("urlMp3", urlmp3)
                    putString("name", name)
                    putString("urlIMG", urlimg)
                    putString("urlIMG", urlimg)
                    putInt("curent", position)
                }
            }

        fun newInstance() = FragmentPlayerAudio()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }
}