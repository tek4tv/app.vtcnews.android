package app.vtcnews.android.ui.audio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentPlayerAudioBinding


class FragmentPlayerAudio : Fragment() {
    lateinit var binding: FragmentPlayerAudioBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerAudioBinding.inflate(layoutInflater,container,false)





        binding.ibPlay.setOnClickListener(View.OnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            Toast.makeText(activity, "Close", Toast.LENGTH_SHORT).show()
        })


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitleChapter.setText(requireArguments().getString("name"))
        binding.tvTitleChapter.isSelected = true
    }
    companion object
    {
        fun newInstance(url :String,name:String) =
            FragmentPlayerAudio().apply {
                arguments = Bundle().apply {
                    putString("url",url)
                    putString("name",name)

                }
            }
    }
}