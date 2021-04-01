package app.vtcnews.android.ui.audio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.R
import app.vtcnews.android.model.Audio.ListPodcast
import com.squareup.picasso.Picasso

class ListChapterAdapter(private val listChapter: List<ListPodcast>, val authur: String) :
    RecyclerView.Adapter<ListChapterAdapter.ViewHolder>() {

    var currImg: ImageView? = null

    var clickListen: (ListPodcast, Int) -> Unit = { listPodcast: ListPodcast, i: Int -> }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val ivChapter: ImageView = v.findViewById(R.id.ivChapter)
        val ivIsPlayChapter: ImageView = v.findViewById(R.id.ivIsPlayChapter)
        val tvTitleChapter: TextView = v.findViewById(R.id.tvTitleChapter)
        val tvAuthur: TextView = v.findViewById(R.id.tvAuthur)
        val btShare: ImageView = v.findViewById(R.id.btShare)
        val itemChapter = v.findViewById(R.id.itemChapter) as LinearLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item_listpodcast, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = listChapter.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val audio: ListPodcast = listChapter[position]
        holder.tvTitleChapter.text = audio.name
        Picasso.get().load(audio.image182182).fit().noFade().into(holder.ivChapter)
        holder.ivIsPlayChapter.isVisible = false
        if (authur != "") {
            holder.tvAuthur.text = authur
        } else {
            holder.tvAuthur.visibility = View.GONE
        }
        holder.itemChapter.setOnClickListener(View.OnClickListener {
            clickListen.invoke(audio, position)
            currImg?.isVisible = false
            holder.ivIsPlayChapter.isVisible = true
            currImg = holder.ivIsPlayChapter

        })


    }
}