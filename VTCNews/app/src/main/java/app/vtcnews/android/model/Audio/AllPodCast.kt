package app.vtcnews.android.model.Audio

import com.squareup.moshi.Json

data class AllPodCast(
    @Json(name = "Id")
    val id: Long,

    @Json(name = "ParrentId")
    val parrentID: Any? = null,

    @Json(name = "Name")
    val name: String,

    @Json(name = "Des")
    val des: Any? = null,

    @Json(name = "ImageId")
    val imageID: Any? = null,

    @Json(name = "ImageUrl")
    val imageURL: Any? = null,

    @Json(name = "SEOSlug")
    val seoSlug: String,

    @Json(name = "SEOKeyword")
    val seoKeyword: Any? = null,

    @Json(name = "SEOTitle")
    val seoTitle: Any? = null,

    @Json(name = "SEODes")
    val seoDES: Any? = null,

    @Json(name = "CreatedDate")
    val createdDate: String,

    @Json(name = "LastUpdated")
    val lastUpdated: Any? = null,

    @Json(name = "CreatedBy")
    val createdBy: Any? = null,

    @Json(name = "LastUpdatedBy")
    val lastUpdatedBy: Any? = null
)