package app.vtcnews.android.ui.audio

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import app.vtcnews.android.MainActivity
import app.vtcnews.android.R
import app.vtcnews.android.databinding.FragmentPlayerAudioBinding
import app.vtcnews.android.model.Audio.ListPodcast
import app.vtcnews.android.repos.AudioRepo
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class FragmentPlayerAudio : Fragment() {
    lateinit var binding: FragmentPlayerAudioBinding
    var currntPos: Int = 0
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var audioRepo: AudioRepo
    lateinit var mediaPlayer: MediaPlayer
    lateinit var listPodcast: List<ListPodcast>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerAudioBinding.inflate(layoutInflater, container, false)
        val fragment = requireActivity().supportFragmentManager.findFragmentByTag("fragVideo")
        if (fragment != null) {
            requireActivity().supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitleChapter.text = requireArguments().getString("name")
        binding.tvTitleChapter.isSelected = true
        if (requireArguments().getString("authur") != "") {
            binding.tvAuthur.text = requireArguments().getString("authur")
        }
        Picasso.get().load(requireArguments().getString("urlIMG")).into(binding.ivChapter)
        val urlMp3 = requireArguments().getString("urlMp3")!!
        if (urlMp3.startsWith("/")) {
            mediaPlayer = MediaPlayer.create(
                requireActivity(),
                Uri.parse("https://media.vtc.vn/" + urlMp3)
            )
        } else {
            mediaPlayer = MediaPlayer.create(
                requireActivity(),
                Uri.parse(urlMp3)
            )
        }
        mediaPlayer.start()
        listPodcast = audioRepo.listAlbumDetail
        buttonClick()

        val listPodcast = audioRepo.listAlbumDetail
        currntPos = requireArguments().getInt("curent")
        CreatNotifi(listPodcast, R.drawable.ic_pause, currntPos, listPodcast.size)

    }

    var broadcastReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            listPodcast = audioRepo.listAlbumDetail
            try {
                when (intent.getStringExtra("actionname")) {
                    "actionpre" -> {
                        mediaPlayer.release()
                        if (currntPos <= 0) {
                            currntPos = 0
                        } else {
                            currntPos--
                        }
                        CreatNotifi(listPodcast, R.drawable.ic_pause, currntPos, listPodcast.size)
                        mediaPlayer = MediaPlayer.create(
                            requireActivity(),
                            Uri.parse(
                                "https://media.vtc.vn/" + listPodcast[currntPos].fileURL
                            )
                        )
                        binding.tvTitleChapter.text = listPodcast[currntPos].name
                        mediaPlayer.start()
                        if (!mediaPlayer.isPlaying) {
                            binding.ibPlay.setImageResource(R.drawable.play)
                        } else {
                            binding.ibPlay.setImageResource(R.drawable.pause)
                        }

                    }
                    "actionplay" -> {
                        if (mediaPlayer.isPlaying) {
                            mediaPlayer.pause()
                            binding.ibPlay.setImageResource(R.drawable.play)
                            CreatNotifi(
                                listPodcast,
                                R.drawable.ic_play_arrow,
                                currntPos,
                                listPodcast.size
                            )

                        } else {
                            mediaPlayer.start()
                            binding.ibPlay.setImageResource(R.drawable.pause)
                            CreatNotifi(
                                listPodcast,
                                R.drawable.ic_pause,
                                currntPos,
                                listPodcast.size
                            )
                        }
                    }
                    "actionnext" -> {
                        mediaPlayer.release()
                        if (currntPos >= listPodcast.size - 1) {
                            currntPos = listPodcast.size - 1
                        } else {
                            currntPos++
                        }
                        CreatNotifi(listPodcast, R.drawable.ic_pause, currntPos, listPodcast.size)
                        mediaPlayer = MediaPlayer.create(
                            requireActivity(),
                            Uri.parse("https://media.vtc.vn/" + listPodcast[currntPos].fileURL)
                        )
                        binding.tvTitleChapter.text = listPodcast[currntPos].name
                        mediaPlayer.start()
                        if (!mediaPlayer.isPlaying) {
                            binding.ibPlay.setImageResource(R.drawable.play)
                        } else {
                            binding.ibPlay.setImageResource(R.drawable.pause)
                        }
                    }
                }
            } catch (e: Exception) {

            }

        }
    }

    fun CreatNotifi(listPodcast: List<ListPodcast>, playButton: Int, pos: Int, size: Int) {
        val mediaSessionCompat = MediaSessionCompat(context, "tag")
        lifecycleScope.launch(Dispatchers.IO) {
            val bitmap = Picasso.get().load(listPodcast[pos].image182182).get()
            val pendingIntenPre: PendingIntent?
            val drw_pre: Int
            if (pos == 0) {
                pendingIntenPre = null
                drw_pre = 0
            } else {
                val intentPre =
                    Intent(
                        requireContext(),
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
                Intent(context, NotifiAudioBroadcast::class.java).setAction("actionplay")
            val pendingIntenPlay = PendingIntent.getBroadcast(
                context,
                1,
                intentPlay,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            //val drw_play = R.drawable.ic_play_arrow
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
                    2,
                    intentNext,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                drw_next = R.drawable.ic_skip_next
            }

            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("notifiAudio", "AudioPlayer")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            notificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val mbuild = NotificationCompat.Builder(requireContext(), "1")
            mbuild.setSmallIcon(R.drawable.icaudiohome)
            mbuild.setContentTitle(listPodcast[pos].name)
            mbuild.setLargeIcon(bitmap)
            mbuild.addAction(drw_pre, "Previous", pendingIntenPre)
            mbuild.addAction(playButton, "Play", pendingIntenPlay)
            mbuild.addAction(drw_next, "Next", pendingIntenNext)
            mbuild.setContentIntent(pendingIntent)
            mbuild.setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
                    .setMediaSession(mediaSessionCompat.sessionToken)
            ).setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            notificationManager.notify(1, mbuild.build())
        }


    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    "1", "PC Player", NotificationManager.IMPORTANCE_LOW
                )
            channel.enableVibration(false)
            notificationManager =
                requireActivity().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun buttonClick() {
        binding.apply {
            ibPlay.setOnClickListener(View.OnClickListener {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    ibPlay.setImageResource(R.drawable.play)
                    CreatNotifi(
                        listPodcast,
                        R.drawable.ic_play_arrow,
                        currntPos,
                        listPodcast.size
                    )

                } else {
                    mediaPlayer.start()
                    ibPlay.setImageResource(R.drawable.pause)
                    CreatNotifi(
                        listPodcast,
                        R.drawable.ic_pause,
                        currntPos,
                        listPodcast.size
                    )
                }
            })

            currntPos = requireArguments().getInt("curent")
            ibNext.setOnClickListener {
                mediaPlayer.release()
                if (currntPos >= listPodcast.size - 1) {
                    currntPos = listPodcast.size - 1
                } else {
                    currntPos++
                }

                mediaPlayer = MediaPlayer.create(
                    requireActivity(),
                    Uri.parse("https://media.vtc.vn/" + listPodcast[currntPos].fileURL)
                )
                tvTitleChapter.text = listPodcast[currntPos].name
                mediaPlayer.start()
                if (!mediaPlayer.isPlaying) {
                    ibPlay.setImageResource(R.drawable.play)
                } else {
                    ibPlay.setImageResource(R.drawable.pause)
                }
                CreatNotifi(listPodcast, R.drawable.pause, currntPos, listPodcast.size)

            }
            ibPre.setOnClickListener {
                mediaPlayer.release()
                if (currntPos <= 0) {
                    currntPos = 0
                } else {
                    currntPos--
                }
                mediaPlayer = MediaPlayer.create(
                    requireActivity(),
                    Uri.parse(
                        "https://media.vtc.vn/" + listPodcast[currntPos].fileURL
                    )
                )
                tvTitleChapter.text = listPodcast[currntPos].name
                mediaPlayer.start()
                if (!mediaPlayer.isPlaying) {
                    ibPlay.setImageResource(R.drawable.play)
                } else {
                    ibPlay.setImageResource(R.drawable.pause)
                }
                CreatNotifi(listPodcast, R.drawable.pause, currntPos, listPodcast.size)
            }
            binding.ibClose.setOnClickListener(View.OnClickListener {
                mediaPlayer.stop()
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(0, R.anim.exit_to_right, 0, R.anim.exit_to_right)
                    .remove(this@FragmentPlayerAudio).commit()
                notificationManager.cancelAll()
            })


        }
    }

    companion object {
        fun newInstance(
            urlmp3: String,
            name: String,
            urlimg: String,
            position: Int,
            authur: String
        ) =
            FragmentPlayerAudio().apply {
                arguments = Bundle().apply {
                    putString("urlMp3", urlmp3)
                    putString("name", name)
                    putString("urlIMG", urlimg)
                    putString("urlIMG", urlimg)
                    putInt("curent", position)
                    putString("authur", authur)
                }
            }

        fun newInstance() = FragmentPlayerAudio()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(0, R.anim.exit_to_right, 0, R.anim.exit_to_right)
            .remove(this@FragmentPlayerAudio).commit()
        (requireActivity() as MainActivity).unregisterReceiver(broadcastReceiver)
    }


    override fun onResume() {
        super.onResume()
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            binding.ibPlay.setImageResource(R.drawable.play)
        } else {
            mediaPlayer.start()
            binding.ibPlay.setImageResource(R.drawable.pause)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            createChannel()
            (requireActivity() as MainActivity).registerReceiver(
                broadcastReceiver,
                IntentFilter("TRACK_TRACK")
            )
            (requireActivity() as MainActivity).startService(
                Intent(
                    context,
                    OnClearFromReciverService::class.java
                )
            )
        }

    }
}
