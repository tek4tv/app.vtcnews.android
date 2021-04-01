package app.vtcnews.android.network

import app.vtcnews.android.model.comment.Comment
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentService {
    @GET("home/news/comment/GetComment/{Id}/{page}")
    suspend fun getComment(@Path("Id") id: Long, @Path("page") page: Int): Response<Comment>

    @POST("home/comment/PostComment?_username={username}&_email={email}&_idArticle={articleid}&_idComment=0&_value={value}")
    suspend fun postComment(
        @Path("username") username: String,
        @Path("email") email: String,
        @Path("articleid") articleId: String,
        @Path("value") value: String
    ): Response<String>

    @POST("home/comment/PostComment?")
    suspend fun postCommentQuery(
        @Query("_username") username: String,
        @Query("_email") email: String,
        @Query("_idArticle") articleId: String,
        @Query("_idComment") idComment: String,
        @Query("_value") value: String
    ): Response<Long>
}
