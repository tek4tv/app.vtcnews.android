package app.vtcnews.android.model

import com.squareup.moshi.Json

data class ChannelPaging (
    @Json(name = "Info")
    val info: Info,

    @Json(name = "ThemeModel")
    val themeModel: List<Any?> ? = null,

    @Json(name = "Items")
    val items: List<ItemChannel>,

    @Json(name = "CurrentPage")
    val currentPage: Long? = null,

    @Json(name = "PageSize")
    val pageSize: Long? = null,

    @Json(name = "TotalRecord")
    val totalRecord: Long? = null,

    @Json(name = "TotalPage")
    val totalPage: Long? = null
        )
data class Info(
    @Json(name = "Id")
    val id: Long,

    @Json(name = "Title")
    val title: String,

    @Json(name = "TitleWithoutUnicode")
    val titleWithoutUnicode: String? = null,

    @Json(name = "ImageBannerUrl")
    val imageBannerURL: String? = null,

    @Json(name = "ImageBannerMobileUrl")
    val imageBannerMobileURL: Any? = null
)
data class ItemChannel(
    @Json(name = "Id")
    val id: Int,

    @Json(name = "Title")
    val title: String,

    @Json(name = "CategoryId")
    val categoryID: Long,

    @Json(name = "ImageUrl")
    val imageURL: String,

    @Json(name = "Description")
    val description: String? = null,

    @Json(name = "Type")
    val type: Long? = null,

    @Json(name = "CategoryName")
    val categoryName: String,

    @Json(name = "CateGorySEOSlug")
    val cateGorySEOSlug: String? = null,

    @Json(name = "ListUrlImages")
    val listURLImages: Any? = null,

    @Json(name = "IsPhotoArticle")
    val isPhotoArticle: Long? = null,

    @Json(name = "IsVideoArticle")
    val isVideoArticle: Long? = null,

    @Json(name = "PublishedDate")
    val publishedDate: String? = null,

    @Json(name = "ViewCount")
    val viewCount: Long? = null,

    @Json(name = "SEOSlug")
    val seoSlug: String? = null,

    @Json(name = "Duration")
    val duration: Long? = null,

    @Json(name = "OrderBy")
    val orderBy: Long? = null,

    @Json(name = "TotalRecord")
    val totalRecord: Long? = null,

    @Json(name = "ListVideoInfo")
    val listVideoInfo: Any? = null,

    @Json(name = "LikeCount")
    val likeCount: Long? = null,

    @Json(name = "Author")
    val author: Any? = null,

    @Json(name = "CountComment")
    val countComment: Long? = null,

    @Json(name = "AudioUrl")
    val audioURL: Any? = null,

    @Json(name = "ImageThumb")
    val imageThumb: Any? = null,

    @Json(name = "image16_9")
    val image169: String
)