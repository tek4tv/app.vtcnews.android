package app.vtcnews.android.ui.comment

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
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
import app.vtcnews.android.databinding.CommentTotalLayoutBinding
import app.vtcnews.android.model.comment.CommentItem
import app.vtcnews.android.viewmodels.CommentFragViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentAllFragment : Fragment() {
    lateinit var binding: CommentTotalLayoutBinding
    val viewModelCM: CommentFragViewModel by viewModels()
    lateinit var navBottom : BottomNavigationView
    private var refreshLayout: SwipeRefreshLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CommentTotalLayoutBinding.inflate(layoutInflater, container, false)
        (requireActivity() as MainActivity).supportActionBar?.hide()
        navBottom = requireActivity().findViewById(R.id.main_bottom_nav)
        navBottom.isVisible = false
        refreshLayout = activity?.findViewById(R.id.refreshlayout)
        refreshLayout?.isEnabled = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelCM.getComment(requireArguments().getLong("articleId"), 1)
        setUpObser()
        binding.btBack.setOnClickListener{
            refreshLayout?.isEnabled = true
            (requireActivity() as MainActivity).supportActionBar?.show()
            requireActivity().supportFragmentManager.popBackStack()
        }
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
            val adapter = CommentItemParentAdapter(listParentCm)
            val layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvTotalComment.adapter = adapter
            binding.rvTotalComment.layoutManager = layoutManager

            adapter.clickListener = { commentItem ->
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                    .replace(
                        R.id.fragment_holder,
                        CommentReplyFragment.newInstance(commentItem)
                    )
                    .addToBackStack(null).commit()
            }
        }
    }

    companion object {
        fun newInstance(articleId: Long) = CommentAllFragment().apply {
            arguments = Bundle().apply {
                putLong("articleId", articleId)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        refreshLayout?.isEnabled = true
        (requireActivity() as MainActivity).supportActionBar?.hide()
    }

}