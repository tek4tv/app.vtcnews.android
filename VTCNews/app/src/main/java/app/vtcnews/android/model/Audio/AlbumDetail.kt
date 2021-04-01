package app.vtcnews.android.model.Audio

import com.squareup.moshi.Json

data class AlbumDetail(
    @Json(name = "Info")
    val info: PodcastInfo,

    @Json(name = "Items")
    val items: List<ListPodcast>
)

data class ListPodcast(
    @Json(name = "Id")
    val id: Long,

    @Json(name = "Name")
    val name: String,

    @Json(name = "FileUrl")
    val fileURL: String,

    @Json(name = "Des")
    val des: String? = null,

    @Json(name = "CreatedDate")
    val createdDate: String? = null,

    @Json(name = "Time")
    val time: Long? = null,

    @Json(name = "PublishedDate")
    val publishedDate: String? = null,

    @Json(name = "Status")
    val status: Long? = null,

    @Json(name = "AlbumId")
    val albumID: Long? = null,

    @Json(name = "TotalLength")
    val totalLength: Double? = null,

    @Json(name = "image182_182")
    val image182182: String
)
