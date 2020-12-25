package app.vtcnews.android.repos

import app.vtcnews.android.model.HotChannel
import app.vtcnews.android.network.ArticleService
import app.vtcnews.android.network.Resource
import app.vtcnews.android.network.performNetworkCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepo @Inject constructor(
    private val articleService: ArticleService
) {


    suspend fun getHotChannels() : Resource<List<HotChannel>>
    {
        return performNetworkCall { articleService.getHotChannels() }
    }
}