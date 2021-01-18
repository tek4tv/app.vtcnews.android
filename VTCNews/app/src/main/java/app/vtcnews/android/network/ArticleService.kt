package app.vtcnews.android.network

import app.vtcnews.android.model.Article

import app.vtcnews.android.model.ArticleDetail
import app.vtcnews.android.model.ChannelPaging
import app.vtcnews.android.model.HotChannel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ArticleService {
    @GET("home/news/getchannelhot")
    suspend fun getHotChannels(): Response<List<HotChannel>>

    @GET("home/news/getarticlehot")
    suspend fun getHotArticles(): Response<List<Article>>

    @GET("home/news/GetArticleSuggestionHome")
    suspend fun getArticleSuggestionHome(): Response<List<Article>>

    @GET("home/news/ArticleCategoryPaging/{page}/{categoryId}")
    suspend fun getArticleByCategory(
        @Path("page") page: Int,
        @Path("categoryId") categoryId: Long
    ): Response<MutableList<Article>>

    @GET("home/news/trending/{page}")
    suspend fun getTrendingArticles(@Path("page") page: Int): Response<List<Article>>

    @GET("home/news/detail/{article_id}")
    suspend fun getArticleDetail(@Path("article_id") articleId: Int): Response<ArticleDetail>

    @GET("home/news/ListChannel/{page}")
    suspend fun getListChannel(@Path("page") page: Int): Response<List<HotChannel>>

    @GET("home/news/IndexChannelPaging/{page}/{id}")
    suspend fun getIndexChannelPaging(
        @Path("page") page: Int,
        @Path("id") id: Long
    ): Response<ChannelPaging>
}