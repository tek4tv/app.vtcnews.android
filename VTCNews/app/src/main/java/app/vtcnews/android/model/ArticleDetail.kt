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

    val articleStructure: String? = null,
    val folderStructure: Any? = null,
    val breakcrumbStructure: String? = null,
    val videoStructure: Any? = null,

    @Json(name = "ListArticleRelated")
    val relatedArticleList: List<Article>,

    @Json(name = "image16_9")
    val image169: String? = null
)

data class DetailData(
    val tags: List<Any?>,

    @Json(name = "Id")
    val id: Long? = null,

    @Json(name = "Title")
    val title: String? = null,

    @Json(name = "CategoryId")
    val categoryID: Long? = null,

    @Json(name = "ImageUrl")
    val imageURL: String? = null,

    @Json(name = "ImageWidth")
    val imageWidth: Long? = null,

    @Json(name = "ImageHeight")
    val imageHeight: Long? = null,

    @Json(name = "Image")
    val image: Long? = null,

    @Json(name = "Description")
    val description: String? = null,

    @Json(name = "CategoryName")
    val categoryName: String? = null,

    @Json(name = "CategoryCode")
    val categoryCode: String? = null,

    @Json(name = "PublishedDate")
    val publishedDate: String? = null,

    @Json(name = "ViewCount")
    val viewCount: Long? = null,

    @Json(name = "Content")
    val content: String? = null,

    @Json(name = "IsContentCached")
    val isContentCached: Boolean? = null,

    @Json(name = "InMatch")
    val inMatch: Long? = null,

    @Json(name = "CountComment")
    val countComment: Long? = null,

    @Json(name = "ImageFacebookUrl")
    val imageFacebookURL: String? = null,

    @Json(name = "CateSEOSlug")
    val cateSEOSlug: String? = null,

    @Json(name = "LastUpdateDate")
    val lastUpdateDate: String? = null,

    @Json(name = "CreatedDate")
    val createdDate: String? = null,

    @Json(name = "LocationRegionalCode")
    val locationRegionalCode: Any? = null,

    @Json(name = "LocationLatitude")
    val locationLatitude: Any? = null,

    @Json(name = "LocationLongitude")
    val locationLongitude: Any? = null,

    @Json(name = "Author")
    val author: String? = null,

    @Json(name = "Type")
    val type: Long? = null,

    @Json(name = "LikeCount")
    val likeCount: Long? = null,

    @Json(name = "NoteSignature")
    val noteSignature: String? = null,

    @Json(name = "AmpStatus")
    val ampStatus: Boolean? = null,

    val isVideoArticle: Boolean? = null,

    @Json(name = "AdGoogle")
    val adGoogle: Boolean? = null,

    @Json(name = "ChannelId")
    val channelID: Any? = null,

    @Json(name = "ChannelName")
    val channelName: Any? = null,

    @Json(name = "ChannelSlug")
    val channelSlug: Any? = null,

    @Json(name = "IsFacebook")
    val isFacebook: Boolean? = null,

    @Json(name = "IsZalo")
    val isZalo: Boolean? = null,

    @Json(name = "IsTweet")
    val isTweet: Boolean? = null,

    @Json(name = "IsYoutube")
    val isYoutube: Boolean? = null,

    @Json(name = "PublishedDateInt")
    val publishedDateInt: Long? = null,

    @Json(name = "SEODescription")
    val seoDescription: Any? = null,

    @Json(name = "SEOTitle")
    val seoTitle: String? = null,

    @Json(name = "SEOTagKeyword")
    val seoTagKeyword: String? = null,

    @Json(name = "SEOKeyword")
    val seoKeyword: String? = null,

    @Json(name = "SEOSlug")
    val seoSlug: String? = null
)

data class CategoryDetail(
    @Json(name = "Id")
    val id: Long? = null,

    @Json(name = "SEOSlug")
    val seoSlug: String? = null,

    @Json(name = "Title")
    val title: String? = null,

    @Json(name = "ParentId")
    val parentID: Long? = null,

    @Json(name = "OrderDesktop")
    val orderDesktop: Long? = null,

    @Json(name = "OrderMobile")
    val orderMobile: Long? = null,

    @Json(name = "OrderIndex")
    val orderIndex: Long? = null,

    @Json(name = "OrderFooter")
    val orderFooter: Long? = null,

    @Json(name = "IsShowMenu")
    val isShowMenu: Boolean? = null,

    @Json(name = "Selected")
    val selected: Long? = null,

    @Json(name = "Domain")
    val domain: Any? = null
)

data class Tag(
    @Json(name = "TagId")
    val tagID: Long? = null,

    @Json(name = "TagName")
    val tagName: String? = null,

    @Json(name = "TagTitleWithoutUnicode")
    val tagTitleWithoutUnicode: String? = null
)

data class SourceInfo(
    @Json(name = "Id")
    val id: Long? = null,

    @Json(name = "Name")
    val name: Any? = null,

    @Json(name = "Slug")
    val slug: Any? = null
)