package app.vtcnews.android.model

import com.squareup.moshi.Json

data class Article (
    @Json(name = "Id")
    val id: Int,

    @Json(name = "Title")
    val title: String? = null,

    @Json(name = "CategoryId")
    val categoryID: Long?  = null,

    @Json(name = "ImageUrl")
    val imageURL: String? = null,

    @Json(name = "Description")
    val description: String? = null,

    @Json(name = "OrderBy")
    val orderBy: Long? = null,

    @Json(name = "CategoryName")
    val categoryName: String? = null,

    @Json(name = "PublishedDate")
    val publishedDate: String? = null,

    @Json(name = "ViewCount")
    val viewCount: Long? =  null,

    @Json(name = "Duration")
    val duration: Long? = null,

    @Json(name = "SEOSlug")
    val seoSlug: String? = null,

    @Json(name = "CateGorySEOSlug")
    val cateGorySEOSlug: String? = null,

    @Json(name = "TotalRecord")
    val totalRecord: Long? = null,

    @Json(name = "ListVideoInfo")
    val listVideoInfo: Any? = null,

    @Json(name = "IsPhotoArticle")
    val isPhotoArticle: Long?  = null,

    @Json(name = "IsVideoArticle")
    val isVideoArticle: Long? =  null,

    @Json(name = "ListUrlImages")
    val listURLImages: String? = null,

    @Json(name = "LikeCount")
    val likeCount: Long?  = null,

    @Json(name = "Type")
    val type: Long? = null,

    @Json(name = "Author")
    val author: Any? = null,

    @Json(name = "CountComment")
    val countComment: Long? = null,

    @Json(name = "AudioUrl")
    val audioURL: Any? = null,

    @Json(name = "ImageThumb")
    val imageThumb: Any? = null,

    @Json(name = "image16_9_large")
    val image169_Large: String? = null,

    @Json(name = "image16_9")
    val image169: String? = null,

    @Json(name = "imagecrop")
    val imageCrop: String? = null
)