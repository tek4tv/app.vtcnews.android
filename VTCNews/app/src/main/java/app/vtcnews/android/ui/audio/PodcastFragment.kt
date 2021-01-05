package app.vtcnews.android.ui.audio


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.R
import app.vtcnews.android.databinding.LayoutPodcastBinding
import app.vtcnews.android.databinding.LayoutPodcastBindingImpl
import app.vtcnews.android.repos.AudioRepo
import app.vtcnews.android.viewmodels.AudioHomeFragViewModel
import com.google.android.material.tabs.TabLayoutMediator
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
        val view = inflater.inflate(R.layout.layout_podcast, container, false)




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
                binding.tvEpDescrip.text = "tập"

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
                binding.tvEpDescrip.text = "tập"
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
                binding.tvEpDescrip.text = "ca khúc"
            }
        }
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

    fun setUpObser()
    {
        val adapter = LoadMoreVPAdapter(requireActivity())
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
                        binding.tvTitle.setText(it[0].name)
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