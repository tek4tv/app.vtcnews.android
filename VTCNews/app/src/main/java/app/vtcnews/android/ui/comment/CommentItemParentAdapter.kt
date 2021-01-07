package app.vtcnews.android.ui.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import app.vtcnews.android.R
import app.vtcnews.android.model.comment.CommentItem
import app.vtcnews.android.ui.trang_chu_sub_section.getDateDiff

class CommentItemParentAdapter(val listCM: List<CommentItem>) :
    RecyclerView.Adapter<CommentItemParentAdapter.CommentParentHolder>() {

    var clickListener: (CommentItem) -> Unit = {}

    class CommentParentHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvTimeDiffComment = v.findViewById(R.id.tvTimeDiffComment) as TextView
        val tvCustomerName = v.findViewById(R.id.tvCustomerName) as TextView
        val tvContent = v.findViewById(R.id.tvContentCM) as TextView
        val tvCountLike = v.findViewById(R.id.tvCountLike) as TextView
        val tvReply = v.findViewById(R.id.tvReply) as TextView
        val icLike = v.findViewById(R.id.icLike) as ImageView
        val isHaveChild = v.findViewById(R.id.isHaveChild) as LinearLayout
        val tvCountChild = v.findViewById(R.id.tvCmtChildCount) as TextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentParentHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.comment_item_layout, parent, false)
        return CommentParentHolder(itemView)
    }

    override fun getItemCount(): Int = listCM.size

    override fun onBindViewHolder(
        holder: CommentParentHolder,
        position: Int
    ) {
        val commentItem = listCM[position]
        holder.tvTimeDiffComment.text =
            getDateDiff(commentItem.createdDate, holder.tvTimeDiffComment.context.resources)
        holder.tvCustomerName.text = commentItem.customerName
        holder.tvContent.text = commentItem.content
        holder.tvCountLike.text = commentItem.countLike.toString()
        holder.tvReply.setOnClickListener {
            clickListener.invoke(commentItem)
        }
        if(commentItem.countChild > 0)
        {
            holder.tvCountChild.text = ((commentItem.countChild ).toString())
            holder.isHaveChild.isVisible = true
        }
        else
        {
            holder.isHaveChild.isVisible = false
        }

        if (commentItem.countLike > 0) {
            holder.icLike.setImageResource(R.drawable.icliked)
        } else {
            holder.icLike.setImageResource(R.drawable.iclike)
        }
    }

}