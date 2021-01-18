package app.vtcnews.android.model

import com.squareup.moshi.Json

data class MenuItem(
    @Json(name = "Id")
    var id: Int? = null,
    @Json(name = "SEOSlug")
    var seoSlug: String? = null,
    @Json(name = "Title")
    var title: String? = null,
    @Json(name = "ParentId")
    var parentId: Int? = null,
    @Json(name = "OrderMobile")
    var orderMobile: Int? = null,
    @Json(name = "IsShowMenu")
    var isShowMenu: Boolean? = null
)
