package app.vtcnews.android.repos

import app.vtcnews.android.model.Video
import app.vtcnews.android.network.Resource
import app.vtcnews.android.network.VideoService
import app.vtcnews.android.network.performNetworkCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepo @Inject constructor(private val videoService: VideoService) {
    var videoList = listOf<Video>()

    suspend fun getVideos() : Resource<List<Video>>
    {
        val res = performNetworkCall { videoService.getVideoHome() }
        if(res is Resource.Success)
            videoList = res.data
        return res
    }
}