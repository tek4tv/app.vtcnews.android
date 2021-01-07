package app.vtcnews.android.network

import app.vtcnews.android.model.comment.Comment
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CommentService {
    @GET("home/news/comment/GetComment/{Id}/{page}")
    suspend fun getComment(@Path("Id")id:Long,@Path("page")page:Int): Response<Comment>
}