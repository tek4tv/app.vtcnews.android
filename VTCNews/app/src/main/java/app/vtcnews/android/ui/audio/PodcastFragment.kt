package app.vtcnews.android.ui.audio


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import app.vtcnews.android.R
import app.vtcnews.android.databinding.LayoutPodcastBinding
import app.vtcnews.android.repos.AudioRepo
import app.vtcnews.android.viewmodels.AudioHomeFragViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PodcastFragment : Fragment() {
    companion object {
        fun newInstance(trangThai: String, id: Long) =
            PodcastFragment().apply {
                arguments = Bundle().apply {
                    putString("trangthai", trangThai)
                    putLong("idchannel", id)
                }
            }
    }
    private val viewModel: AudioHomeFragViewModel by viewModels()
    lateinit var binding: LayoutPodcastBinding
    @Inject
    lateinit var audioRepo : AudioRepo
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutPodcastBinding.inflate(layoutInflater,container,false)
        setUpLayoutByCategory()
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getChannelPodcast(requireArguments().getLong("idchannel"))

        //setUpob()
        setUpObser()

        binding.btLoadMore.setOnClickListener(View.OnClickListener {
            parentFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_holder,
                    FragmentLoadMoreAudio.newInstance(
                        arguments?.getString("trangthai")!!,
                        arguments?.getLong("idchannel")!!
                    )
                )
                .addToBackStack(null).commit()
        })


    }

    override fun onResume() {
        super.onResume()
//        if(requireActivity().supportFragmentManager.findFragmentByTag("player") != null)
//        {
//            val btLoadmore = requireActivity().findViewById<AppCompatButton>(R.id.btLoadMore)
//            val param = btLoadmore.layoutParams as ViewGroup.MarginLayoutParams
//            param.setMargins(0,0,0,200)
//            btLoadmore.layoutParams = param
//        }
    }
    fun setUpLayoutByCategory()
    {
        when (arguments?.getString("trangthai")) {
            "Podcast" -> {
                binding.backgroundPC.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.podcast
                    )
                )
                binding.tvHeader.text = "Podcast"
                binding.tvHeader.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorTitle
                    )
                )
                binding.icHeader.setImageResource(R.drawable.icpc)
                binding.btLoadMore.setBackgroundResource(R.drawable.custombuttonpodc)

            }
            "Sách nói" -> {
                binding.backgroundPC.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.sachnoi
                    )
                )
                binding.tvHeader.text = "Sách nói"
                binding.tvHeader.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                binding.icHeader.setImageResource(R.drawable.icbook)
                binding.btLoadMore.setBackgroundResource(R.drawable.custombuttonbook)
            }
            "Âm nhạc" -> {
                binding.backgroundPC.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.amnhac
                    )
                )
                binding.tvHeader.text = "Âm nhạc"
                binding.tvHeader.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                binding.icHeader.setImageResource(R.drawable.icmusic)
                binding.btLoadMore.setBackgroundResource(R.drawable.custombuttonam)
            }
        }
    }


    fun setUpObser()
    {
        viewModel.listChannelPodCast.observe(viewLifecycleOwner)
        {listChannerPodcast ->
            listChannerPodcast.forEach{channelPodcast ->
                viewModel.getAlbumPaging(channelPodcast.id)
                viewModel.listAlbumPaging.observe(viewLifecycleOwner)
                {
                    val layoutManager = GridLayoutManager(context, 2)
                    val adapter = ItemAudioAdapter(it.drop(1).take(4))
                    if (it.isNotEmpty()) {
                        Picasso.get().load(it[0].image360360).into(binding.ivHeader)
                        binding.tvTitle.text = it[0].name
                        binding.itemHeaderPd.setOnClickListener{view ->
                            requireActivity().supportFragmentManager.beginTransaction()
                                .add(
                                    R.id.fragment_holder,
                                    FragmentChiTietAudio.newInstance(it[0].id)
                                )
                                .addToBackStack(null).commit()
                        }

                        binding.rvXemthem.layoutManager = layoutManager
                        binding.rvXemthem.adapter = adapter
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

        }

    }



}