package app.vtcnews.android.ui.audio

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import app.vtcnews.android.databinding.ActivityLoadmoreBinding
import app.vtcnews.android.viewmodels.AudioHomeFragViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentLoadMoreAudio : Fragment() {
    lateinit var binding: ActivityLoadmoreBinding
    val viewModel: AudioHomeFragViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityLoadmoreBinding.inflate(layoutInflater, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getChannelPodcast(requireArguments().getLong("idchannel"))

        setUpob()

    }

    fun setUpob() {
        val adapter = LoadMoreVPAdapter(requireActivity())
        viewModel.listChannelPodCast.observe(viewLifecycleOwner)
        {
            it.forEach { channel ->
                viewModel.getAlbumPaging(channel.id, 1)
                adapter.addFrag(
                    FragmentLoadMoreChild.newInstance(
                        arguments?.getString("trangthai")!!, channel.id
                    )
                )
                TabLayoutMediator(binding.tabLoadMore, binding.vpLoadMore) { tab, position ->
                    tab.text = it[position].name
                }.attach()
            }

        }
        binding.vpLoadMore.adapter = adapter
    }

    companion object {
        fun newInstance(trangThai: String, id: Long) =
            FragmentLoadMoreAudio().apply {
                arguments = Bundle().apply {
                    putString("trangthai", trangThai)
                    putLong("idchannel", id)
                }
            }
    }


}