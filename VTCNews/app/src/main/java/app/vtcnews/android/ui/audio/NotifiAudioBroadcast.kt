package app.vtcnews.android.ui.audio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotifiAudioBroadcast : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.sendBroadcast(
            Intent("TRACK_TRACK")
                .putExtra("actionname", p1?.action)
        )

    }
}