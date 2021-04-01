package app.vtcnews.android.model

import com.squareup.moshi.Json

data class Video(
    @Json(name = "Id")
    val id: Long,

    @Json(name = "Title")
    val title: String,

    @Json(name = "CategoryId")
    val categoryID: Long,

    @Json(name = "ImageUrl")
    val imageURL: String? = null,

    @Json(name = "Description")
    val description: String? = null,

    @Json(name = "OrderBy")
    val orderBy: Long? = null,

    @Json(name = "CategoryName")
    val categoryName: String? = null,

    @Json(name = "PublishedDate")
    val publishedDate: String,

    @Json(name = "ViewCount")
    val viewCount: Long? = null,

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
    val isPhotoArticle: Long? = null,

    @Json(name = "IsVideoArticle")
    val isVideoArticle: Long,

    @Json(name = "ListUrlImages")
    val listURLImages: Any? = null,

    @Json(name = "LikeCount")
    val likeCount: Long? = null,

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
    @Json(name = "ImageResize430x243")
    val image430: String? = null

)

data class VideoDetail(
    @Json(name = "Id")
    val id: Long,

    @Json(name = "ArticleId")
    val articleID: Long,

    @Json(name = "FileId")
    val fileID: Long,

    @Json(name = "VideoURL")
    val videoURL: String
)

data class ArticleVideo(
    @Json(name = "Id")
    var id: Long,
    @Json(name = "URL")
    var url: String
)