package app.vtcnews.android.model.Audio

import com.squareup.moshi.Json

data class ChannelPodcast(
    @Json(name = "Id")
    val id: Long,

    @Json(name = "Name")
    val name: String,

    @Json(name = "Des")
    val des: Any? = null,

    @Json(name = "Keyword")
    val keyword: Any? = null,

    @Json(name = "Podcast")
    val podcast: Long? = null,

    @Json(name = "Slug")
    val slug: String? = null
)