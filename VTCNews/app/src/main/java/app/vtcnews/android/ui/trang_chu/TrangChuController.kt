package app.vtcnews.android.ui.trang_chu

import app.vtcnews.android.model.TrangChuData
import com.airbnb.epoxy.TypedEpoxyController

class TrangChuController : TypedEpoxyController<TrangChuData>() {
    override fun buildModels(data: TrangChuData?) {

        if(data == null || data.hotArticles.isEmpty() || data.hotChannels.isEmpty()) return

        hotArticleHeader {
            id("hotArticleHeader")
            hotArticle(data.hotArticles.first())
        }

        data.hotArticles.drop(1).forEach { hotArticle ->
            hotArticle {
                id(hotArticle.id)
                hotArticle(hotArticle)
            }
        }

        hotChannel {
            id("hotChannels")
            channels(Pair(data.hotChannels[0], data.hotChannels[1]))
        }
    }
}