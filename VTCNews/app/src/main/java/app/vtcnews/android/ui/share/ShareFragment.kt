package app.vtcnews.android.ui.share

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.vtcnews.android.databinding.ShareFragmentBinding

class ShareFragment : Fragment() {
    lateinit var binding: ShareFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ShareFragmentBinding.inflate(layoutInflater, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        return binding.root
    }

    companion object {
        fun newInstance() = ShareFragment()
    }
}