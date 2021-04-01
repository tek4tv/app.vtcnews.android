package app.vtcnews.android.ui.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.R
import app.vtcnews.android.model.comment.CommentItem
import app.vtcnews.android.ui.trang_chu_sub_section.getDateDiff

class CommentPagingAdapter : PagingDataAdapter<CommentItem, CommentHoldel>(CommentDiffCallback) {
    var clickListener: (CommentItem) -> Unit = {}
    override fun onBindViewHolder(holder: CommentHoldel, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHoldel {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.comment_item_layout, parent, false)
        return CommentHoldel(itemView)
    }
}

object CommentDiffCallback : DiffUtil.ItemCallback<CommentItem>() {
    override fun areItemsTheSame(oldItem: CommentItem, newItem: CommentItem): Boolean =
        oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: CommentItem, newItem: CommentItem): Boolean =
        oldItem == newItem
}

class CommentHoldel(v: View) : RecyclerView.ViewHolder(v) {
    private val tvTimeDiffComment = v.findViewById(R.id.tvTimeDiffComment) as TextView
    private val tvCustomerName = v.findViewById(R.id.tvCustomerName) as TextView
    private val tvContent = v.findViewById(R.id.tvContentCM) as TextView
    private val tvCountLike = v.findViewById(R.id.tvCountLike) as TextView
    private val tvReply = v.findViewById(R.id.tvReply) as TextView
    private val icLike = v.findViewById(R.id.icLike) as ImageView
    private val isHaveChild = v.findViewById(R.id.isHaveChild) as LinearLayout
    private val tvCountChild = v.findViewById(R.id.tvCmtChildCount) as TextView
    fun bind(commentItem: CommentItem, clickListener: (CommentItem) -> Unit) {
        tvTimeDiffComment.text =
            getDateDiff(commentItem.createdDate, tvTimeDiffComment.context.resources)
        tvCustomerName.text = commentItem.customerName
        tvContent.text = commentItem.content
        tvCountLike.text = commentItem.countLike.toString()
        isHaveChild.setOnClickListener {
            clickListener.invoke(commentItem)
        }
        tvReply.setOnClickListener {
            clickListener.invoke(commentItem)

        }
        if (commentItem.countChild > 0) {
            tvCountChild.text = ((commentItem.countChild).toString())
            isHaveChild.isVisible = true
        } else {
            isHaveChild.isVisible = false
        }

        if (commentItem.countLike > 0) {
            icLike.setImageResource(R.drawable.icliked)
        } else {
            icLike.setImageResource(R.drawable.iclike)
        }
    }

}
