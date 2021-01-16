package app.vtcnews.android.ui.video

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.R
import app.vtcnews.android.model.Article.Article
import com.squareup.picasso.Picasso

class VideoItemAdapter(private val listArticle: List<Article>) :
    RecyclerView.Adapter<VideoItemAdapter.ViewHolder>() {

    var clickListen: (Article) -> Unit = {}


    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.video_item_next_video, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listArticle.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = listArticle[position]
        holder.icItem.setImageResource(R.drawable.icroundplay)
        holder.tvTitleItem.setText(article.title)
        Picasso.get().load(article.image169).fit().into(holder.ivVideo)
        holder.itemAudio.setOnClickListener {
            clickListen.invoke(article)
        }

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val ivVideo: ImageView = v.findViewById(R.id.ivVideoNext)
        val tvTitleItem: TextView = v.findViewById(R.id.tvTitleNext)
        val itemAudio: LinearLayout = v.findViewById(R.id.itemVideoNext)
        val icItem: ImageView = v.findViewById(R.id.icItem)


    }
}