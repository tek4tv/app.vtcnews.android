package app.vtcnews.android.ui.audio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import app.vtcnews.android.MainActivity
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChitietAudioBinding.inflate(layoutInflater, container, false)
        (requireActivity() as MainActivity).supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btBack.setOnClickListener {
            binding.layoutMenu.isVisible = false
            (requireActivity() as MainActivity).supportActionBar?.show()
            requireActivity().supportFragmentManager.popBackStack()
        }
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
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val listChapterAdapter = ListChapterAdapter(it)
            binding.rvListChapter.adapter = listChapterAdapter
            binding.rvListChapter.layoutManager = layoutManager
            listChapterAdapter.clickListen = { listPodcast: ListPodcast, i: Int ->

                val frame_player =
                    requireActivity().findViewById<FrameLayout>(R.id.frame_player_podcast)
                val params = frame_player.layoutParams
                params.width = FrameLayout.LayoutParams.MATCH_PARENT
                params.height = FrameLayout.LayoutParams.WRAP_CONTENT
                frame_player.layoutParams = params

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frame_player_podcast,
                        FragmentPlayerAudio.newInstance(
                            listPodcast.fileURL,
                            listPodcast.name, listPodcast.image182182, i
                        ), "player").commit()

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

    override fun onDestroyView() {
        super.onDestroyView()
        binding.layoutMenu.isVisible = false
        (requireActivity() as MainActivity).supportActionBar?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).supportActionBar?.show()
    }
}