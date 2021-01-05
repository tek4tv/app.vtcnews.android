package app.vtcnews.android.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.vtcnews.android.model.ArticleDetail
import app.vtcnews.android.network.Resource
import app.vtcnews.android.repos.ArticleRepo
import kotlinx.coroutines.launch

class ArticleDetailViewModel @ViewModelInject constructor(
    private val articleRepo: ArticleRepo
) : ViewModel() {
    var articleId = 0
    val articleDetail = MutableLiveData<ArticleDetail>()
    val error = MutableLiveData<String>()

    fun getArticleDetail()
    {
        viewModelScope.launch {
            when(val res = articleRepo.getArticleDetail(articleId))
            {
                is Resource.Success -> articleDetail.value = res.data
                is Resource.Error -> error.value = "ArticleDetail: ${res.message}"
            }
        }
    }
}