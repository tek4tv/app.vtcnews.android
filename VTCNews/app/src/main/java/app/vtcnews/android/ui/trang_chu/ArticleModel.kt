package app.vtcnews.android.ui.trang_chu

import android.view.View
import app.vtcnews.android.R
import app.vtcnews.android.model.Article
import app.vtcnews.android.ui.trang_chu_sub_section.getDateDiff
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.squareup.picasso.Picasso

@EpoxyModelClass(layout = R.layout.artice_item)
abstract class ArticleModel : EpoxyModelWithHolder<HotArticleViewHolder>() {
    @EpoxyAttribute
    lateinit var article: Article

    @EpoxyAttribute
    var booleanEnd: Boolean = false

    @EpoxyAttribute
    lateinit var articleClickListener: (Article) -> Unit

    override fun bind(holder: HotArticleViewHolder) {
        holder.apply {

            Picasso.get().load(article.image169).into(img)

            txtTitle.text = article.title

            when {
                article.isPhotoArticle == 1L -> {
                    imgMedia.visibility = View.VISIBLE
                    imgMedia.setImageResource(R.drawable.ic_camera_24)
                }
                article.isVideoArticle == 1L -> {
                    imgMedia.visibility = View.VISIBLE
                    imgMedia.setImageResource(R.drawable.ic_videocam_24)
                }
                else -> {
                    imgMedia.visibility = View.GONE
                }
            }


            txtCategory.text = article.categoryName

            txtDate.text =
                getDateDiff(article.publishedDate!!, txtDate.context.applicationContext.resources)
            holder.root.setOnClickListener {
                articleClickListener.invoke(article)
            }
            if (booleanEnd) {
                view.visibility = View.INVISIBLE
            } else {
                view.visibility = View.VISIBLE
            }
        }
    }
}

