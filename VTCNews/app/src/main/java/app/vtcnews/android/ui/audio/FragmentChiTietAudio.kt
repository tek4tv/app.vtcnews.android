package app.vtcnews.android.ui.audio

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentChitietAudioBinding
import app.vtcnews.android.model.Audio.ListPodcast
import app.vtcnews.android.viewmodels.AudioHomeFragViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentChiTietAudio : Fragment() {
    private val viewModel: AudioHomeFragViewModel by viewModels()
    lateinit var binding: FragmentChitietAudioBinding
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private var curFrag = "home"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = FragmentChitietAudioBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAlbumDetail(requireArguments().getLong("idAlbumDetail"))
        setUpDataObser()
    }

    fun setUpDataObser() {
        viewModel.podcastInfo.observe(viewLifecycleOwner)
        {
            Picasso.get().load(it.image182182).into(binding.ivChitiet)
            binding.tvTitleChiTiet.text = it.name
            binding.tvTacGia.text = it.author
            binding.tvThongtin.text = it.des

        }
        viewModel.listAlbumDetail.observe(viewLifecycleOwner)
        {
            if (it.isNotEmpty()) {
                binding.tvNoChapter.visibility = View.GONE
                val layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                val listChapterAdapter = ListChapterAdapter(it)
                binding.rvListChapter.adapter = listChapterAdapter
                binding.rvListChapter.layoutManager = layoutManager
                listChapterAdapter.clickListen = { listPodcast: ListPodcast, i: Int ->
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame_player_audio,
                            FragmentPlayerAudio.newInstance(
                                listPodcast.fileURL,
                                listPodcast.name, listPodcast.image182182, i
                            ), "player"
                        ).commit()

                }
            } else {
                binding.tvNoChapter.visibility = View.VISIBLE
            }

        }

    }


    companion object {
        fun newInstance(id: Long) = FragmentChiTietAudio().apply {
            arguments = Bundle().apply {
                putLong("idAlbumDetail", id)
            }
        }
    }

}