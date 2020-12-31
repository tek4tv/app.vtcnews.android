package app.vtcnews.android.model

import com.squareup.moshi.Json

data class ArticleDetail(
    val videoList: List<Any?>,

    @Json(name = "DetailData")
    val detailData: DetailData,

    @Json(name = "ListCategoryDetail")
    val listCategoryDetail: List<CategoryDetail>,

    @Json(name = "ListTag")
    val listTag: List<Tag>,

    @Json(name = "SourceInfo")
    val sourceInfo: SourceInfo,

    val articleStructure: String,
    val folderStructure: Any? = null,
    val breakcrumbStructure: String,
    val videoStructure: Any? = null,

    @Json(name = "ListArticleRelated")
    val relatedArticleList: List<Article>,

    @Json(name = "image16_9")
    val image169: String? = null
)

data class DetailData(
    val tags: List<Any?>,

    @Json(name = "Id")
    val id: Long,

    @Json(name = "Title")
    val title: String,

    @Json(name = "CategoryId")
    val categoryID: Long,

    @Json(name = "ImageUrl")
    val imageURL: String,

    @Json(name = "ImageWidth")
    val imageWidth: Long,

    @Json(name = "ImageHeight")
    val imageHeight: Long,

    @Json(name = "Image")
    val image: Long,

    @Json(name = "Description")
    val description: String,

    @Json(name = "CategoryName")
    val categoryName: String,

    @Json(name = "CategoryCode")
    val categoryCode: String,

    @Json(name = "PublishedDate")
    val publishedDate: String,

    @Json(name = "ViewCount")
    val viewCount: Long,

    @Json(name = "Content")
    val content: String,

    @Json(name = "IsContentCached")
    val isContentCached: Boolean,

    @Json(name = "InMatch")
    val inMatch: Long,

    @Json(name = "CountComment")
    val countComment: Long,

    @Json(name = "ImageFacebookUrl")
    val imageFacebookURL: String,

    @Json(name = "CateSEOSlug")
    val cateSEOSlug: String,

    @Json(name = "LastUpdateDate")
    val lastUpdateDate: String,

    @Json(name = "CreatedDate")
    val createdDate: String,

    @Json(name = "LocationRegionalCode")
    val locationRegionalCode: Any? = null,

    @Json(name = "LocationLatitude")
    val locationLatitude: Any? = null,

    @Json(name = "LocationLongitude")
    val locationLongitude: Any? = null,

    @Json(name = "Author")
    val author: String,

    @Json(name = "Type")
    val type: Long,

    @Json(name = "LikeCount")
    val likeCount: Long,

    @Json(name = "NoteSignature")
    val noteSignature: String,

    @Json(name = "AmpStatus")
    val ampStatus: Boolean,

    val isVideoArticle: Boolean,

    @Json(name = "AdGoogle")
    val adGoogle: Boolean,

    @Json(name = "ChannelId")
    val channelID: Any? = null,

    @Json(name = "ChannelName")
    val channelName: Any? = null,

    @Json(name = "ChannelSlug")
    val channelSlug: Any? = null,

    @Json(name = "IsFacebook")
    val isFacebook: Boolean,

    @Json(name = "IsZalo")
    val isZalo: Boolean,

    @Json(name = "IsTweet")
    val isTweet: Boolean,

    @Json(name = "IsYoutube")
    val isYoutube: Boolean,

    @Json(name = "PublishedDateInt")
    val publishedDateInt: Long,

    @Json(name = "SEODescription")
    val seoDescription: Any? = null,

    @Json(name = "SEOTitle")
    val seoTitle: Any? = null,

    @Json(name = "SEOTagKeyword")
    val seoTagKeyword: String,

    @Json(name = "SEOKeyword")
    val seoKeyword: String,

    @Json(name = "SEOSlug")
    val seoSlug: String
)

data class CategoryDetail(
    @Json(name = "Id")
    val id: Long,

    @Json(name = "SEOSlug")
    val seoSlug: String,

    @Json(name = "Title")
    val title: String,

    @Json(name = "ParentId")
    val parentID: Long? = null,

    @Json(name = "OrderDesktop")
    val orderDesktop: Long,

    @Json(name = "OrderMobile")
    val orderMobile: Long,

    @Json(name = "OrderIndex")
    val orderIndex: Long,

    @Json(name = "OrderFooter")
    val orderFooter: Long,

    @Json(name = "IsShowMenu")
    val isShowMenu: Boolean,

    @Json(name = "Selected")
    val selected: Long,

    @Json(name = "Domain")
    val domain: Any? = null
)

data class Tag(
    @Json(name = "TagId")
    val tagID: Long,

    @Json(name = "TagName")
    val tagName: String,

    @Json(name = "TagTitleWithoutUnicode")
    val tagTitleWithoutUnicode: String
)

data class SourceInfo(
    @Json(name = "Id")
    val id: Long,

    @Json(name = "Name")
    val name: Any? = null,

    @Json(name = "Slug")
    val slug: Any? = null
)