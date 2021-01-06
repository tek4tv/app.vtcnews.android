package com.example.vtclive.Video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import app.vtcnews.android.R
import app.vtcnews.android.databinding.LayoutVideoPageBinding
import app.vtcnews.android.ui.video.FragmentChitietVideo
import app.vtcnews.android.ui.video.VideoHomeAdapter
import app.vtcnews.android.viewmodels.VideoHomeFragViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentVideoPage : Fragment() {
    lateinit var binding: LayoutVideoPageBinding
    val viewModel: VideoHomeFragViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutVideoPageBinding.inflate(layoutInflater, container, false)

//        val listTitle: MutableList<String> = ArrayList()
//        val adapter = LoadMoreVPAdapter(requireActivity())
//
//        listTitle.add("Chính trị xã hội")
//        listTitle.add("Kinh tế")
//        listTitle.add("Quốc tế")
//        listTitle.add("Giải trí")
//        adapter.addFrag(FragmentVPVideo.newInstance())
//        adapter.addFrag(FragmentVPVideo.newInstance())
//        adapter.addFrag(FragmentVPVideo.newInstance())
//        adapter.addFrag(FragmentVPVideo.newInstance())
//        binding.vpVideo.adapter = adapter
//        TabLayoutMediator( binding.tabVideo,binding.vpVideo ) { tab, position ->
//            tab.text = listTitle[position]
//        }.attach()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getVideoHome()
        dataObserVideo()

    }

    fun dataObserVideo() {
        viewModel.listVideoHome.observe(viewLifecycleOwner)
        {
            val adapter = VideoHomeAdapter(it)
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.rvVideoHome.adapter = adapter
            binding.rvVideoHome.layoutManager = layoutManager

            adapter.clickListen = {
                val frame_player =
                    requireActivity().findViewById<FrameLayout>(R.id.frame_player_podcast)
                val frame_hoder = requireActivity().findViewById<FrameLayout>(R.id.fragment_holder)
                val params = frame_player.layoutParams
                params.width = FrameLayout.LayoutParams.MATCH_PARENT
                params.height = frame_hoder.height
                frame_player.layoutParams = params
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frame_player_podcast,
                        FragmentChitietVideo.newInstance(it.title, it.id, it.categoryID)
                    ).addToBackStack(null).commit()
               
            }
        }
    }

    companion object {
        fun newInstance() = FragmentVideoPage()
    }
}