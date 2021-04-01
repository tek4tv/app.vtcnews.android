package app.vtcnews.android.repos

import app.vtcnews.android.model.Video
import app.vtcnews.android.model.VideoDetail
import app.vtcnews.android.network.Resource
import app.vtcnews.android.network.VideoService
import app.vtcnews.android.network.performNetworkCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepo @Inject constructor(private val videoService: VideoService) {
    var videoList = listOf<Video>()

    suspend fun getVideos(): Resource<List<Video>> {
        val res = performNetworkCall { videoService.getVideoHome() }
        if (res is Resource.Success)
            videoList = res.data
        return res
    }

    suspend fun getVideoDetail(id: Long): Resource<List<VideoDetail>> = performNetworkCall {
        videoService.getVideoDetail(id)
    }

    suspend fun getMenuVideos(): Resource<List<Video>> {
        val res = performNetworkCall { videoService.getVideoMenuHome() }
        if (res is Resource.Success)
            videoList = res.data
        return res
    }

    suspend fun getMenuVideosPaging(page: Int): Resource<List<Video>> =
        performNetworkCall {
            videoService.getVideoMenuHomePaging(page)
        }


}