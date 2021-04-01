package app.vtcnews.android.ui.trang_chu

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import app.vtcnews.android.R
import app.vtcnews.android.model.Video
import app.vtcnews.android.ui.KotlinEpoxyHolder
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.squareup.picasso.Picasso

@EpoxyModelClass(layout = R.layout.home_video_section_layout)
abstract class VideoSectionModel : EpoxyModelWithHolder<VideoSectionHolder>() {

    @EpoxyAttribute
    var videoList: List<Video> = listOf()

    @EpoxyAttribute
    var clickListener: (Video) -> Unit = {}

    @EpoxyAttribute
    var btClickListener: () -> Unit = {}

    override fun bind(holder: VideoSectionHolder) {
        super.bind(holder)

        holder.run {

            val imgs = mutableListOf<ImageView>().apply {
                add(imgVideoHeader)
                add(imgThumb1)
                add(imgThumb2)
                add(imgThumb3)
                add(imgThumb4)
            }

            val txt = mutableListOf<TextView>().apply {
                add(txtHeaderVideo)
                add(txtTitle1)
                add(txtTitle2)
                add(txtTitle3)
                add(txtTitle4)
            }
            val itemVideo = mutableListOf<ConstraintLayout>().apply {
                add(itemVideoHeader)
                add(itemVideo1)
                add(itemVideo2)
                add(itemVideo3)
                add(itemVideo4)
            }
            itemVideo.forEachIndexed { index, constraintLayout ->
                constraintLayout.setOnClickListener {
                    clickListener.invoke(videoList[index])
                }
            }

            imgs.forEachIndexed { index, imageView ->
                val thumbUrl = videoList[index].image169_Large
                Picasso.get().load(thumbUrl).fit().noFade().centerCrop().into(imageView)
            }
            btXemthem.setOnClickListener {
                btClickListener.invoke()
            }


            txt.forEachIndexed { index, textView ->
                textView.text = videoList[index].title
            }
        }
    }
}

class VideoSectionHolder : KotlinEpoxyHolder() {
    val imgVideoHeader by bind<ImageView>(R.id.img_video_header)
    val imgThumb1 by bind<ImageView>(R.id.img_thumb_1)
    val imgThumb2 by bind<ImageView>(R.id.img_thumb_2)
    val imgThumb3 by bind<ImageView>(R.id.img_thumb_3)
    val imgThumb4 by bind<ImageView>(R.id.img_thumb_4)

    val txtHeaderVideo by bind<TextView>(R.id.txt_video_header)
    val txtTitle1 by bind<TextView>(R.id.txt_video_title_1)
    val txtTitle2 by bind<TextView>(R.id.txt_video_title_2)
    val txtTitle3 by bind<TextView>(R.id.txt_video_title_3)
    val txtTitle4 by bind<TextView>(R.id.txt_video_title_4)

    val itemVideoHeader by bind<ConstraintLayout>(R.id.itemVideoHeader)
    val itemVideo1 by bind<ConstraintLayout>(R.id.itemVideo1)
    val itemVideo2 by bind<ConstraintLayout>(R.id.itemVideo2)
    val itemVideo3 by bind<ConstraintLayout>(R.id.itemVideo3)
    val itemVideo4 by bind<ConstraintLayout>(R.id.itemVideo4)

    val btXemthem by bind<AppCompatButton>(R.id.btXemThemVideo)
}