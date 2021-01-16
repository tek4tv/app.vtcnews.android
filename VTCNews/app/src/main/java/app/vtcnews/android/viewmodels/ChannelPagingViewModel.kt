package app.vtcnews.android.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.vtcnews.android.model.Article.ChannelPaging.ItemChannel
import app.vtcnews.android.repos.ArticleRepo
import kotlinx.coroutines.launch

class ChannelPagingViewModel @ViewModelInject constructor(private var articleRepo: ArticleRepo) :
    ViewModel() {
    val listChannelPaging = MutableLiveData<List<ItemChannel>>()
    val error = MutableLiveData<String>()
    fun getChannelPaging(page: Int, id: Long) {
        viewModelScope.launch {
            when (val res = articleRepo.getChannelPaging(page, id)) {
                is app.vtcnews.android.network.Resource.Success -> {
                    listChannelPaging.value = res.data.items
                }
                is app.vtcnews.android.network.Resource.Error -> error.value = res.message
            }
        }
    }
}