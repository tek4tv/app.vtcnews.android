package app.vtcnews.android.ui.audio

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import app.vtcnews.android.R
import app.vtcnews.android.databinding.ActivityAudioHomeBinding
import app.vtcnews.android.model.Audio.AllPodCast
import app.vtcnews.android.viewmodels.AudioHomeFragViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AudioHomeFragment : Fragment() {
    lateinit var binding: ActivityAudioHomeBinding
    lateinit var adapter : AudioVPHomeAdapter
    val viewModel: AudioHomeFragViewModel by viewModels()
    companion object
    {
        fun newInstance() = AudioHomeFragment()
    }

    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityAudioHomeBinding.inflate(layoutInflater, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AudioVPHomeAdapter(requireActivity())
        binding.vpPager.adapter = adapter
        viewModel.getListAllPC()
        setUpob()

    }

    fun setUpob()
    {
        val listHeaderAudio = listOf("Sách nói","Âm nhạc","Podcast")
        viewModel.listAllPodCast.observe(viewLifecycleOwner)
        {
            if(it.isNotEmpty()) {
                binding.tvNodata.isVisible = false
                it.forEach { AllPodCast ->
                    adapter.addFrag(PodcastFragment.newInstance(AllPodCast.name, AllPodCast.id))
                    TabLayoutMediator(binding.tabAudioHome, binding.vpPager) { tab, position ->
                        tab.text = listHeaderAudio[position]
                    }.attach()
                }
            }
            else
            {
                binding.tvNodata.isVisible = true
            }
        }
        viewModel.error.observe(viewLifecycleOwner)
        {
            Toast.makeText(requireContext(), resources.getString(R.string.nointernet), Toast.LENGTH_SHORT).show()
        }
    }
}
//        binding.refreshlayoutAudio.setOnRefreshListener{
//            binding.refreshlayoutAudio.isRefreshing = false
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_holder, AudioHomeFragment.newInstance())
//                .commit()
//        }