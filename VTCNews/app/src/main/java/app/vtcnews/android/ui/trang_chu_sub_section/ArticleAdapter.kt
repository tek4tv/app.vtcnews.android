package app.vtcnews.android.ui.trang_chu_sub_section

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.R
import app.vtcnews.android.databinding.ArticeItemBinding
import app.vtcnews.android.model.Article
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class ArticleAdapter() : PagingDataAdapter<Article, ArticleHolder>(ArticleDiffCallback) {
    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        return ArticleHolder.from(parent)
    }
}

object ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem == newItem
}

class ArticleHolder(private val binding: ArticeItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val img = binding.imgHotArticle
    private val txtTitle = binding.txtHotArticleTitle
    private val imgMedia = binding.imgHotArticleMediaType
    private val txtDate = binding.txtHotArticleDate
    private val txtCategory = binding.txtHotArticleCategory

    fun bind(article: Article) {
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

        txtDate.text = getDateDiff(article.publishedDate, txtDate.context.applicationContext.resources)
    }

    companion object {
        fun from(parent: ViewGroup): ArticleHolder {
            val binding =
                ArticeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return ArticleHolder(binding)
        }
    }
}

