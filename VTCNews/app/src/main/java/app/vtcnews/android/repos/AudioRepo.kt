package app.vtcnews.android.repos

import androidx.lifecycle.MutableLiveData
import app.vtcnews.android.model.Audio.AllPodCast
import app.vtcnews.android.model.Audio.ChannelPodcast
import app.vtcnews.android.network.Audio.AllPodcastService
import app.vtcnews.android.network.Resource
import app.vtcnews.android.network.performNetworkCall
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRepo @Inject constructor(private val audioService: AllPodcastService) {

    val listAllPodCast= MutableLiveData<List<AllPodCast>>()
    suspend fun getAllPodcast(): Resource<List<AllPodCast>> = performNetworkCall {
        audioService.getAllPodcast()

    }
    suspend fun getChannelPodcast(id:Long):Resource<List<ChannelPodcast>> = performNetworkCall {
        audioService.getChannelPodcast(id)
    }


}