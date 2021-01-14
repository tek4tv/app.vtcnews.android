package com.example.vtclive.Video

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pbLoading.isVisible = true
        viewModel.getVideoHome()
        dataObserVideo()
        binding.pbLoading.isVisible = false
    }

    fun dataObserVideo() {
        viewModel.listVideoHome.observe(viewLifecycleOwner)
        {
            val adapter = VideoHomeAdapter(it)
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.rvVideoHome.adapter = adapter
            binding.rvVideoHome.layoutManager = layoutManager
            adapter.clickListen = { video ->
                var frame_player =
                    requireActivity().findViewById<FrameLayout>(R.id.frame_player_podcast)
                var params = frame_player.layoutParams
                params.width = FrameLayout.LayoutParams.MATCH_PARENT
                params.height = FrameLayout.LayoutParams.MATCH_PARENT
                frame_player.layoutParams = params
                if(requireActivity().supportFragmentManager.findFragmentByTag("fragVideo") != null)
                {
                    requireActivity().supportFragmentManager.popBackStack()
                }
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_right,
                        0,
                        R.anim.exit_to_right
                    )
                    .replace(
                        R.id.frame_player_podcast,
                        FragmentChitietVideo.newInstance(video.title, video.id, video.categoryID),"fragVideo"
                    ).addToBackStack(null).commit()
            }
        }
    }

    companion object {
        fun newInstance() = FragmentVideoPage()
    }
}