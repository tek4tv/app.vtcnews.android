package app.vtcnews.android.network

import app.vtcnews.android.model.Article
import app.vtcnews.android.model.HotChannel
import retrofit2.Response
import retrofit2.http.GET

interface ArticleService {
    @GET("home/news/getchannelhot")
    suspend fun getHotChannels() : Response<List<HotChannel>>

    @GET("home/news/getarticlehot")
    suspend fun getHotArticles() : Response<List<Article>>

    @GET("home/news/GetArticleSuggestionHome")
    suspend fun getArticleSuggestionHome() : Response<List<Article>>
}