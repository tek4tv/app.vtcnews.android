package app.vtcnews.android.ui.video

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.R
import app.vtcnews.android.model.Video
import app.vtcnews.android.ui.trang_chu_sub_section.getDateDiff
import com.squareup.picasso.Picasso

class VideoHomeAdapter(private val listVideo: List<Video>) :
    RecyclerView.Adapter<VideoHomeAdapter.ViewHolder>() {
    var clickListen: (Video) -> Unit = {}

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val ivVideo = v.findViewById<ImageView>(R.id.ivVideo)
        val tvTitleVideo = v.findViewById<TextView>(R.id.tvTitleVideo)
        val tvTimeDiffVideo = v.findViewById<TextView>(R.id.tvTimeDiff)
        val tvCategoryVideo = v.findViewById<TextView>(R.id.tvCategoryVideo)
        val itemVideo = v.findViewById<LinearLayout>(R.id.itemVideo)
        val tvChannel = v.findViewById<TextView>(R.id.tvChannel)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.video_item_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = listVideo.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = listVideo[position]
        holder.tvChannel.visibility = View.GONE
        holder.tvTitleVideo.text = video.title
        if (video.image430 != null) {
            Picasso.get().load(video.image430).fit().noFade().centerCrop().into(holder.ivVideo)
        } else {
            Picasso.get().load(video.image169_Large).fit().noFade().centerCrop()
                .into(holder.ivVideo)
        }

        holder.tvCategoryVideo.text = video.categoryName
        holder.tvTimeDiffVideo.text = getDateDiff(
            video.publishedDate,
            holder.tvTimeDiffVideo.context.resources
        )
        holder.itemVideo.setOnClickListener(View.OnClickListener {
            clickListen.invoke(video)
        })

    }
}