package app.vtcnews.android.ui.Search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.vtcnews.android.databinding.SearchLayoutBinding

class SearchFragment : Fragment() {
    lateinit var binding: SearchLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpWebview()
    }

    private fun setUpWebview() {
        binding.webviewSearch.apply {
            settings.javaScriptEnabled = true
            binding.webviewSearch.loadUrl("https://cse.google.com/cse?cx=partner-pub-2579189069606201%3A4583669249&ie=UTF-8&q=")
        }
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}