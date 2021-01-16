package app.vtcnews.android.ui.trang_chu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.vtcnews.android.databinding.LayoutVideoPageBinding
import app.vtcnews.android.viewmodels.HotChannelPagingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HotChannelFragment : Fragment() {
    lateinit var binding: LayoutVideoPageBinding
    private val viewModel: HotChannelPagingViewModel by viewModels()
    private val pagingChannelAdapter = AllChannelAdapter()
    companion object
    {
        fun newInstance() = HotChannelFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutVideoPageBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layout = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.rvVideoHome.layoutManager = layout
        binding.rvVideoHome.adapter = pagingChannelAdapter
        setupObservers()
    }
    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.pagingData.collectLatest {
                pagingChannelAdapter.submitData(it)
            }
        }
    }
}