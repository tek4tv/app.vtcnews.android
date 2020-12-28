package app.vtcnews.android.ui.trang_chu

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.vtcnews.android.R
import app.vtcnews.android.model.HotArticle
import app.vtcnews.android.ui.KotlinEpoxyHolder
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

@EpoxyModelClass(layout = R.layout.hot_artice_header)
abstract class HotArticleHeaderModel : EpoxyModelWithHolder<HotArticleViewHolder>() {
    @EpoxyAttribute
    lateinit var hotArticle: HotArticle

    override fun bind(holder: HotArticleViewHolder) {
        val picasso = Picasso.get()
        holder.apply {
            picasso
                .load(hotArticle.image169_Large)
                .into(img)

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
            txtDate.text = getDateDiff(txtDate.context.applicationContext.resources)

        }
    }

    private fun getDateDiff(resource: Resources): String {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val date = dateFormatter.parse(hotArticle.publishedDate)
        val diff = abs(Date().time - date!!.time)
        val diffMinutes: Long = diff / (60 * 1000)
        val diffHours: Long = diff / (60 * 60 * 1000)
        val diffDays: Long = diff / (60 * 60 * 1000 * 24)
        val diffMonths = (diff / (60 * 60 * 1000 * 24 * 30.41666666))
        val diffYears: Long = diff / (60.toLong() * 60 * 1000 * 24 * 365)


        return when {
            diffYears >= 1 -> resource.getString(R.string.years_before, diffYears)
            diffMonths >= 1 -> resource.getString(R.string.months_before, diffMonths)
            diffDays >= 1 -> resource.getString(R.string.days_before, diffDays)
            diffHours >= 1 -> resource.getString(R.string.hours_before, diffHours)
            diffMinutes >= 1 -> resource.getString(R.string.minutes_before, diffMinutes)
            else -> resource.getString(R.string.minutes_before, 1)
        }
    }
}

class HotArticleViewHolder : KotlinEpoxyHolder() {
    val img by bind<ImageView>(R.id.img_hot_article)
    val txtTitle by bind<TextView>(R.id.txt_hot_article_title)
    val imgMedia by bind<ImageView>(R.id.img_hot_article_media_type)
    val txtDate by bind<TextView>(R.id.txt_hot_article_date)
    val txtCategory by bind<TextView>(R.id.txt_hot_article_category)
}