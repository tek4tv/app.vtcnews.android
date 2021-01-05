package app.vtcnews.android.network

import app.vtcnews.android.model.Video
import app.vtcnews.android.model.VideoDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface VideoService {
    @GET("home/news/GetVideoHome")
    suspend fun getVideoHome() : Response<List<Video>>

    @GET("home/news/GetVideoDetail/{ArticleId}")
    suspend fun getVideoDetail(@Path("ArticleId")id:Long):Response<List<VideoDetail>>
}