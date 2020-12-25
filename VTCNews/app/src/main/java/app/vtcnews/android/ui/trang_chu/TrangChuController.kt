package app.vtcnews.android.ui.trang_chu

import app.vtcnews.android.model.TrangChuData
import com.airbnb.epoxy.TypedEpoxyController

class TrangChuController : TypedEpoxyController<TrangChuData>() {
    override fun buildModels(data: TrangChuData?) {
        hotChannel {
            id("hotChannels")
            channels(Pair(data!!.hotChannels[0], data!!.hotChannels[1]))
        }
    }
}