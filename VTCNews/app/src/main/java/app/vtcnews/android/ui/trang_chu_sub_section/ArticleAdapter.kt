package app.vtcnews.android.ui.trang_chu_sub_section

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.R
import app.vtcnews.android.databinding.ArticeItemBinding
import app.vtcnews.android.databinding.HotArticeHeaderBinding
import app.vtcnews.android.model.Article
import com.squareup.picasso.Picasso


class ArticleAdapter : PagingDataAdapter<Article, RecyclerView.ViewHolder>(ArticleDiffCallback) {
    companion object {
        private val TYPE_Normal = 1
        private val TYPE_Lager = 2
    }

    var articleClickListener: (Article) -> Unit = {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_Normal) {
            (holder as ArticleHolder).bind(getItem(position)!!, articleClickListener)

        }
        else
        {
            (holder as ArticleLagerHoder).bind(getItem(position)!!, articleClickListener)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_Normal) {
            return ArticleHolder.from(parent)
        } else {
            return ArticleLagerHoder.from(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {

//        if (position == 0 || position == 5 || position == 12) {
//            return TYPE_Lager
//
//        } else {
            return TYPE_Normal
//        }
    }

}

class ArticleHolder(private val binding: ArticeItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val img = binding.imgHotArticle
    private val txtTitle = binding.txtHotArticleTitle
    private val imgMedia = binding.imgHotArticleMediaType
    private val txtDate = binding.txtHotArticleDate
    private val txtCategory = binding.txtHotArticleCategory

    fun bind(article: Article, articleClickListener: (Article) -> Unit) {
        Picasso.get().load(article.image169).into(img)

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

class ArticleLagerHoder(private val binding: HotArticeHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val img = binding.imgHotArticle
    private val txtTitle = binding.txtHotArticleTitle
    private val imgMedia = binding.imgHotArticleMediaType
    private val txtDate = binding.txtHotArticleDate
    private val txtCategory = binding.txtHotArticleCategory
    fun bind(article: Article, articleClickListener: (Article) -> Unit) {
        Picasso.get().load(article.image169).into(img)

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
        fun from(parent: ViewGroup): ArticleLagerHoder {
            val binding =
                HotArticeHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return ArticleLagerHoder(binding)
        }
    }
}

object ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem == newItem
}

