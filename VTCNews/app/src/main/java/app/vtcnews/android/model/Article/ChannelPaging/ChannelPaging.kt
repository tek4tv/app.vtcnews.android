package app.vtcnews.android.model.Article.ChannelPaging

import com.squareup.moshi.Json

data class ChannelPaging (
    @Json(name = "Info")
    val info: Info,

    @Json(name = "ThemeModel")
    val themeModel: List<Any?> ? = null,

    @Json(name = "Items")
    val items: List<ItemChannel>,

    @Json(name = "CurrentPage")
    val currentPage: Long,

    @Json(name = "PageSize")
    val pageSize: Long,

    @Json(name = "TotalRecord")
    val totalRecord: Long,

    @Json(name = "TotalPage")
    val totalPage: Long
        )
data class Info(
    @Json(name = "Id")
    val id: Long,

    @Json(name = "Title")
    val title: String,

    @Json(name = "TitleWithoutUnicode")
    val titleWithoutUnicode: String,

    @Json(name = "ImageBannerUrl")
    val imageBannerURL: String,

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
    val description: String,

    @Json(name = "Type")
    val type: Long,

    @Json(name = "CategoryName")
    val categoryName: String,

    @Json(name = "CateGorySEOSlug")
    val cateGorySEOSlug: String,

    @Json(name = "ListUrlImages")
    val listURLImages: Any? = null,

    @Json(name = "IsPhotoArticle")
    val isPhotoArticle: Long,

    @Json(name = "IsVideoArticle")
    val isVideoArticle: Long,

    @Json(name = "PublishedDate")
    val publishedDate: String,

    @Json(name = "ViewCount")
    val viewCount: Long,

    @Json(name = "SEOSlug")
    val seoSlug: String,

    @Json(name = "Duration")
    val duration: Long,

    @Json(name = "OrderBy")
    val orderBy: Long,

    @Json(name = "TotalRecord")
    val totalRecord: Long,

    @Json(name = "ListVideoInfo")
    val listVideoInfo: Any? = null,

    @Json(name = "LikeCount")
    val likeCount: Long,

    @Json(name = "Author")
    val author: Any? = null,

    @Json(name = "CountComment")
    val countComment: Long,

    @Json(name = "AudioUrl")
    val audioURL: Any? = null,

    @Json(name = "ImageThumb")
    val imageThumb: Any? = null,

    @Json(name = "image16_9")
    val image169: String
)