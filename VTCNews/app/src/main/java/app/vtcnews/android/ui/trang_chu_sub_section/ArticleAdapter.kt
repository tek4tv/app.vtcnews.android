package app.vtcnews.android.ui.trang_chu_sub_section

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.R
import app.vtcnews.android.databinding.ArticeItemBinding
import app.vtcnews.android.databinding.ArticleMultiImgBinding
import app.vtcnews.android.model.Article
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso


class ArticleAdapter : PagingDataAdapter<Article, RecyclerView.ViewHolder>(ArticleDiffCallback) {
    companion object {
        private const val TYPE_Normal = 1
        private const val TYPE_Lager = 2
    }

    var articleClickListener: (Article) -> Unit = {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_Normal) {
            (holder as ArticleHolder).bind(getItem(position)!!, articleClickListener, position)
        } else {
            (holder as ArticleLagerHoder).bind(getItem(position)!!, articleClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_Normal) {
            ArticleHolder.from(parent)
        } else {
            ArticleLagerHoder.from(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val article: Article = getItem(position)!!
        return if (article.listURLImages != "") {
            TYPE_Lager
        } else {
            TYPE_Normal
        }
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
        Picasso.get().load(article.image169).fit().centerCrop().noFade()
            .memoryPolicy(MemoryPolicy.NO_CACHE).into(img)

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

class ArticleLagerHoder(private val binding: ArticleMultiImgBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val img1 = binding.ivArticle1
    private val img2 = binding.ivArticle2
    private val img3 = binding.ivArticle3
    private val txtTitle = binding.txtHotArticleTitle
    private val imgMedia = binding.imgHotArticleMediaType
    private val txtDate = binding.txtHotArticleDate
    private val txtCategory = binding.txtHotArticleCategory
    fun bind(article: Article, articleClickListener: (Article) -> Unit) {
        val listUrlImg = article.listURLImagesResize
        val listUrlSplit: List<String> = listUrlImg!!.replace("[\"", "").replace("\"", "")
            .replace("\"", "").replace("\\\"]", "").split(",")

        Picasso.get().load(listUrlSplit[1]).fit().centerCrop().noFade()
            .memoryPolicy(MemoryPolicy.NO_CACHE).into(img1)
        Picasso.get().load(listUrlSplit[2]).fit().centerCrop().noFade()
            .memoryPolicy(MemoryPolicy.NO_CACHE).into(img2)
        Picasso.get().load(listUrlSplit[3]).fit().centerCrop().noFade()
            .memoryPolicy(MemoryPolicy.NO_CACHE).into(img3)

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
                ArticleMultiImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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

