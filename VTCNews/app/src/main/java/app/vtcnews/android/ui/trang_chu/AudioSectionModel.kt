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
import com.squareup.picasso.Picasso

@EpoxyModelClass(layout = R.layout.audio_inhome_layout)
abstract class AudioSectionModel : EpoxyModelWithHolder<AudioSectionHodel>() {

//    @EpoxyAttribute
//    var listAudio: List<AlbumPaging> = listOf()

    @EpoxyAttribute
    var listAudioPd: List<AlbumPaging> = listOf()

    @EpoxyAttribute
    var listAudioMusic: List<AlbumPaging> = listOf()

    @EpoxyAttribute
    var listAudioBook: List<AlbumPaging> = listOf()

    @EpoxyAttribute
    var clickListener: (AlbumPaging) -> Unit = {}

    @EpoxyAttribute
    var btClickListener: () -> Unit = {}
    override fun bind(holder: AudioSectionHodel) {
        super.bind(holder)
        holder.run {

            //audio0
            title0.text = listAudioBook[0].name
            Picasso.get().load(listAudioBook[0].image360360).into(iv0)
            item0.setOnClickListener {
                clickListener.invoke(listAudioBook[0])
            }
            //audio1
            title1.text = listAudioMusic[2].name
            Picasso.get().load(listAudioMusic[2].image182182).into(iv1)
            item1.setOnClickListener {
                clickListener.invoke(listAudioMusic[2])
            }
            //audio2
            title2.text = listAudioPd[1].name
            Picasso.get().load(listAudioPd[1].image182182).into(iv2)
            item2.setOnClickListener {
                clickListener.invoke(listAudioPd[1])
            }
            //audio3
            title3.text = listAudioBook[2].name
            Picasso.get().load(listAudioBook[2].image182182).into(iv3)
            item3.setOnClickListener {
                clickListener.invoke(listAudioBook[2])
            }

            btXemThemPD.setOnClickListener {
                btClickListener.invoke()
            }
//            val listItem = mutableListOf<ConstraintLayout>().apply {
//                add(item0)
//                add(item1)
//                add(item2)
//                add(item3)
//            }
//            listItem.forEachIndexed { index, item ->
//                item.setOnClickListener {
//                    clickListener.invoke(listAudio[index])
//                }
//            }


        }
    }
}

class AudioSectionHodel : KotlinEpoxyHolder() {

    val iv0 by bind<ImageView>(R.id.ivHeader)
    val iv1 by bind<ImageView>(R.id.ivAudio1)
    val iv2 by bind<ImageView>(R.id.ivAudio2)
    val iv3 by bind<ImageView>(R.id.ivAudio3)

    val title0 by bind<TextView>(R.id.tvTitle)
    val title1 by bind<TextView>(R.id.tvAudio1)
    val title2 by bind<TextView>(R.id.tvAudio2)
    val title3 by bind<TextView>(R.id.tvAudio3)

    val item1 by bind<ConstraintLayout>(R.id.audio1)
    val item2 by bind<ConstraintLayout>(R.id.audio2)
    val item0 by bind<ConstraintLayout>(R.id.audio0)
    val item3 by bind<ConstraintLayout>(R.id.audio5)

    val btXemThemPD by bind<AppCompatButton>(R.id.btXemThemPd)


}