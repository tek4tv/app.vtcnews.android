package app.vtcnews.android.model

import com.squareup.moshi.Json

data class MenuItem(
    @Json(name = "Id")
    var id: Int,
    @Json(name = "SEOSlug")
    var seoSlug: String,
    @Json(name = "Title")
    var title: String,
    @Json(name = "ParentId")
    var parentId: Int? = null,
    @Json(name = "OrderMobile")
    var orderMobile: Int,
    @Json(name = "IsShowMenu")
    var isShowMenu: Boolean
)
