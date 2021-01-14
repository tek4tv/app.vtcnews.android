package app.vtcnews.android.ui.trang_chu

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import app.vtcnews.android.R
import app.vtcnews.android.model.Audio.AlbumPaging
import app.vtcnews.android.ui.KotlinEpoxyHolder
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide

@EpoxyModelClass(layout = R.layout.audio_inhome_layout)
abstract class AudioSectionModel : EpoxyModelWithHolder<AudioSectionHodel>() {

    @EpoxyAttribute
   var  listAudio: List<AlbumPaging> = listOf()

    @EpoxyAttribute
   var clickListener : (AlbumPaging) -> Unit = {}

    @EpoxyAttribute
   var btClickListener : () -> Unit = {}
    override fun bind(holder: AudioSectionHodel) {
        super.bind(holder)
        holder.run {
            val imgs = mutableListOf<ImageView>().apply {
                add(iv0)
                add(iv1)
                add(iv2)
            }
            val txt = mutableListOf<TextView>().apply {
                add(title0)
                add(title1)
                add(title2)
            }
            imgs.forEachIndexed { index, imageView ->
                val thumbUrl = if(index == 0) listAudio[index].image360360
                else listAudio[index].image182182
                Glide.with(imageView)
                    .load(thumbUrl!!)
                    .into(imageView)
            }
            txt.forEachIndexed { index, textView ->
                textView.text = listAudio[index].name
            }

            btXemThemPD.setOnClickListener{
                btClickListener.invoke()
            }
            val listItem = mutableListOf<ConstraintLayout>().apply {
                add(item0)
                add(item1)
                add(item2)
            }
            listItem.forEachIndexed{index,item ->
                item.setOnClickListener{
                    clickListener.invoke(listAudio[index])
                }
            }



        }
    }
}

class AudioSectionHodel : KotlinEpoxyHolder() {

    val iv0 by bind<ImageView>(R.id.ivHeader)
    val iv1 by bind<ImageView>(R.id.ivAudio1)
    val iv2 by bind<ImageView>(R.id.ivAudio2)

    val title0 by bind<TextView>(R.id.tvTitle)
    val title1 by bind<TextView>(R.id.tvAudio1)
    val title2 by bind<TextView>(R.id.tvAudio2)

    val item1 by bind<ConstraintLayout>(R.id.audio1)
    val item2 by bind<ConstraintLayout>(R.id.audio2)
    val item0 by bind<ConstraintLayout>(R.id.audio0)

    val btXemThemPD by bind<AppCompatButton>(R.id.btXemThemPd)


}