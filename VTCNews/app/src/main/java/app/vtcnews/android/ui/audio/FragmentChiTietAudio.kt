package app.vtcnews.android.ui.audio

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentChitietAudioBinding
import app.vtcnews.android.model.Audio.AlbumPaging
import app.vtcnews.android.model.Audio.ListPodcast
import app.vtcnews.android.viewmodels.AudioHomeFragViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentChiTietAudio : Fragment() {
    private val viewModel: AudioHomeFragViewModel by viewModels()
    lateinit var binding: FragmentChitietAudioBinding
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
        setUpListSameCateGory()
        buttonClick()
    }

    private fun buttonClick() {
        binding.btHeart.setOnClickListener {
            val toast = Toast.makeText(context, R.string.noOption, Toast.LENGTH_SHORT)
            toast.show()
            lifecycleScope.launch()
            {
                delay(200)
                toast.cancel()
            }
        }
        binding.btCommentAudio.setOnClickListener {
            val toast = Toast.makeText(context, R.string.noOption, Toast.LENGTH_SHORT)
            toast.show()
            lifecycleScope.launch()
            {
                delay(200)
                toast.cancel()
            }
        }
        binding.btShareAudio.setOnClickListener {
            val toast = Toast.makeText(context, R.string.noOption, Toast.LENGTH_SHORT)
            toast.show()
            lifecycleScope.launch()
            {
                delay(200)
                toast.cancel()
            }
        }

    }

    private fun setUpDataObser() {
        viewModel.podcastInfo.observe(viewLifecycleOwner)
        { podcastInfo ->
            //item lager
            Picasso.get().load(podcastInfo.image182182).fit().noFade().into(binding.ivChitiet)
            binding.tvTitleChiTiet.text = podcastInfo.name
            binding.tvTacGia.text = podcastInfo.author
            binding.tvThongtin.text = podcastInfo.des
            //list chapter
            viewModel.listAlbumDetail.observe(viewLifecycleOwner)
            {
                if (it.isNotEmpty()) {
                    binding.tvNoChapter.visibility = View.GONE
                    val layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    val listChapterAdapter = podcastInfo.author?.let { it1 ->
                        ListChapterAdapter(
                            it,
                            it1
                        )
                    }
                    binding.rvListChapter.adapter = listChapterAdapter
                    binding.rvListChapter.layoutManager = layoutManager
                    listChapterAdapter?.clickListen = { listPodcast: ListPodcast, i: Int ->
                        requireActivity().supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                R.anim.enter_from_right,
                                R.anim.exit_to_right,
                                0,
                                R.anim.exit_to_right
                            )
                            .replace(
                                R.id.frame_player_audio,
                                FragmentPlayerAudio.newInstance(
                                    listPodcast.fileURL,
                                    listPodcast.name,
                                    listPodcast.image182182,
                                    i,
                                    podcastInfo.author!!
                                ), "player"
                            ).commit()

                    }
                } else {
                    binding.tvNoChapter.visibility = View.VISIBLE
                }

            }
        }

    }

    private fun setUpListSameCateGory() {
        viewModel.getAlbumPaging(requireArguments().getLong("idchannel"), 1)
            .observe(viewLifecycleOwner)
            {
                if (it.size > 5) {
                    val layoutManager = GridLayoutManager(context, 2)
                    val adapter = ItemAudioNoHeaderAdapter(it.drop(1).take(6))
                    binding.rvSameCategory.layoutManager = layoutManager
                    binding.rvSameCategory.adapter = adapter
                    binding.rvSameCategory.setHasFixedSize(true)
                    adapter.clickListen = { albumPaging: AlbumPaging, i: Int ->
                        openRelaAudio(albumPaging.id)
                    }
                } else {
                    viewModel.getAlbumPaging(requireArguments().getLong("idchannel"), 1)
                        .observe(viewLifecycleOwner)
                        { list1 ->
                            viewModel.getAlbumPaging(29, 1).observe(viewLifecycleOwner)
                            { list2 ->
                                list2.forEach()
                                { AlbumPaging ->
                                    list1.add(AlbumPaging)
                                }
                                val layoutManager = GridLayoutManager(context, 2)
                                val adapter = ItemAudioNoHeaderAdapter(list1.drop(1).take(6))
                                binding.rvSameCategory.layoutManager = layoutManager
                                binding.rvSameCategory.adapter = adapter
                                binding.rvSameCategory.setHasFixedSize(true)
                                adapter.clickListen = { albumPaging: AlbumPaging, i: Int ->
                                    openRelaAudio(albumPaging.id)
                                }
                            }

                        }


                }
            }
    }

    fun openRelaAudio(id: Long) {
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
            .add(
                R.id.fragment_holder,
                newInstance(
                    id,
                    requireArguments().getLong("idchannel")
                )
            )
            .addToBackStack(null).commit()
    }


    companion object {
        fun newInstance(id: Long, channelId: Long) = FragmentChiTietAudio().apply {
            arguments = Bundle().apply {
                putLong("idAlbumDetail", id)
                putLong("idchannel", channelId)
            }
        }
    }

}