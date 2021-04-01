package app.vtcnews.android.ui.article_detail_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.R
import app.vtcnews.android.databinding.ArticeItemBinding
import app.vtcnews.android.model.Article
import app.vtcnews.android.ui.trang_chu_sub_section.getDateDiff
import com.squareup.picasso.Picasso


class ListByTagAdapter : PagingDataAdapter<Article, ArticleHolder>(ArticleDiffCallback) {

    var articleClickListener: (Article) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        return ArticleHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        holder.bind(getItem(position)!!, articleClickListener, position)
    }
}

class ArticleHolder(private val binding: ArticeItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val img = binding.imgHotArticle
    private val txtTitle = binding.txtHotArticleTitle
    private val imgMedia = binding.imgHotArticleMediaType
    private val txtDate = binding.txtHotArticleDate
    private val txtCategory = binding.txtHotArticleCategory

    fun bind(article: Article, articleClickListener: (Article) -> Unit, position: Int) {
        Picasso.get().load(article.imageResize).fit().centerCrop().noFade().into(img)

        txtTitle.text = article.title

        binding.root.setOnClickListener {
            articleClickListener(article)
        }

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
    }

    companion object {
        fun from(parent: ViewGroup): ArticleHolder {
            val binding =
                ArticeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ArticleHolder(binding)
        }
    }
}

object ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem == newItem
}

