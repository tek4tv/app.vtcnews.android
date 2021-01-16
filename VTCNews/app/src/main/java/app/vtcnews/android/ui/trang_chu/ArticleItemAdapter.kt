package app.vtcnews.android.ui.trang_chu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.R
import app.vtcnews.android.model.Article.Article
import app.vtcnews.android.ui.trang_chu_sub_section.getDateDiff
import com.squareup.picasso.Picasso

class ArticleItemAdapter(private val listArticle: List<Article>) :
    RecyclerView.Adapter<ArticleItemAdapter.ArticleItemViewHolder>() {
    class ArticleItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val img = v.findViewById(R.id.img_hot_article) as ImageView
        val txtTitle = v.findViewById(R.id.txt_hot_article_title) as TextView
        val imgMedia = v.findViewById(R.id.img_hot_article_media_type) as ImageView
        val txtDate = v.findViewById(R.id.txt_hot_article_date) as TextView
        val txtCategory = v.findViewById(R.id.txt_hot_article_category) as TextView
        val itemArticle = v.findViewById(R.id.article_item_root) as ConstraintLayout
    }

    var articleClickListener: (Article) -> Unit = {}
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.artice_item, parent, false)
        return ArticleItemViewHolder(itemView)

    }

    override fun getItemCount(): Int = listArticle.size

    override fun onBindViewHolder(holder: ArticleItemViewHolder, position: Int) {
        val article = listArticle[position]
        Picasso.get().load(article.imageCrop).into(holder.img)

        holder.txtTitle.text = article.title

        holder.itemArticle.setOnClickListener {
            articleClickListener(article)}


            when {
                article.isPhotoArticle == 1L -> {
                    holder.imgMedia.visibility = View.VISIBLE
                    holder.imgMedia.setImageResource(R.drawable.ic_camera_24)
                }
                article.isVideoArticle == 1L -> {
                    holder.imgMedia.visibility = View.VISIBLE
                    holder.imgMedia.setImageResource(R.drawable.ic_videocam_24)
                }
                else -> {
                    holder.imgMedia.visibility = View.GONE
                }
            }

            holder.txtCategory.text = article.categoryName

            holder.txtDate.text =
                getDateDiff(
                    article.publishedDate!!,
                    holder.txtDate.context.applicationContext.resources
                )

        }
    }


