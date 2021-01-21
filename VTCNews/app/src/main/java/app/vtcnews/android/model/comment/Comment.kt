package app.vtcnews.android.model.comment

import com.squareup.moshi.Json
import java.io.Serializable

data class Comment (
    @Json(name = "CurrentPage")
    val currentPage: Int? = null,

    @Json(name = "PageSize")
    val pageSize: Int? = null,

    @Json(name = "TotalRecord")
    val totalRecord: Int? = null,

    @Json(name = "TotalAllRecord")
    val totalAllRecord: Int? = null,

    @Json(name = "TotalPage")
    val totalPage: Int? = null,

    @Json(name = "PageNumber")
    val pageNumber: Int? = null,

    @Json(name = "Items")
    val items: List<CommentItem>
)
data class CommentItem(
    @Json(name = "Id")
    var id: Long,

    @Json(name = "CustomerName")
    val customerName: String? = null,

    @Json(name = "Content")
    val content: String? = null,

    @Json(name = "CreatedDate")
    val createdDate: String,

    @Json(name = "CreatedDateFormatText")
    val createdDateFormatText: Any? = null,

    @Json(name = "Avatar")
    val avatar: String? = null,

    @Json(name = "ParentId")
    val parentID: Int,

    @Json(name = "AccountId")
    val accountID: Int? = null,

    @Json(name = "CountChild")
    val countChild: Int,

    @Json(name = "CountLike")
    val countLike: Int
) : Serializable