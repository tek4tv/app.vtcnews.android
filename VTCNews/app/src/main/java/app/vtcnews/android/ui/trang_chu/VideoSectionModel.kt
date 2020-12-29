package app.vtcnews.android.ui.trang_chu

import android.widget.ImageView
import android.widget.TextView
import app.vtcnews.android.R
import app.vtcnews.android.model.Video
import app.vtcnews.android.ui.KotlinEpoxyHolder
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide

@EpoxyModelClass(layout = R.layout.home_video_section_layout)
abstract class VideoSectionModel : EpoxyModelWithHolder<VideoSectionHolder>() {

    @EpoxyAttribute
    var videoList : List<Video> = listOf()

    @EpoxyAttribute
    var clickListener : (Video) -> Unit = {}

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

            imgs.forEachIndexed { index, imageView ->
                val thumbUrl = if(index == 0) videoList[index].image169_Large
                else videoList[index].image169
                Glide.with(imageView)
                    .load(thumbUrl!!)
                    .into(imageView)

                imageView.setOnClickListener {
                    clickListener.invoke(videoList[index])
                }
            }

            txt.forEachIndexed { index, textView ->
                textView.text = videoList[index].title
            }
        }
    }
}

class VideoSectionHolder : KotlinEpoxyHolder()
{
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
}