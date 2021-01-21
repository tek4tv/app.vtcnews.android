package app.vtcnews.android.model.Audio

import com.squareup.moshi.Json

data class AlbumPaging (
    @Json(name = "Id")
    val id: Long,

    @Json(name = "Name")
    val name: String,

    @Json(name = "Des")
    val des: String? = null,

    @Json(name = "ShowMenu")
    val showMenu: Any? = null,

    @Json(name = "SEOSlug")
    val seoSlug: String? = null,

    @Json(name = "SEOKeyword")
    val seoKeyword: String? = null,

    @Json(name = "SEOTitle")
    val seoTitle: String? = null,

    @Json(name = "SEODes")
    val seoDES: String? = null,

    @Json(name = "ImageId")
    val imageID: Long? = null,

    @Json(name = "ImageUrl")
    val imageURL: String? = null,

    @Json(name = "ChannelId")
    val channelID: Long,

    @Json(name = "CreatedDate")
    val createdDate: String? = null,

    @Json(name = "LastUpdated")
    val lastUpdated: Any? = null,

    @Json(name = "CreatedBy")
    val createdBy: Any? = null,

    @Json(name = "LastUpdatedBy")
    val lastUpdatedBy: Any? = null,

    @Json(name = "Author")
    val author: String? = null,

    @Json(name = "image182_182")
    val image182182: String? = null,

    @Json(name = "image360_360")
    val image360360: String
)