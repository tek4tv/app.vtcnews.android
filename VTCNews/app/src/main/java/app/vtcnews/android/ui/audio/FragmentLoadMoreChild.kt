package app.vtcnews.android.ui.audio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.vtcnews.android.R

class FragmentLoadMoreChild : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_loadmorere, container, false)
        when (arguments?.getString("trangthai")) {
            "podcast" -> {
                view.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.podcast
                ))


            }
            "sachnoi" -> {
                view.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.sachnoi
                ))

            }
            "amnhac" -> {
                view.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.amnhac
                ))

            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance(trangThai: String) =
            FragmentLoadMoreChild().apply {
                arguments = Bundle().apply {
                    putString("trangthai", trangThai)
                }
            }
    }
}