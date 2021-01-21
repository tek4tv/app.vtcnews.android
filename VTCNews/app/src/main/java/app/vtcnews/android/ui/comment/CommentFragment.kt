package app.vtcnews.android.ui.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import app.vtcnews.android.R
import app.vtcnews.android.databinding.CommentFragmentLayoutBinding
import app.vtcnews.android.model.comment.CommentItem
import app.vtcnews.android.viewmodels.CommentFragViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CommentFragment : Fragment() {
    val viewModelCM: CommentFragViewModel by viewModels()
    lateinit var binding: CommentFragmentLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CommentFragmentLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelCM.getComment(requireArguments().getLong("articleID"), 1)
        setUpObser()
        buttonClick()

    }

    fun setUpObser() {
        val listParentCm = ArrayList<CommentItem>()
        viewModelCM.listIteamComment.observe(viewLifecycleOwner)
        {
            it.forEach { commentItem ->
                if (commentItem.parentID == 0) {
                    listParentCm.add(commentItem)
                }
            }
            val adapter = CommentItemParentAdapter(listParentCm.take(3))
            val layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvComment.adapter = adapter
            binding.rvComment.layoutManager = layoutManager
            binding.btShowmoreCm.isVisible = it.size > 3
            adapter.clickListener = { commentItem ->
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                    .add(
                        R.id.fragment_holder,
                        CommentReplyFragment.newInstance(commentItem)
                    )
                    .addToBackStack(null).commit()
            }
        }

        viewModelCM.totalComment.observe(viewLifecycleOwner)
        {
            binding.tvTotalCm.text = it.toString()
        }
    }

    fun buttonClick() {
        binding.btShowmoreCm.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .add(
                    R.id.fragment_holder,
                    CommentAllFragment.newInstance(requireArguments().getLong("articleID"))
                )
                .addToBackStack(null).commit()
        }
        binding.btComment.setOnClickListener {
            displayAlertDialog()
        }
    }

    fun displayAlertDialog() {
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
                        "0",
                        binding.etValueCm.text.toString()
                    )

                    if (viewModelCM.success.value!! > 0) {
                        displayDialogSucces()
                        binding.etValueCm.setText("")
                        dialog.hide()
                    } else {
                        Toast.makeText(
                            context,
                            "Bình luận không thành công, hãy thử lại",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.etValueCm.setText("")
                        dialog.hide()
                    }
                }else
            {Toast.makeText(
                context,
                "Nội dung, tên, email không được để trống",
                Toast.LENGTH_SHORT
            ).show()}
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
        fun newInstance(articleId: Long) = CommentFragment().apply {
            arguments = Bundle().apply {
                putLong("articleID", articleId)
            }
        }
    }
}