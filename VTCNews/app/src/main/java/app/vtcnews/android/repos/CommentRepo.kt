package app.vtcnews.android.repos

import app.vtcnews.android.model.Audio.AlbumPaging
import app.vtcnews.android.model.comment.Comment
import app.vtcnews.android.network.CommentService
import app.vtcnews.android.network.Resource
import app.vtcnews.android.network.performNetworkCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepo @Inject constructor(
    val commentService: CommentService
) {
    suspend fun getComment(id:Long,page:Int): Resource<Comment> = performNetworkCall {
        commentService.getComment(id,page)
    }
}