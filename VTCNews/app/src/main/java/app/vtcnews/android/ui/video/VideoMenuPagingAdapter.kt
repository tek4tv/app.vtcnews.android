package app.vtcnews.android.ui.video

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.databinding.VideoItemLayoutBinding
import app.vtcnews.android.model.Video
import app.vtcnews.android.ui.trang_chu_sub_section.getDateDiff
import com.squareup.picasso.Picasso

class VideoMenuPagingAdapter :
    PagingDataAdapter<Video, VideoMenuPagingHoldel>(VideoPagingDiffCallback) {
    var clickItemChannel: (Video) -> Unit = {}


    override fun onBindViewHolder(holder: VideoMenuPagingHoldel, position: Int) {
        holder.bind(getItem(position)!!, clickItemChannel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoMenuPagingHoldel {
        return VideoMenuPagingHoldel.from(parent)
    }
}

object VideoPagingDiffCallback : DiffUtil.ItemCallback<Video>() {

    override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean =
        oldItem == newItem

}

class VideoMenuPagingHoldel(val binding: VideoItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val imgChannel = binding.ivVideo
    val titleChannel = binding.tvTitleVideo
    val tvTimeDiff = binding.tvTimeDiff
    val icPlay = binding.icPlay
    val itemChannel = binding.itemVideo
    val tvChannel = binding.tvChannel
    fun bind(video: Video, clickItemChannel: (Video) -> Unit) {
        icPlay.visibility = View.VISIBLE
        tvChannel.visibility = View.GONE
        Picasso.get().load(video.image430).fit().centerCrop().noFade().into(imgChannel)
        titleChannel.text = video.title
        tvTimeDiff.text = video.publishedDate?.let { getDateDiff(it, tvTimeDiff.resources) }
        itemChannel.setOnClickListener {
            clickItemChannel.invoke(video)
        }
    }

    companion object {
        fun from(parent: ViewGroup): VideoMenuPagingHoldel {
            val binding =
                VideoItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return VideoMenuPagingHoldel(binding)
        }
    }
}
