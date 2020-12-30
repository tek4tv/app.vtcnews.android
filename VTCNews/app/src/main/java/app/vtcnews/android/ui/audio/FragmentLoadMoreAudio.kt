package app.vtcnews.android.ui.audio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import app.vtcnews.android.R
import app.vtcnews.android.databinding.ActivityAudioHomeBinding
import app.vtcnews.android.databinding.ActivityLoadmoreBinding
import app.vtcnews.android.viewmodels.AudioHomeFragViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentLoadMoreAudio :Fragment() {
    lateinit var binding: ActivityLoadmoreBinding
    val viewModel: AudioHomeFragViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityLoadmoreBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getChannelPodcast(requireArguments().getLong("idchannel"))

        setUpob()

    }
    fun setUpob()
    {
        val adapter = LoadMoreVPAdapter(requireActivity())
        viewModel.listChannelPodCast.observe(viewLifecycleOwner)
        {
            it.drop(1).forEach{
                viewModel.getAlbumPaging(it.id)
                adapter.addFrag(
                    FragmentLoadMoreChild.newInstance(
                        arguments?.getString("trangthai")!!,it.id
                    )
                )
                TabLayoutMediator( binding.tabLoadMore ,binding.vpLoadMore ) { tab, position ->
                    tab.text = it.name
                }.attach()
            }

        }
        binding.vpLoadMore.adapter = adapter
    }

    companion object {
        fun newInstance(trangThai: String,id:Long) =
            FragmentLoadMoreAudio().apply {
                arguments = Bundle().apply {
                    putString("trangthai", trangThai)
                    putLong("idchannel", id)
                }
            }
    }

}