package app.vtcnews.android.network.Audio

import app.vtcnews.android.model.Audio.AlbumDetail
import app.vtcnews.android.model.Audio.AlbumPaging
import app.vtcnews.android.model.Audio.AllPodCast
import app.vtcnews.android.model.Audio.ChannelPodcast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AllPodcastService {
    @GET("podcast/GetAllPodcast")
    suspend fun getAllPodcast(): Response<List<AllPodCast>>

    @GET("podcast/ChannelByPodcast/{Id}")
    suspend fun getChannelPodcast(@Path("Id") id: Long): Response<List<ChannelPodcast>>

    @GET("podcast/GetAlbumPaging/chanId/{ChannelId}/pageIndex/{page}")
    suspend fun getAlbumPaging(
        @Path("ChannelId") channelid: Long,
        @Path("page") page: Int
    ): Response<MutableList<AlbumPaging>>

    @GET("podcast/AlbumDetail/{Id}")
    suspend fun getAlbumDetail(@Path("Id") id: Long): Response<AlbumDetail>
}