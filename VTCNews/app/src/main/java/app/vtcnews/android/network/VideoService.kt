package app.vtcnews.android.network

import app.vtcnews.android.model.Video
import retrofit2.Response
import retrofit2.http.GET

interface VideoService {
    @GET("home/news/GetVideoHome")
    suspend fun getVideoHome() : Response<List<Video>>
}