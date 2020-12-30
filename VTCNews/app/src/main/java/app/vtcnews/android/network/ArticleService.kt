package app.vtcnews.android.network

import app.vtcnews.android.model.Article
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
        @Path("categoryId") categoryId: Int
    ): Response<List<Article>>

    @GET("home/news/trending/{page}")
    suspend fun getTrendingArticles(@Path("page") page : Int) : Response<List<Article>>
}