package app.vtcnews.android.ui.audio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentLoadmorereBinding
import app.vtcnews.android.viewmodels.AudioHomeFragViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentLoadMoreChild : Fragment() {
    val viewModel: AudioHomeFragViewModel by viewModels()
    lateinit var binding: FragmentLoadmorereBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentLoadmorereBinding.inflate(layoutInflater,container,false)
        when (arguments?.getString("trangthai")) {
            "Podcast" -> {
                binding.backgroundLoadmorechild.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.podcast
                ))


            }
            "Sách nói" -> {
                binding.backgroundLoadmorechild.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.sachnoi
                ))

            }
            "Âm nhạc" -> {
                binding.backgroundLoadmorechild.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.amnhac
                ))

            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAlbumPaging(requireArguments().getLong("id"))
        setUpObser()

    }
    fun setUpObser()
    {   val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL,false)
        viewModel.listAlbumPaging.observe(viewLifecycleOwner)
        {
            val adapter = ItemAudioAdapter(it.drop(1))
            if (it.isNotEmpty()) {
                binding.tvTitle.text = it[0].name
                Picasso.get().load(it[0].image360360).into(binding.ivHeader)
                binding.itemHeaderPdChild.setOnClickListener{view ->
                    requireActivity().supportFragmentManager.beginTransaction()
                        .add(
                            R.id.fragment_holder,
                            FragmentChiTietAudio.newInstance(it[0].id)
                        )
                        .addToBackStack(null).commit()
                }
                binding.rvPcChild.adapter = adapter
                binding.rvPcChild.layoutManager = layoutManager
                adapter.clickListen = {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .add(
                            R.id.fragment_holder,
                            FragmentChiTietAudio.newInstance(it.id)
                        )
                        .addToBackStack(null).commit()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if(requireActivity().supportFragmentManager.findFragmentByTag("player") != null)
        {
            val layout = requireActivity().findViewById<RecyclerView>(R.id.rvPcChild)
            val param = layout.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0,0,0,200)
            layout.layoutParams = param
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(requireActivity().supportFragmentManager.findFragmentByTag("player") != null)
        {
            val layout = requireActivity().findViewById<RecyclerView>(R.id.rvPcChild)
            val param = layout.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0,0,0,0)
            layout.layoutParams = param
        }

    }

    companion object {
        fun newInstance(trangThai: String,id:Long) =
            FragmentLoadMoreChild().apply {
                arguments = Bundle().apply {
                    putString("trangthai", trangThai)
                    putLong("id", id)
                }
            }
    }
}