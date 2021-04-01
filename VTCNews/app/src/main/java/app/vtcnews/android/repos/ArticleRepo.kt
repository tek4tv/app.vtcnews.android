package app.vtcnews.android.repos

import app.vtcnews.android.model.*
import app.vtcnews.android.network.ArticleService
import app.vtcnews.android.network.Resource
import app.vtcnews.android.network.performNetworkCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepo @Inject constructor(
    private val articleService: ArticleService
) {
    var listSuggestionHome = listOf<Article>()
    var listHotArticle = listOf<Article>()
    suspend fun getHotChannels(): Resource<List<HotChannel>> =
        performNetworkCall { articleService.getHotChannels() }

    suspend fun getHotArticles(): Resource<List<Article>> {
        val res = performNetworkCall {
            articleService.getHotArticles()
        }
        if (res is Resource.Success) {
            listHotArticle = res.data
        }
        return res
    }


    suspend fun getArticleSuggestionHome(): Resource<List<Article>> {
        val res = performNetworkCall { articleService.getArticleSuggestionHome() }
        if (res is Resource.Success) {
            listSuggestionHome = res.data
        }
        return res
    }

    suspend fun getArticleByCategory(page: Int, categoryId: Long): Resource<MutableList<Article>> {
        return performNetworkCall { articleService.getArticleByCategory(page, categoryId) }
    }

    suspend fun getTrendingArticles(page: Int): Resource<List<Article>> =
        performNetworkCall { articleService.getTrendingArticles(page) }

    suspend fun getArticleDetail(articleId: Int): Resource<ArticleDetail> =
        performNetworkCall { articleService.getArticleDetail(articleId) }

    suspend fun getListChannel(page: Int): Resource<List<HotChannel>> = performNetworkCall {
        articleService.getListChannel(page)
    }

    suspend fun getChannelPaging(page: Int, id: Long): Resource<ChannelPaging> =
        performNetworkCall {
            articleService.getIndexChannelPaging(page, id)
        }

    suspend fun getListByTag(tag: String, page: Int): Resource<TagModel> = performNetworkCall {
        articleService.getListByTag(tag, page)
    }
}