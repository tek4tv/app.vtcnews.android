package com.example.vtclive.Video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import app.vtcnews.android.R
import app.vtcnews.android.databinding.LayoutVideoPageBinding
import app.vtcnews.android.ui.audio.LoadMoreVPAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentVideoPage : Fragment() {
    lateinit var binding : LayoutVideoPageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutVideoPageBinding.inflate(layoutInflater,container,false)

        val listTitle: MutableList<String> = ArrayList()
        val adapter = LoadMoreVPAdapter(requireActivity())

        listTitle.add("Chính trị xã hội")
        listTitle.add("Kinh tế")
        listTitle.add("Quốc tế")
        listTitle.add("Giải trí")
        adapter.addFrag(FragmentVPVideo.newInstance())
        adapter.addFrag(FragmentVPVideo.newInstance())
        adapter.addFrag(FragmentVPVideo.newInstance())
        adapter.addFrag(FragmentVPVideo.newInstance())
        binding.vpVideo.adapter = adapter
        TabLayoutMediator( binding.tabVideo,binding.vpVideo ) { tab, position ->
            tab.text = listTitle[position]
        }.attach()


        return binding.root
    }
    companion object {
        fun newInstance() = FragmentVideoPage()
    }
}