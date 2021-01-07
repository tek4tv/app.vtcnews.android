package app.vtcnews.android.ui.audio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import app.vtcnews.android.databinding.ActivityAudioHomeBinding
import app.vtcnews.android.model.Audio.AllPodCast
import app.vtcnews.android.viewmodels.AudioHomeFragViewModel
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
        viewModel.listAllPodCast.observe(viewLifecycleOwner)
        {
            it.forEach {AllPodCast ->
                adapter.addFrag(PodcastFragment.newInstance(AllPodCast.name,AllPodCast.id))
            }
        }
    }
}