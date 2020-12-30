package app.vtcnews.android.ui.audio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.R
import app.vtcnews.android.model.Audio.AlbumPaging
import com.squareup.picasso.Picasso

class ItemAudioAdapter(private val listAudio: List<AlbumPaging>) :
    RecyclerView.Adapter<ItemAudioAdapter.ViewHolder>() {

    var clickListen : (AlbumPaging) -> Unit = {}


    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_item_audio, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int
    {
            return listAudio.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val albumPaging: AlbumPaging = listAudio[position]
        holder.tvTitleItem.setText(albumPaging.name)
//        holder.tvEpisode.setText(albumPaging.)
        Picasso.get().load(albumPaging.image182182).fit().into(holder.ivItem)
        holder.itemAudio.setOnClickListener {
            clickListen.invoke(albumPaging)
        }

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val ivItem: ImageView = v.findViewById(R.id.ivItem)
        val tvEpisode: TextView = v.findViewById(R.id.tv_episode)
        val tvTitleItem: TextView = v.findViewById(R.id.tvTitleItem)
        val itemAudio: LinearLayout = v.findViewById(R.id.itemAudio)
        override fun onClick(p0: View?) {

        }


    }
}