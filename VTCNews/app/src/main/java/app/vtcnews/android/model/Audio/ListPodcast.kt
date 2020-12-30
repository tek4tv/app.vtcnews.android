package app.vtcnews.android.model.Audio

import com.squareup.moshi.Json

data class ListPodcast (
    @Json(name = "Id")
    val id: Long,

    @Json(name = "Name")
    val name: String,

    @Json(name = "FileUrl")
    val fileURL: String,

    @Json(name = "Des")
    val des: String,

    @Json(name = "CreatedDate")
    val createdDate: String,

    @Json(name = "Time")
    val time: Long,

    @Json(name = "PublishedDate")
    val publishedDate: String,

    @Json(name = "Status")
    val status: Long,

    @Json(name = "AlbumId")
    val albumID: Long,

    @Json(name = "TotalLength")
    val totalLength: Double,

    @Json(name = "image182_182")
    val image182182: String
)