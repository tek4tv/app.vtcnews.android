package app.vtcnews.android.repos

import androidx.lifecycle.MutableLiveData
import app.vtcnews.android.model.Audio.*
import app.vtcnews.android.network.Audio.AllPodcastService
import app.vtcnews.android.network.Resource
import app.vtcnews.android.network.performNetworkCall
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRepo @Inject constructor(private val audioService: AllPodcastService) {

    val listAllPodCast = MutableLiveData<List<AllPodCast>>()
    var listChannelPodcast = listOf<ChannelPodcast>()
    var listAlbumDetail = listOf<ListPodcast>()

    suspend fun getAllPodcast(): Resource<List<AllPodCast>> = performNetworkCall {
        audioService.getAllPodcast()

    }

    suspend fun getChannelPodcast(id: Long): Resource<List<ChannelPodcast>> {
        val respon = performNetworkCall {
            audioService.getChannelPodcast(id)
        }

        if (respon is Resource.Success) {
            listChannelPodcast = respon.data

        }

        return respon
    }

    suspend fun getAlbumPaging(channelid: Long): Resource<List<AlbumPaging>> = performNetworkCall {
        audioService.getAlbumPaging(channelid)
    }

    suspend fun getAlbumDetail(id: Long):
            Resource<AlbumDetail>{
        val res = performNetworkCall {
            audioService.getAlbumDetail(id)
        }

        if(res is Resource.Success)
            listAlbumDetail = res.data.items

        return res
    }
}



