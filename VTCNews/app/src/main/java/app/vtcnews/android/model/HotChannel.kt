package app.vtcnews.android.model

import com.squareup.moshi.Json

data class HotChannel (
    @Json(name = "ImageUrl")
    val imageURL: String,

    @Json(name = "ImageBannerUrl")
    val imageBannerURL: Any? = null,

    @Json(name = "ImageBannerMobileUrl")
    val imageBannerMobileURL: Any? = null,

    @Json(name = "Content")
    val content: Any? = null,

    @Json(name = "ChannelThemeId")
    val channelThemeID: Long,

    @Json(name = "TitleTheme")
    val titleTheme: Any? = null,

    @Json(name = "Id")
    val id: Long,

    @Json(name = "Title")
    val title: String,

    @Json(name = "CategoryId")
    val categoryID: Long,

    @Json(name = "SEOTitle")
    val seoTitle: String,

    @Json(name = "SEOKeyword")
    val seoKeyword: String,

    @Json(name = "SEODesciption")
    val seoDesciption: String? = null,

    @Json(name = "Image")
    val image: Long,

    @Json(name = "IsHotNews")
    val isHotNews: Boolean,

    @Json(name = "Order")
<<<<<<< Updated upstream
    val order: Long,
=======
    val order: Any? = null,
>>>>>>> Stashed changes

    @Json(name = "TitleWithoutUnicode")
    val titleWithoutUnicode: String,

    @Json(name = "Status")
    val status: Long,

    @Json(name = "CreatedDate")
    val createdDate: String,

    @Json(name = "ImageBanner")
<<<<<<< Updated upstream
    val imageBanner: Long,
=======
    val imageBanner: Long? = null,
>>>>>>> Stashed changes

    @Json(name = "ImageBannerMobile")
    val imageBannerMobile: Any? = null,

    @Json(name = "image16_9")
<<<<<<< Updated upstream
    val image169: String
)
=======
    val image169: String? = null
        )
>>>>>>> Stashed changes
