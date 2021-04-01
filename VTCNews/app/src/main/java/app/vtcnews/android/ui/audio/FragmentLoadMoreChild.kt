package app.vtcnews.android.ui.audio

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentLoadmorereBinding
import app.vtcnews.android.model.Audio.AlbumPaging
import app.vtcnews.android.viewmodels.AudioHomeFragViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentLoadMoreChild : Fragment() {
    val viewModel: AudioHomeFragViewModel by viewModels()
    lateinit var binding: FragmentLoadmorereBinding
    private val albumPagingAdapter = AlbumPagingAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = FragmentLoadmorereBinding.inflate(layoutInflater, container, false)
        setUpLayout()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.id = requireArguments().getLong("id")
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (position) {
                    0 -> 2
                    else -> 1
                }
            }
        }
        binding.rvPcChild.adapter = albumPagingAdapter
        binding.rvPcChild.layoutManager = layoutManager
        albumPagingAdapter.clickListen = { albumPaging: AlbumPaging, _ ->
            requireActivity().supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_holder,
                    FragmentChiTietAudio.newInstance(
                        albumPaging.id,
                        albumPaging.channelID
                    )
                )
                .addToBackStack(null).commit()
        }
        setUpObser()

    }

    private fun setUpObser() {
        viewModel.getAlbumPaging(requireArguments().getLong("id"), 1).observe(viewLifecycleOwner)
        {
            lifecycleScope.launch {
                viewModel.pagingData.collectLatest { pagingda ->
                    albumPagingAdapter.submitData(pagingda)
                }
            }
        }
    }

    private fun setUpLayout() {
        when (arguments?.getString("trangthai")) {
            "Podcast" -> {
                binding.backgroundLoadmorechild.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.podcast
                    )
                )


            }
            "Sách nói" -> {
                binding.backgroundLoadmorechild.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.sachnoi
                    )
                )

            }
            "Âm nhạc" -> {
                binding.backgroundLoadmorechild.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.amnhac
                    )
                )

            }
        }
    }

    companion object {
        fun newInstance(trangThai: String, id: Long) =
            FragmentLoadMoreChild().apply {
                arguments = Bundle().apply {
                    putString("trangthai", trangThai)
                    putLong("id", id)
                }
            }
    }
}