package app.vtcnews.android.ui.trang_chu

import app.vtcnews.android.model.Article
import app.vtcnews.android.model.Audio.AlbumPaging
import app.vtcnews.android.model.HotChannel
import app.vtcnews.android.model.TrangChuData
import app.vtcnews.android.model.Video
import com.airbnb.epoxy.TypedEpoxyController

class TrangChuController : TypedEpoxyController<TrangChuData>() {
    var articleClickListener: (Article) -> Unit = {}
    var hotChannelClickListener: (HotChannel) -> Unit = {}
    var hotChannelHeaderClickListener: () -> Unit = {}
    var videoClickListener: (Video) -> Unit = {}
    var btXemThemVideoClickListener: () -> Unit = {}

    var audioClickListener: (AlbumPaging) -> Unit = {}
    var btXemThemAudioClickListener: () -> Unit = {}

    override fun buildModels(data: TrangChuData?) {

        if (data == null || data.hotArticles.isEmpty() || data.hotChannels.isEmpty()) return

        hotArticleHeader {
            id("hotArticleHeader")
            hotArticle(data.hotArticles.first())
            articleClickListener(articleClickListener)
        }

        data.hotArticles.drop(1).forEach { hotArticle ->
            article {
                id(hotArticle.id)
                article(hotArticle)
                articleClickListener(articleClickListener)
            }
        }

        hotChannel {
            id("hotChannels")
            channels(Pair(data.hotChannels[0], data.hotChannels[1]))
            onClickListener(hotChannelClickListener)
            onHeaderClick(hotChannelHeaderClickListener)
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
                    articleClickListener(articleClickListener)
                }
            }
        }

        if (data.videos.isNotEmpty())
            videoSection {
                id("videos")
                videoList(data.videos)
                clickListener(videoClickListener)
                btClickListener(btXemThemVideoClickListener)
            }

        if (firstArticles.isNotEmpty()) {
            for (i in 3..5) {
                article {
                    id(firstArticles[i].id)
                    article(firstArticles[i])
                    articleClickListener(articleClickListener)
                }
            }
        }

        if (data.audio.isNotEmpty()) {
            audioSection {
                id("audioSection")
                listAudio(data.audio)
                clickListener(audioClickListener)
                btClickListener(btXemThemAudioClickListener)
            }
        }

        remainArticle.forEach {
            article {
                id(it.id)
                article(it)
                articleClickListener(articleClickListener)
            }
        }
    }
}