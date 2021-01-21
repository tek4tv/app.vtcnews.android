package app.vtcnews.android.ui.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.filter
import androidx.recyclerview.widget.LinearLayoutManager
import app.vtcnews.android.R
import app.vtcnews.android.databinding.CommentTotalLayoutBinding
import app.vtcnews.android.viewmodels.CommentPagingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommentAllFragment : Fragment() {
    lateinit var binding: CommentTotalLayoutBinding
    val viewModelPaging: CommentPagingViewModel by viewModels()
    val cmpaginAdapter = CommentPagingAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CommentTotalLayoutBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelPaging.id = requireArguments().getLong("articleId")
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvTotalComment.layoutManager = layoutManager
        binding.rvTotalComment.adapter = cmpaginAdapter
        setupObservers()
        cmpaginAdapter.clickListener = { commentItem ->
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .add(
                    R.id.fragment_holder,
                    CommentReplyFragment.newInstance(commentItem)
                )
                .addToBackStack(null).commit()
        }


    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModelPaging.pagingData.collectLatest { it ->

                val r = it.filter { item ->
                    item.parentID == 0
                }
                cmpaginAdapter.submitData(r)
            }
        }
    }

//    fun setUpObser() {
//        val listParentCm = ArrayList<CommentItem>()
//        viewModelCM.listIteamComment.observe(viewLifecycleOwner)
//        {
//            it.forEach { commentItem ->
//                if (commentItem.parentID == 0) {
//                    listParentCm.add(commentItem)
//                }
//            }
//            val adapter = CommentItemParentAdapter(listParentCm)
//            val layoutManager =
//                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//            binding.rvTotalComment.adapter = adapter
//            binding.rvTotalComment.layoutManager = layoutManager
//
//            adapter.clickListener = { commentItem ->
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
//                    .replace(
//                        R.id.fragment_holder,
//                        CommentReplyFragment.newInstance(commentItem)
//                    )
//                    .addToBackStack(null).commit()
//            }
//        }
//    }

    companion object {
        fun newInstance(articleId: Long) = CommentAllFragment().apply {
            arguments = Bundle().apply {
                putLong("articleId", articleId)
            }
        }
    }


}