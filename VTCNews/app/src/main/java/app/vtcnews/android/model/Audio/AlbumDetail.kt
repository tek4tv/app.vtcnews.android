package app.vtcnews.android.model.Audio

import com.squareup.moshi.Json

data class AlbumDetail (
    @Json(name = "Info")
    val info: PodcastInfo,

    @Json(name = "Items")
    val items: List<ListPodcast>
)
