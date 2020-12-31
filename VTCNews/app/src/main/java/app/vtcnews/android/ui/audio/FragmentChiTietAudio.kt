package app.vtcnews.android.ui.audio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChitietAudioBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btBack.setOnClickListener(View.OnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        })

        viewModel.getAlbumDetail(requireArguments().getLong("idAlbumDetail"))
        setUpDataObser()

    }
    fun setUpDataObser()
    {
        viewModel.podcastInfo.observe(viewLifecycleOwner)
        {
            Picasso.get().load(it.image182182).into(binding.ivChitiet)
            binding.tvTitleChiTiet.setText(it.name)
            binding.tvTacGia.setText(it.author)
            binding.tvThongtin.setText(it.des)
        }
        viewModel.listAlbumDetail.observe(viewLifecycleOwner)
        {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val listChapterAdapter = ListChapterAdapter(it)
            binding.rvListChapter.adapter = listChapterAdapter
            binding.rvListChapter.layoutManager = layoutManager

            listChapterAdapter.clickListen = { listPodcast: ListPodcast, i: Int ->
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frame_player_podcast,
                        FragmentPlayerAudio.newInstance(listPodcast.fileURL,listPodcast.name,listPodcast.image182182,i)
                    ).commit()

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