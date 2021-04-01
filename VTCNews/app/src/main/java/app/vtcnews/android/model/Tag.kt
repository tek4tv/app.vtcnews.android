package app.vtcnews.android.model

import com.squareup.moshi.Json

data class TagModel(
    @Json(name = "TagInfo")
    val tagInfo: TagInfo? = null,

    @Json(name = "Items")
    val items: List<Article>,

    @Json(name = "CurrentPage")
    val currentPage: Long? = null,

    @Json(name = "PageSize")
    val pageSize: Long? = null,

    @Json(name = "TotalRecord")
    val totalRecord: Long? = null,

    @Json(name = "TotalPage")
    val totalPage: Long? = null
)

data class TagInfo(
    @Json(name = "TagId")
    val tagID: Long? = null,

    @Json(name = "TagTitle")
    val tagTitle: String? = null,

    @Json(name = "TagTitleWithoutUnicode")
    val tagTitleWithoutUnicode: String? = null,

    @Json(name = "GoogleTitle")
    val googleTitle: Any? = null,

    @Json(name = "GoogleKeyWord")
    val googleKeyWord: Any? = null,

    @Json(name = "GoogleDescription")
    val googleDescription: Any? = null
)
