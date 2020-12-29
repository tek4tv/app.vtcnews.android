package app.vtcnews.android.ui.trang_chu

import app.vtcnews.android.model.Article
import app.vtcnews.android.model.TrangChuData
import com.airbnb.epoxy.TypedEpoxyController

class TrangChuController : TypedEpoxyController<TrangChuData>() {
    override fun buildModels(data: TrangChuData?) {

        if (data == null || data.hotArticles.isEmpty() || data.hotChannels.isEmpty()) return

        hotArticleHeader {
            id("hotArticleHeader")
            hotArticle(data.hotArticles.first())
        }

        data.hotArticles.drop(1).forEach { hotArticle ->
            article {
                id(hotArticle.id)
                article(hotArticle)
            }
        }

        hotChannel {
            id("hotChannels")
            channels(Pair(data.hotChannels[0], data.hotChannels[1]))
        }

        var firstArticles = listOf<Article>()
        var remainArticle = data.articlesSuggestionsHome
        if (data.articlesSuggestionsHome.isNotEmpty() && data.articlesSuggestionsHome.size > 6) {
            firstArticles = data.articlesSuggestionsHome.take(6)
            remainArticle = data.articlesSuggestionsHome.drop(6)
        }

        if (firstArticles.isNotEmpty()) {
            for (i in 0..2) {
                article {
                    id(firstArticles[i].id)
                    article(firstArticles[i])
                }
            }
        }

        if (data.videos.isNotEmpty())
            videoSection {
                id("videos")
                videoList(data.videos)
            }

        if (firstArticles.isNotEmpty()) {
            for (i in 3..5) {
                article {
                    id(firstArticles[i].id)
                    article(firstArticles[i])
                }
            }
        }

        remainArticle.forEach {
            article {
                id(it.id)
                article(it)
            }
        }
    }
}