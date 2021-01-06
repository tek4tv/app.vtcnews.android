package app.vtcnews.android.ui.video

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import app.vtcnews.android.R
import app.vtcnews.android.model.Audio.ListPodcast

class VideoNotification {
    companion object {
        val CHANNEL_ID = "1"
        val action_pre = "actionpre"
        val action_play = "actionplay"
        val action_next = "actionnext"
        lateinit var notification: NotificationCompat.Builder
        fun createNotifi(context: Context, listPC: ListPodcast) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notifacasionManager = context.getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
                val mediaSess = MediaSessionCompat(context, "tag")

                notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_play_arrow)
                    .setContentTitle(listPC.name)
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                notifacasionManager.notify(1, notification.build())
            }
        }
    }
}