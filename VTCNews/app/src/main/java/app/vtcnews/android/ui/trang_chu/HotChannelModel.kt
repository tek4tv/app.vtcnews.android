package app.vtcnews.android.ui.trang_chu

import android.widget.ImageView
import android.widget.TextView
import app.vtcnews.android.R
import app.vtcnews.android.model.HotChannel
import app.vtcnews.android.ui.KotlinEpoxyHolder
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.squareup.picasso.Picasso

@EpoxyModelClass(layout = R.layout.chuyen_de_nong_layout)
abstract class HotChannelModel : EpoxyModelWithHolder<HotChannelHolder>() {
    @EpoxyAttribute
    lateinit var channels : Pair<HotChannel, HotChannel>

    override fun bind(holder: HotChannelHolder) {
        super.bind(holder)

        Picasso.get()
            .load(channels.first.image169)
            .into(holder.img1)
        holder.txtTitle1.text = channels.first.title

        Picasso.get()
            .load(channels.second.image169)
            .into(holder.img2)
        holder.txtTitle2.text = channels.second.title
    }
}

class HotChannelHolder : KotlinEpoxyHolder()
{
    val img1 by bind<ImageView>(R.id.img_hot_channel1)
    val img2 by bind<ImageView>(R.id.img_hot_channel2)

    val txtTitle1 by bind<TextView>(R.id.txt_hot_channel_title1)
    val txtTitle2 by bind<TextView>(R.id.txt_hot_channel_title2)
}