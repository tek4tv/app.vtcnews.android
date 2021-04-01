package app.vtcnews.android.network

import app.vtcnews.android.model.ArticleVideo
import app.vtcnews.android.model.Video
import app.vtcnews.android.model.VideoDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VideoService {
    @GET("home/news/GetVideoHome")
    suspend fun getVideoHome(): Response<List<Video>>

    @GET("home/news/GetVideoDetail/{ArticleId}")
    suspend fun getVideoDetail(@Path("ArticleId") id: Long): Response<List<VideoDetail>>

    @GET("home/video/GetVideoById")
    suspend fun getArticleVideoById(
        @Query("text") videoId: String
    ): Response<List<ArticleVideo>>

    @GET("home/news/Video/VideoHot/27")
    suspend fun getVideoMenuHome(): Response<List<Video>>

    @GET("home/news/Video/VideoSuggestionAjax/27/{page}")
    suspend fun getVideoMenuHomePaging(@Path("page") page: Int): Response<List<Video>>
}