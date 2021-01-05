package com.example.vtclive.Video

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.R
import app.vtcnews.android.databinding.LayoutVideoPageBinding
import app.vtcnews.android.ui.audio.FragmentChiTietAudio
import app.vtcnews.android.ui.audio.LoadMoreVPAdapter
import app.vtcnews.android.ui.video.FragmentChitietVideo
import app.vtcnews.android.ui.video.VideoHomeAdapter
import app.vtcnews.android.viewmodels.VideoHomeFragViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentVideoPage : Fragment() {
    lateinit var binding : LayoutVideoPageBinding
    val viewModel: VideoHomeFragViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutVideoPageBinding.inflate(layoutInflater,container,false)

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
    fun dataObserVideo()
    {
        viewModel.listVideoHome.observe(viewLifecycleOwner)
        {
            val adapter = VideoHomeAdapter(it)
            val layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
           binding.rvVideoHome.adapter = adapter
            binding.rvVideoHome.layoutManager = layoutManager
            adapter.clickListen = {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_holder,
                        FragmentChitietVideo.newInstance(it.title,it.id,it.categoryID)
                    )
                    .addToBackStack(null).commit()
            }
        }
    }

    companion object {
        fun newInstance() = FragmentVideoPage()
    }
}