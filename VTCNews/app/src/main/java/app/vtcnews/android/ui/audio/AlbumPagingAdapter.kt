package app.vtcnews.android.ui.audio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.R
import app.vtcnews.android.model.Audio.AlbumPaging
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class AlbumPagingAdapter :
    PagingDataAdapter<AlbumPaging, RecyclerView.ViewHolder>(AlbumPagingDiffCallback) {
    companion object {
        private const val TYPE_Normal = 1
        private const val TYPE_Lager = 2
    }

    var clickListen: (AlbumPaging, Int) -> Unit = { albumPaging: AlbumPaging, i: Int -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_Normal) {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_item_audio, parent, false)
            AlbumPagingHoldel(itemView)
        } else {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.audio_item_header, parent, false)
            AlbumPagingHoldelHeader(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_Normal) {
            holder as AlbumPagingHoldel
            val albumPaging = getItem(position)
            holder.tvTitleItem.text = albumPaging?.name
            Picasso.get().load(albumPaging?.image182182).fit().noFade().into(holder.ivItem)
            holder.itemAudio.setOnClickListener {
                if (albumPaging != null) {
                    clickListen.invoke(albumPaging, position)
                }
            }
        } else {
            holder as AlbumPagingHoldelHeader
            val albumPaging = getItem(position)
            holder.tvTitleItem.text = albumPaging?.name
            Picasso.get().load(albumPaging?.image182182).fit().noFade().into(holder.ivItem)
            holder.itemAudio.setOnClickListener {
                if (albumPaging != null) {
                    clickListen.invoke(albumPaging, position)
                }
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_Lager
        } else {
            TYPE_Normal
        }
    }
}

object AlbumPagingDiffCallback : DiffUtil.ItemCallback<AlbumPaging>() {
    override fun areItemsTheSame(oldItem: AlbumPaging, newItem: AlbumPaging): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: AlbumPaging, newItem: AlbumPaging): Boolean =
        oldItem == newItem

}


class AlbumPagingHoldel(v: View) : RecyclerView.ViewHolder(v) {
    val ivItem: ImageView = v.findViewById(R.id.ivItem)
    val tvTitleItem: TextView = v.findViewById(R.id.tvTitleItem)
    val itemAudio: MaterialCardView = v.findViewById(R.id.itemAudio)
}

class AlbumPagingHoldelHeader(v: View) : RecyclerView.ViewHolder(v) {
    val ivItem: ImageView = v.findViewById(R.id.ivAudioHeader)
    val tvTitleItem: TextView = v.findViewById(R.id.tvTitleHeader)
    val itemAudio: MaterialCardView = v.findViewById(R.id.itemHeader)
}
