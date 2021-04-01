package app.vtcnews.android.ui.audio

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import app.vtcnews.android.R
import app.vtcnews.android.databinding.AudioWithouttabBinding
import app.vtcnews.android.model.Audio.AlbumPaging
import app.vtcnews.android.viewmodels.AudioHomeFragViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AudioWithoutTab : Fragment() {
    lateinit var binding: AudioWithouttabBinding
    val viewModel: AudioHomeFragViewModel by activityViewModels()

    companion object {
        fun newInstance() = AudioWithoutTab()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AudioWithouttabBinding.inflate(layoutInflater, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPD()
        setupMusic()
        setupBook()


        binding.refreshlayoutAudio.setOnRefreshListener {
            binding.refreshlayoutAudio.isRefreshing = false
            requireActivity().supportFragmentManager.beginTransaction().setCustomAnimations(0, 0)
                .replace(R.id.fragment_holder, newInstance())
                .commit()
        }
    }

    private fun setPD() {
        //pc
        viewModel.getChannelPodcast(5)
        binding.layoutBook.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.podcast
            )
        )
        binding.tvHeaderbook.text = "Podcast"
        binding.tvHeaderbook.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.black
            )
        )
        binding.icHeaderbook.setImageResource(R.drawable.icpc)
        binding.btLoadMoreBook.setBackgroundResource(R.drawable.custombuttonpodc)
//        viewModel.getChannelPodcast(AllPodCast.id)
        viewModel.getAlbumPaging(5, 1).observe(viewLifecycleOwner)
        {
            val layoutManager = GridLayoutManager(context, 2)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (position) {
                        0 -> 2
                        else -> 1
                    }
                }
            }
            val adapter = ItemAudioAdapter(it.take(5))
            if (it.isNotEmpty()) {

                binding.rvXemthemBook.layoutManager = layoutManager
                binding.rvXemthemBook.adapter = adapter
                adapter.clickListen = { albumPaging: AlbumPaging, i: Int ->
                    checkAudio(albumPaging.id, albumPaging.channelID)
                }
            }
        }
        binding.btLoadMoreBook.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .add(
                    R.id.fragment_holder,
                    FragmentLoadMoreAudio.newInstance(
                        "Podcast",
                        5
                    )
                )
                .addToBackStack(null).commit()
        }

    }

    fun setupMusic() {
        viewModel.getChannelPodcast(2)
        binding.layoutMusic.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.amnhac
            )
        )
        binding.tvHeaderMusic.text = "Music"
        binding.tvHeaderMusic.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.icHeaderMusic.setImageResource(R.drawable.icmusic)
        binding.btLoadMoreMusic.setBackgroundResource(R.drawable.custombuttonam)
        viewModel.getAlbumPaging(14, 1).observe(viewLifecycleOwner)
        {
            val layoutManagerMusic = GridLayoutManager(context, 2)
            layoutManagerMusic.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (position) {
                        0 -> 2
                        else -> 1
                    }
                }
            }
            val adapterMusic = ItemAudioAdapter(it.take(5))
            if (it.isNotEmpty()) {
                binding.rvXemthemMusic.layoutManager = layoutManagerMusic
                binding.rvXemthemMusic.adapter = adapterMusic
                adapterMusic.clickListen = { albumPaging: AlbumPaging, i: Int ->
                    checkAudio(albumPaging.id, albumPaging.channelID)
                }
            }
        }
        binding.btLoadMoreMusic.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .add(
                    R.id.fragment_holder,
                    FragmentLoadMoreAudio.newInstance(
                        "Âm nhạc",
                        2
                    )
                )
                .addToBackStack(null).commit()
        }

    }

    fun setupBook() {
        viewModel.getChannelPodcast(2)
        binding.layoutPd.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.sachnoi
            )
        )
        binding.tvHeaderPd.text = "Sách nói"
        binding.tvHeaderPd.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.icHeaderPd.setImageResource(R.drawable.icbook)
        binding.btLoadMorePd.setBackgroundResource(R.drawable.custombuttonbook)
        viewModel.getAlbumPaging(3, 1).observe(viewLifecycleOwner)
        {
            val layoutManagerPd = GridLayoutManager(context, 2)
            layoutManagerPd.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    when (position) {
                        0 -> return 2
                        else -> return 1
                    }
                }
            }
            val adapterPd = ItemAudioAdapter(it.take(5))
            if (it.isNotEmpty()) {
                binding.rvXemthemPd.layoutManager = layoutManagerPd
                binding.rvXemthemPd.adapter = adapterPd
                adapterPd.clickListen = { albumPaging: AlbumPaging, i: Int ->
                    checkAudio(albumPaging.id, albumPaging.channelID)
                }
            }

        }
        binding.btLoadMorePd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .add(
                    R.id.fragment_holder,
                    FragmentLoadMoreAudio.newInstance(
                        "Sách nói",
                        1
                    )
                )
                .addToBackStack(null).commit()
        }

    }

    private fun checkAudio(id: Long, channelID: Long) {
        viewModel.checkAlbumDetail(id).observe(viewLifecycleOwner)
        { isSucces ->
            if (isSucces) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right
                    )
                    .replace(
                        R.id.fragment_holder,
                        FragmentChiTietAudio.newInstance(
                            id,
                            channelID
                        )
                    )
                    .addToBackStack("audio").commit()
            } else {
                val toast = Toast.makeText(context, R.string.audioerr, Toast.LENGTH_SHORT)
                toast.show()
                lifecycleScope.launch()
                {
                    delay(200)
                    toast.cancel()
                }
            }


        }
    }
}
