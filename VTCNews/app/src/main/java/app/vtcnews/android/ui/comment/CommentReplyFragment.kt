package app.vtcnews.android.ui.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.vtcnews.android.MainActivity
import app.vtcnews.android.R
import app.vtcnews.android.databinding.CommentShowReplyLayoutBinding
import app.vtcnews.android.model.comment.CommentItem
import app.vtcnews.android.ui.trang_chu_sub_section.getDateDiff
import app.vtcnews.android.viewmodels.CommentFragViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentReplyFragment : Fragment() {
    lateinit var binding: CommentShowReplyLayoutBinding
    val viewModelCM: CommentFragViewModel by viewModels()
    lateinit var navBottom : BottomNavigationView
    private var refreshLayout: SwipeRefreshLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CommentShowReplyLayoutBinding.inflate(layoutInflater, container, false)
        (requireActivity() as MainActivity).supportActionBar?.hide()
        navBottom = requireActivity().findViewById(R.id.main_bottom_nav)
        navBottom.isVisible = false
        refreshLayout = activity?.findViewById(R.id.refreshlayout)
        refreshLayout?.isEnabled = false
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
        viewModelCM.getComment(parentId,1)
        setUpObser(parentId)
        binding.btBack.setOnClickListener{
            refreshLayout?.isEnabled = true
            (requireActivity() as MainActivity).supportActionBar?.show()
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    fun setUpObser(parentId : Long) {
        val listChildCm = ArrayList<CommentItem>()

        viewModelCM.listIteamComment.observe(viewLifecycleOwner)
        {
            it.forEach { commentItem ->
                if (commentItem.parentID.toLong() == parentId ) {
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

    companion object {
        fun newInstance(commentItem: CommentItem) = CommentReplyFragment().apply {
            arguments = Bundle().apply {
                putSerializable("parentCmt", commentItem)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        refreshLayout?.isEnabled = true
        (requireActivity() as MainActivity).supportActionBar?.hide()
    }
}