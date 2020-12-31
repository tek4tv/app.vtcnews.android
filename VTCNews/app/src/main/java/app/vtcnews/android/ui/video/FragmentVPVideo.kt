package com.example.vtclive.Video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import app.vtcnews.android.databinding.FragmentVpVideoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentVPVideo : Fragment() {
    lateinit var binding: FragmentVpVideoBinding

    companion object {
        fun newInstance() = FragmentVPVideo()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVpVideoBinding.inflate(inflater, container, false)

        return binding.root
    }
}