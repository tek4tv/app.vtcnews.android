package app.vtcnews.android.ui.trang_chu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.databinding.VideoItemLayoutBinding
import app.vtcnews.android.model.HotChannel
import app.vtcnews.android.ui.trang_chu_sub_section.getDateDiff
import com.squareup.picasso.Picasso

class AllChannelAdapter : PagingDataAdapter<HotChannel, AllChannelHoldel>(AllChannelDiffCallback) {
    var clickItemChannel: (HotChannel) -> Unit = {}


    override fun onBindViewHolder(holder: AllChannelHoldel, position: Int) {
        holder.bind(getItem(position)!!, clickItemChannel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllChannelHoldel {
        return AllChannelHoldel.from(parent)
    }
}

object AllChannelDiffCallback : DiffUtil.ItemCallback<HotChannel>() {
    override fun areItemsTheSame(oldItem: HotChannel, newItem: HotChannel): Boolean =
        oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: HotChannel, newItem: HotChannel): Boolean =
        oldItem == newItem

}

class AllChannelHoldel(val binding: VideoItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val imgChannel = binding.ivVideo
    val titleChannel = binding.tvTitleVideo
    val tvTimeDiff = binding.tvTimeDiff
    val icPlay = binding.icPlay
    val itemChannel = binding.itemVideo
    fun bind(hotChannel: HotChannel, clickItemChannel: (HotChannel) -> Unit) {
        icPlay.visibility = View.GONE
        Picasso.get().load(hotChannel.image169).fit().noFade().centerCrop().into(imgChannel)
        titleChannel.text = hotChannel.title
        tvTimeDiff.text = hotChannel.createdDate?.let { getDateDiff(it, tvTimeDiff.resources) }
        itemChannel.setOnClickListener {
            clickItemChannel.invoke(hotChannel)
        }
    }

    companion object {
        fun from(parent: ViewGroup): AllChannelHoldel {
            val binding =
                VideoItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return AllChannelHoldel(binding)
        }
    }
}
