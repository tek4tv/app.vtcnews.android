package app.vtcnews.android.ui.comment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import app.vtcnews.android.R
import app.vtcnews.android.databinding.CommentFragmentLayoutBinding
import app.vtcnews.android.model.comment.CommentItem
import app.vtcnews.android.viewmodels.CommentFragViewModel
import dagger.hilt.android.AndroidEntryPoint

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
                    .replace(
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
    fun buttonClick()
    {
        binding.btShowmoreCm.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .replace(
                    R.id.fragment_holder,
                    CommentAllFragment.newInstance(requireArguments().getLong("articleID"))
                )
                .addToBackStack(null).commit()
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