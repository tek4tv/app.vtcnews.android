package app.vtcnews.android.ui.audio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.R
import app.vtcnews.android.model.Audio.AlbumPaging
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class ItemAudioAdapter(private val listAudio: List<AlbumPaging>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_Normal = 1
        private const val TYPE_Lager = 2
    }

    var clickListen: (AlbumPaging, Int) -> Unit = { albumPaging: AlbumPaging, i: Int -> }


    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_Normal) {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_item_audio, parent, false)
            return ViewHolder(itemView)
        } else {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.audio_item_header, parent, false)
            return ViewHolderHeader(itemView)
        }
    }

    override fun getItemCount(): Int {
        return listAudio.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_Lager
        } else {
            TYPE_Normal
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_Normal) {
            holder as ViewHolder
            val albumPaging: AlbumPaging = listAudio[position]
            holder.tvTitleItem.text = albumPaging.name
            Picasso.get().load(albumPaging.image182182).fit().noFade().into(holder.ivItem)
            holder.itemAudio.setOnClickListener {
                clickListen.invoke(albumPaging, position)
            }
        } else {
            holder as ViewHolderHeader
            val albumPaging: AlbumPaging = listAudio[position]
            holder.tvTitleItem.text = albumPaging.name
            Picasso.get().load(albumPaging.image182182).fit().noFade().into(holder.ivItem)
            holder.itemAudio.setOnClickListener {
                clickListen.invoke(albumPaging, position)
            }
        }
    }
}

class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val ivItem: ImageView = v.findViewById(R.id.ivItem)
    val tvTitleItem: TextView = v.findViewById(R.id.tvTitleItem)
    val itemAudio: MaterialCardView = v.findViewById(R.id.itemAudio)
}

class ViewHolderHeader(v: View) : RecyclerView.ViewHolder(v) {
    val ivItem: ImageView = v.findViewById(R.id.ivAudioHeader)
    val tvTitleItem: TextView = v.findViewById(R.id.tvTitleHeader)
    val itemAudio: MaterialCardView = v.findViewById(R.id.itemHeader)
}

class ItemAudioNoHeaderAdapter(private val listAudio: List<AlbumPaging>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var clickListen: (AlbumPaging, Int) -> Unit = { albumPaging: AlbumPaging, i: Int -> }


    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item_audio, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listAudio.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        val albumPaging: AlbumPaging = listAudio[position]
        holder.tvTitleItem.text = albumPaging.name
        Picasso.get().load(albumPaging.image182182).fit().noFade().into(holder.ivItem)
        holder.itemAudio.setOnClickListener {
            clickListen.invoke(albumPaging, position)
        }

    }
}
