package app.vtcnews.android.model

import com.squareup.moshi.Json


data class HotChannel(
    @Json(name = "ImageUrl")
    val imageURL: String? = null,

    @Json(name = "ImageBannerUrl")
    val imageBannerURL: Any? = null,

    @Json(name = "ImageBannerMobileUrl")
    val imageBannerMobileURL: Any? = null,

    @Json(name = "Content")
    val content: Any? = null,

    @Json(name = "ChannelThemeId")
    val channelThemeID: Long? = null,

    @Json(name = "TitleTheme")
    val titleTheme: Any? = null,

    @Json(name = "Id")
    val id: Long? = null,

    @Json(name = "Title")
    val title: String? = null,

    @Json(name = "CategoryId")
    val categoryID: Long? = null,

    @Json(name = "SEOTitle")
    val seoTitle: String? = null,

    @Json(name = "SEOKeyword")
    val seoKeyword: String? = null,

    @Json(name = "SEODesciption")
    val seoDesciption: String? = null,

    @Json(name = "Image")
    val image: Long? = null,

    @Json(name = "IsHotNews")
    val isHotNews: Boolean? = null,

    @Json(name = "Order")
    val order: Long? = null,

    @Json(name = "TitleWithoutUnicode")
    val titleWithoutUnicode: String? = null,

    @Json(name = "Status")
    val status: Long? = null,

    @Json(name = "CreatedDate")
    val createdDate: String? = null,

    @Json(name = "ImageBanner")
    var imageBanner: Long? = null,

    @Json(name = "ImageBannerMobile")
    val imageBannerMobile: Any? = null,

    @Json(name = "image16_9")
    val image169: String? = null
)