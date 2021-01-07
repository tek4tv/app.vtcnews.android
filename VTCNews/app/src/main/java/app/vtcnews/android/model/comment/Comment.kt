package app.vtcnews.android.model.comment

import com.squareup.moshi.Json
import java.io.Serializable

data class Comment (
    @Json(name = "CurrentPage")
    val currentPage: Int,

    @Json(name = "PageSize")
    val pageSize: Int,

    @Json(name = "TotalRecord")
    val totalRecord: Int,

    @Json(name = "TotalAllRecord")
    val totalAllRecord: Int,

    @Json(name = "TotalPage")
    val totalPage: Int,

    @Json(name = "PageNumber")
    val pageNumber: Int,

    @Json(name = "Items")
    val items: List<CommentItem>
)
data class CommentItem(
    @Json(name = "Id")
    val id: Long,

    @Json(name = "CustomerName")
    val customerName: String,

    @Json(name = "Content")
    val content: String,

    @Json(name = "CreatedDate")
    val createdDate: String,

    @Json(name = "CreatedDateFormatText")
    val createdDateFormatText: Any? = null,

    @Json(name = "Avatar")
    val avatar: String,

    @Json(name = "ParentId")
    val parentID: Int,

    @Json(name = "AccountId")
    val accountID: Int,

    @Json(name = "CountChild")
    val countChild: Int,

    @Json(name = "CountLike")
    val countLike: Int
) : Serializable