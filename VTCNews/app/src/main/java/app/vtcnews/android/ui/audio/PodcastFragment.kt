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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.R
import app.vtcnews.android.databinding.LayoutPodcastBinding


class PodcastFragment : Fragment() {


    //    fun newInstance() = ChildFragment()
    companion object {
        fun newInstance(trangThai: String, id: Long) =
            PodcastFragment().apply {
                arguments = Bundle().apply {
                    putString("trangthai", trangThai)
                    putLong("idchannel", id)
                }
            }
    }

    lateinit var binding: LayoutPodcastBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_podcast, container, false)
        val tvHeader = view.findViewById<TextView>(R.id.tvHeader)
        val icHeader = view.findViewById<ImageView>(R.id.icHeader)
        val btLoadMore = view.findViewById<AppCompatButton>(R.id.btLoadMore)
        val tvEpDes = view.findViewById<TextView>(R.id.tvEpDescrip)

        when (arguments?.getString("trangthai")) {
            "Podcast" -> {
                view.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.podcast
                    )
                )
                tvHeader.setText("Podcast")
                tvHeader.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorTitle
                    )
                )
                icHeader.setImageResource(R.drawable.icpc)
                btLoadMore.setBackgroundResource(R.drawable.custombuttonpodc)
                tvEpDes.setText("tập")

            }
            "Sách nói" -> {
                view.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.sachnoi
                    )
                )
                tvHeader.setText("Sách nói")
                tvHeader.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                icHeader.setImageResource(R.drawable.icbook)
                btLoadMore.setBackgroundResource(R.drawable.custombuttonbook)
                tvEpDes.setText("tập")
            }
            "Âm nhạc" -> {
                view.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.amnhac
                    )
                )
                tvHeader.setText("Âm nhạc")
                tvHeader.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                icHeader.setImageResource(R.drawable.icmusic)
                btLoadMore.setBackgroundResource(R.drawable.custombuttonam)
                tvEpDes.setText("ca khúc")
            }
        }
        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val layoutManager = GridLayoutManager(context, 2)
        val rv_xemthem = view.findViewById<RecyclerView>(R.id.rv_xemthem)
        rv_xemthem.layoutManager = layoutManager


        //click chi tiet audio
//        itemAudioAdapter.clickListen = {
//            activity!!.supportFragmentManager.beginTransaction()
//                .replace(
//                    R.id.frame_main,
//                    FragmentChiTietAudio.newInstance()
//                )
//                .addToBackStack(null).commit()
//        }


        val btLoadMore = view.findViewById<AppCompatButton>(R.id.btLoadMore)

        btLoadMore.setOnClickListener(View.OnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_holder,
                    FragmentLoadMoreAudio.newInstance(
                        arguments?.getString("trangthai")!!,
                        arguments?.getLong("idchannel")!!
                    )
                )
                .addToBackStack("sss").commit()
        })
    }


}