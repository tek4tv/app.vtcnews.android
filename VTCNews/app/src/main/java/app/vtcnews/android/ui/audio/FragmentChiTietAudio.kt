package app.vtcnews.android.ui.audio

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.app.NotificationCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import app.vtcnews.android.MainActivity
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentChitietAudioBinding
import app.vtcnews.android.model.Audio.ListPodcast
import app.vtcnews.android.viewmodels.AudioHomeFragViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlin.concurrent.thread

@AndroidEntryPoint
class FragmentChiTietAudio : Fragment() {
    private val viewModel: AudioHomeFragViewModel by viewModels()
    lateinit var binding: FragmentChitietAudioBinding
    lateinit var notificationManager: NotificationManager
    var positon =0
    val isPlaying = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChitietAudioBinding.inflate(layoutInflater, container, false)
        (requireActivity() as MainActivity).supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btBack.setOnClickListener {
            binding.layoutMenu.isVisible = false
            (requireActivity() as MainActivity).supportActionBar?.show()
            requireActivity().supportFragmentManager.popBackStack()
        }
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
            val sizeList = it.size
            listChapterAdapter.clickListen = { listPodcast: ListPodcast, i: Int ->
                CreatNotifi(listPodcast,i,sizeList)
                val broadcastReceiver = object : BroadcastReceiver() {

                    override fun onReceive(p0: Context?, p1: Intent?) {
                        when (p1?.extras?.getString("actionname")) {
                            "actionpre" -> {
                                //onAudioPre()
                                CreatNotifi(listPodcast,i,sizeList)

                            }
                            "actionplay" -> {
                                if(isPlaying)
                                {
                                    onAudioPause()

                                }
                                else
                                {
//                                  onAudioPlay()
                                    CreatNotifi(listPodcast,i,sizeList)
                                }
                            }
                            "actionnext" -> {
//                                onAudioNext()
                                CreatNotifi(listPodcast,i,sizeList)
                            }
                        }
                    }
                }

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                createChannel()
                requireActivity().registerReceiver(broadcastReceiver, IntentFilter("TRACK_TRACK"))
                requireActivity().startService(
                    Intent(
                        context,
                        OnClearFromReciverService::class.java
                    )
                )
                }



                val frame_player =
                    requireActivity().findViewById<FrameLayout>(R.id.frame_player_podcast)
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
    fun onAudioPause(listPodcast: ListPodcast,pos : Int,sizeList:Int){

        CreatNotifi(listPodcast,pos,sizeList)

    }
    fun onAudioPlay(){}
    fun CreatNotifi(listPodcast : ListPodcast,pos :Int,size :Int)
    {
        thread {
            val bitmap = Picasso.get().load(listPodcast.image182182).get()

            val pendingIntenPre: PendingIntent?
            val drw_pre: Int
            if (pos == 0) {
                pendingIntenPre = null
                drw_pre = 0
            } else {
                val intentPre =
                    Intent(
                        context,
                        NotifiAudioBroadcast::class.java
                    ).setAction("actionpre")
                pendingIntenPre = PendingIntent.getBroadcast(
                    context,
                    0,
                    intentPre,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                drw_pre = R.drawable.ic_skip_previous
            }
            //Play
            val intentPlay =
                Intent(context, NotifiAudioBroadcast::class.java).setAction(
                    "actionplay"
                )
            val pendingIntenPlay = PendingIntent.getBroadcast(
                context,
                0,
                intentPlay,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val drw_play = R.drawable.ic_play_arrow
            //next
            val pendingIntenNext: PendingIntent?
            val drw_next: Int
            if (pos == size) {
                pendingIntenNext = null
                drw_next = 0
            } else {
                val intentNext = Intent(
                    context,
                    NotifiAudioBroadcast::class.java
                ).setAction("actionnext")
                pendingIntenNext = PendingIntent.getBroadcast(
                    context,
                    0,
                    intentNext,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                drw_next = R.drawable.ic_skip_next
            }
            notificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val mbuild = NotificationCompat.Builder(requireContext(), "1")
            mbuild.setSmallIcon(R.drawable.ic_play_arrow)
            mbuild.setContentTitle(listPodcast.name)
            mbuild.setContentText("...")
            mbuild.setLargeIcon(bitmap)
            mbuild.addAction(drw_pre, "Previous", pendingIntenPre)
            mbuild.addAction(drw_play, "Play", pendingIntenPlay)
            mbuild.addAction(drw_next, "Next", pendingIntenNext)
            mbuild.setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
            )
            notificationManager.notify(1, mbuild.build())
        }
    }

    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("1", "PC Player", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager =
                requireActivity().getSystemService(NotificationManager::class.java)
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding.layoutMenu.isVisible = false
        (requireActivity() as MainActivity).supportActionBar?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.cancelAll()
        }
        (requireActivity() as MainActivity).supportActionBar?.show()
    }
}