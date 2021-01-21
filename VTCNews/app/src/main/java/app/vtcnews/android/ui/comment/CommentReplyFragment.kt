package app.vtcnews.android.ui.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import app.vtcnews.android.R
import app.vtcnews.android.databinding.CommentShowReplyLayoutBinding
import app.vtcnews.android.model.comment.CommentItem
import app.vtcnews.android.ui.audio.AudioHomeFragment
import app.vtcnews.android.ui.share.ShareFragment
import app.vtcnews.android.ui.trang_chu.TrangChuFragment
import app.vtcnews.android.ui.trang_chu_sub_section.ArticlesFragment
import app.vtcnews.android.ui.trang_chu_sub_section.getDateDiff
import app.vtcnews.android.viewmodels.CommentFragViewModel
import com.example.vtclive.Video.FragmentVideoPage
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommentReplyFragment : Fragment() {
    lateinit var binding: CommentShowReplyLayoutBinding
    val viewModelCM: CommentFragViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CommentShowReplyLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cmItem: CommentItem = requireArguments().getSerializable("parentCmt") as CommentItem
        binding.tvCustomerName.setText(cmItem.customerName)
        binding.tvContentCM.setText(cmItem.content)
        binding.tvTimeDiffComment.setText(getDateDiff(cmItem.createdDate, resources))
        binding.tvCountLike.setText(cmItem.countLike.toString())
        val parentId = cmItem.id
        viewModelCM.getComment(parentId, 1)
        setUpObser(parentId)

        binding.btComment.setOnClickListener {
            displayAlertDialog(parentId)
        }

    }

    fun setUpObser(parentId: Long) {
        val listChildCm = ArrayList<CommentItem>()

        viewModelCM.listIteamComment.observe(viewLifecycleOwner)
        {
            it.forEach { commentItem ->
                if (commentItem.parentID.toLong() == parentId) {
                    listChildCm.add(commentItem)
                }
            }
            val adapter = CommentItemParentAdapter(listChildCm)
            val layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvListReply.adapter = adapter
            binding.rvListReply.layoutManager = layoutManager

        }
    }

    fun displayAlertDialog(idParent: Long) {
        val inflater = layoutInflater
        val alertLayout: View = inflater.inflate(R.layout.custom_dialog_layout, null)
        val etFullName = alertLayout.findViewById<View>(R.id.etFullName) as EditText
        val etEmail = alertLayout.findViewById<View>(R.id.etEmail) as EditText
        val btCancel = alertLayout.findViewById<Button>(R.id.btCancelCm)
        val btSend = alertLayout.findViewById<Button>(R.id.btSendCm)
        val alert: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alert.setView(alertLayout)
        alert.setCancelable(false)
        val dialog = alert.create()
        dialog.show()
        btSend.setOnClickListener {
            if (etFullName.text.toString() != "" && etEmail.text.toString() != "" &&
                binding.etValueCm.text.toString() != ""
            )
                lifecycleScope.launch {
                    viewModelCM.postComment(
                        etFullName.text.toString(),
                        etEmail.text.toString(),
                        requireArguments().getLong("articleID").toString(),
                        idParent.toString(),
                        binding.etValueCm.text.toString()
                    )

                    if (viewModelCM.success.value!! > 0) {
                        displayDialogSucces()
                        dialog.hide()
                        binding.etValueCm.setText("")
                    } else {
                        Toast.makeText(
                            context,
                            "Bình luận không thành công, hãy thử lại",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog.hide()
                    }
                } else {
                Toast.makeText(
                    context,
                    "Nội dung, tên, email không được để trống",
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.etValueCm.setText("")
        }


        btCancel.setOnClickListener {
            dialog.hide()
        }


//        val back = ColorDrawable(Color.WHITE)
//        val inset = InsetDrawable(back, 50,50,50,50)
//       dialog.window?.setBackgroundDrawable(inset)
    }

    fun displayDialogSucces() {
        val inflater = layoutInflater
        val alertLayout: View = inflater.inflate(R.layout.custom_dialog_cmsucces, null)
        val btClose = alertLayout.findViewById<Button>(R.id.btClose)
        val alert: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alert.setView(alertLayout)
        alert.setCancelable(false)
        val dialog = alert.create()
        dialog.show()
        btClose.setOnClickListener {
            dialog.hide()
        }
    }

    companion object {
        fun newInstance(commentItem: CommentItem) = CommentReplyFragment().apply {
            arguments = Bundle().apply {
                putSerializable("parentCmt", commentItem)
            }
        }
    }
}