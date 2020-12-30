package app.vtcnews.android.model.Audio

import com.squareup.moshi.Json

data class PodcastInfo (
    @Json(name = "Id")
    val id: Long,

    @Json(name = "Name")
    val name: String,

    @Json(name = "Des")
    val des: String,

    @Json(name = "ShowMenu")
    val showMenu: Any? = null,

    @Json(name = "SEOSlug")
    val seoSlug: String,

    @Json(name = "SEOKeyword")
    val seoKeyword: String,

    @Json(name = "SEOTitle")
    val seoTitle: String,

    @Json(name = "SEODes")
    val seoDES: String,

    @Json(name = "ImageId")
    val imageID: Long,

    @Json(name = "ImageUrl")
    val imageURL: String,

    @Json(name = "ChannelId")
    val channelID: Long,

    @Json(name = "CreatedDate")
    val createdDate: String,

    @Json(name = "LastUpdated")
    val lastUpdated: Any? = null,

    @Json(name = "CreatedBy")
    val createdBy: Any? = null,

    @Json(name = "LastUpdatedBy")
    val lastUpdatedBy: Any? = null,

    @Json(name = "Author")
    val author: String,

    @Json(name = "image182_182")
    val image182182: String,

    @Json(name = "image360_360")
    val image360360: String,

    @Json(name = "CountItem")
    val countItem: Long
)