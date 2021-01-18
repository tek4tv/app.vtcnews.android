package app.vtcnews.android.ui.trang_chu

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.vtcnews.android.R
import app.vtcnews.android.model.Article
import app.vtcnews.android.ui.KotlinEpoxyHolder
import app.vtcnews.android.ui.trang_chu_sub_section.getDateDiff
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.squareup.picasso.Picasso

@EpoxyModelClass(layout = R.layout.hot_artice_header)
abstract class HotArticleHeaderModel : EpoxyModelWithHolder<HotArticleViewHolder>() {
    @EpoxyAttribute
    lateinit var hotArticle: Article

    @EpoxyAttribute
    lateinit var articleClickListener : (Article) -> Unit

    override fun bind(holder: HotArticleViewHolder) {
        holder.apply {

            Picasso.get().load(hotArticle.image169_Large).into(img)

            txtTitle.text = hotArticle.title

            if (hotArticle.isPhotoArticle == 1L) {
                imgMedia.visibility = View.VISIBLE
                imgMedia.setImageResource(R.drawable.ic_camera_24)
            }
            if (hotArticle.isVideoArticle == 1L) {
                imgMedia.visibility = View.VISIBLE
                imgMedia.setImageResource(R.drawable.ic_videocam_24)
            }

            txtCategory.text = hotArticle.categoryName

            //time: 2020-12-28T09:45:00
            txtDate.text = getDateDiff(hotArticle.publishedDate!!, txtDate.context.applicationContext.resources)

            holder.root.setOnClickListener {
                articleClickListener.invoke(hotArticle)
            }
        }
    }
}

class HotArticleViewHolder : KotlinEpoxyHolder() {
    val img by bind<ImageView>(R.id.img_hot_article)
    val txtTitle by bind<TextView>(R.id.txt_hot_article_title)
    val imgMedia by bind<ImageView>(R.id.img_hot_article_media_type)
    val txtDate by bind<TextView>(R.id.txt_hot_article_date)
    val txtCategory by bind<TextView>(R.id.txt_hot_article_category)
    val root by bind<View>(R.id.article_item_root)
}