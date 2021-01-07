package app.vtcnews.android.ui.audio

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentChitietAudioBinding
import app.vtcnews.android.model.Audio.ListPodcast
import app.vtcnews.android.ui.video.VideoNotification
import app.vtcnews.android.viewmodels.AudioHomeFragViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

@AndroidEntryPoint
class FragmentChiTietAudio : Fragment() {
    private val viewModel: AudioHomeFragViewModel by viewModels()
    lateinit var binding: FragmentChitietAudioBinding
    lateinit var notificationManager:NotificationManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChitietAudioBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btBack.setOnClickListener(View.OnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        })

        viewModel.getAlbumDetail(requireArguments().getLong("idAlbumDetail"))
        setUpDataObser()

    }

    fun setUpDataObser() {
        viewModel.podcastInfo.observe(viewLifecycleOwner)
        {
            Picasso.get().load(it.image182182).into(binding.ivChitiet)
            binding.tvTitleChiTiet.setText(it.name)
            binding.tvTacGia.setText(it.author)
            binding.tvThongtin.setText(it.des)
        }
        viewModel.listAlbumDetail.observe(viewLifecycleOwner)
        {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val listChapterAdapter = ListChapterAdapter(it)
            binding.rvListChapter.adapter = listChapterAdapter
            binding.rvListChapter.layoutManager = layoutManager

            listChapterAdapter.clickListen = { listPodcast: ListPodcast, i: Int ->

                thread {
                    createChannel()
                    val bitmap = Picasso.get().load(listPodcast.image182182).get()
                    val intent = Intent()
                    val penIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
                    notificationManager =
                        requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    val mbuild = NotificationCompat.Builder(requireContext(), "1")
                    mbuild.setSmallIcon(R.drawable.ic_play_arrow)
                    mbuild.setContentTitle(listPodcast.name)
                    mbuild.setContentText("...")
                    mbuild.setLargeIcon(bitmap)
                    mbuild.addAction(R.drawable.exo_icon_previous, "Player", penIntent)
                    notificationManager.notify(1, mbuild.build())
                }


                val frame_player =
                    requireActivity().findViewById<FrameLayout>(R.id.frame_player_podcast)
                val frame_hoder = requireActivity().findViewById<FrameLayout>(R.id.fragment_holder)
                val params = frame_player.layoutParams
                params.width = FrameLayout.LayoutParams.MATCH_PARENT
                params.height = FrameLayout.LayoutParams.WRAP_CONTENT
                frame_player.layoutParams = params

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frame_player_podcast,
                        FragmentPlayerAudio.newInstance(
                            listPodcast.fileURL,
                            listPodcast.name, listPodcast.image182182, i
                        ), "player"
                    ).commit()

            }

        }

    }
    fun createChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel("1","PC Player",NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }


    companion object {
        fun newInstance(id: Long) = FragmentChiTietAudio().apply {
            arguments = Bundle().apply {
                putLong("idAlbumDetail", id)
            }
        }
    }
}