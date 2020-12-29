package app.vtcnews.android.ui.trang_chu

import android.content.res.Resources
import android.view.View
import app.vtcnews.android.R
import app.vtcnews.android.model.Article
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

@EpoxyModelClass(layout = R.layout.hot_artice)
abstract class ArticleModel : EpoxyModelWithHolder<HotArticleViewHolder>() {
    @EpoxyAttribute
    lateinit var article: Article

    override fun bind(holder: HotArticleViewHolder) {
        holder.apply {
            Glide.with(img)
                .load(article.image169)
                .into(img)

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

            txtDate.text = getDateDiff(txtDate.context.applicationContext.resources)
        }
    }

    private fun getDateDiff(resource: Resources): String {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val date = dateFormatter.parse(article.publishedDate)
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
