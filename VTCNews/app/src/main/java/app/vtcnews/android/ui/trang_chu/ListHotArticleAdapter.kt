package app.vtcnews.android.ui.trang_chu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.R
import app.vtcnews.android.model.Article
import app.vtcnews.android.ui.trang_chu_sub_section.getDateDiff
import com.bumptech.glide.Glide

class ListHotArticleAdapter(val listArticle : List<Article>):RecyclerView.Adapter<ListHotArticleAdapter.HotArticleHolder>() {
    var onClickItem:(Article) -> Unit = {}
    class HotArticleHolder(v:View) : RecyclerView.ViewHolder(v) {
        val ivArticle = v.findViewById<ImageView>(R.id.img_hot_article)
        val icType = v.findViewById<ImageView>(R.id.img_hot_article_media_type)
        val tvTitle = v.findViewById<TextView>(R.id.txt_hot_article_title)
        val tvTimeDiff = v.findViewById<TextView>(R.id.txt_hot_article_date)
        val tvCategory = v.findViewById<TextView>(R.id.txt_hot_article_category)
        val itemArticle = v.findViewById<ConstraintLayout>(R.id.article_item_root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotArticleHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.artice_item, parent, false)
        return HotArticleHolder(itemView)
    }

    override fun onBindViewHolder(holder: HotArticleHolder, position: Int) {
        val article = listArticle[position]
        Glide.with(holder.ivArticle)
            .load(article.image169)
            .into(holder.ivArticle)

        holder.tvTitle.text = article.title

        holder.itemArticle.setOnClickListener {
            onClickItem.invoke(article)
        }

        when {
            article.isPhotoArticle == 1L -> {
                holder.icType.visibility = View.VISIBLE
                holder.icType.setImageResource(R.drawable.ic_camera_24)
            }
            article.isVideoArticle == 1L -> {
                holder.icType.visibility = View.VISIBLE
                holder.icType.setImageResource(R.drawable.ic_videocam_24)
            }
            else -> {
                holder.icType.visibility = View.GONE
            }
        }

        holder.tvCategory.text = article.categoryName

        holder.tvTimeDiff.text = getDateDiff(article.publishedDate!!, holder.tvTimeDiff.context.applicationContext.resources)
    }

    override fun getItemCount(): Int = listArticle.size
}