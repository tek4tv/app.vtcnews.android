package app.vtcnews.android.model

import com.squareup.moshi.Json

data class Article (
    @Json(name = "Id")
    val id: Int,

    @Json(name = "Title")
    val title: String,

    @Json(name = "CategoryId")
    val categoryID: Long,

    @Json(name = "ImageUrl")
    val imageURL: String,

    @Json(name = "Description")
    val description: String,

    @Json(name = "OrderBy")
    val orderBy: Long,

    @Json(name = "CategoryName")
    val categoryName: String,

    @Json(name = "PublishedDate")
    val publishedDate: String,

    @Json(name = "ViewCount")
    val viewCount: Long,

    @Json(name = "Duration")
    val duration: Long,

    @Json(name = "SEOSlug")
    val seoSlug: String,

    @Json(name = "CateGorySEOSlug")
    val cateGorySEOSlug: String,

    @Json(name = "TotalRecord")
    val totalRecord: Long,

    @Json(name = "ListVideoInfo")
    val listVideoInfo: Any? = null,

    @Json(name = "IsPhotoArticle")
    val isPhotoArticle: Long,

    @Json(name = "IsVideoArticle")
    val isVideoArticle: Long,

    @Json(name = "ListUrlImages")
    val listURLImages: String,

    @Json(name = "LikeCount")
    val likeCount: Long,

    @Json(name = "Type")
    val type: Long,

    @Json(name = "Author")
    val author: Any? = null,

    @Json(name = "CountComment")
    val countComment: Long,

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