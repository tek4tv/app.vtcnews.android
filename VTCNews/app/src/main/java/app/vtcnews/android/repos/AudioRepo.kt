package app.vtcnews.android.repos

import androidx.lifecycle.MutableLiveData
import app.vtcnews.android.model.Audio.*
import app.vtcnews.android.network.Audio.AllPodcastService
import app.vtcnews.android.network.Resource
import app.vtcnews.android.network.performNetworkCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRepo @Inject constructor(private val audioService: AllPodcastService) {

    val listAllPodCast = MutableLiveData<List<AllPodCast>>()
    var listChannelPodcast = listOf<ChannelPodcast>()
    var listAlbumDetail = listOf<ListPodcast>()
    var listAlbumPaging = listOf<AlbumPaging>()

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

    suspend fun getAlbumPaging(channelid: Long): Resource<List<AlbumPaging>> {
        val res = performNetworkCall {
            audioService.getAlbumPaging(channelid)
        }

        if(res is Resource.Success)
        {
            listAlbumPaging = res.data
        }
        return res

    }

    suspend fun getAlbumDetail(id: Long):
            Resource<AlbumDetail> {
        val res = performNetworkCall {
            audioService.getAlbumDetail(id)
        }

        if (res is Resource.Success)
            listAlbumDetail = res.data.items

        return res
    }
}



