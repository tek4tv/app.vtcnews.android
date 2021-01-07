package app.vtcnews.android.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.vtcnews.android.model.Article
import app.vtcnews.android.model.ArticleDetail
import app.vtcnews.android.model.ArticleVideo
import app.vtcnews.android.network.Resource
import app.vtcnews.android.repos.ArticleRepo
import app.vtcnews.android.repos.MenuRepo
import app.vtcnews.android.repos.VideoRepo
import kotlinx.coroutines.launch

class ArticleDetailViewModel @ViewModelInject constructor(
    private val articleRepo: ArticleRepo,
    private val videoRepo: VideoRepo
) : ViewModel() {
    var articleId = 0
    val articleDetail = MutableLiveData<ArticleDetail>()
    val articleVideos = MutableLiveData<List<ArticleVideo>>()
    val articleListByCategory = MutableLiveData<List<Article>>()
    val error = MutableLiveData<String>()

    var currentWindow = 0
    var playbackPosition: Long = 0
    var curVideo: ArticleVideo? = null

    fun getArticleDetail() {
        viewModelScope.launch {
            when (val res = articleRepo.getArticleDetail(articleId)) {
                is Resource.Success -> articleDetail.value = res.data
                is Resource.Error -> error.value = "ArticleDetail: ${res.message}"
            }
        }
    }

    fun getVideoList(idList: List<String>) {
        viewModelScope.launch {
            val result = mutableListOf<ArticleVideo>()

            idList.forEach { id ->
                when (val response = videoRepo.getArticleVideo(id)) {
                    is Resource.Success -> result.addAll(response.data)
                    is Resource.Error -> error.value = response.message
                }
            }

            articleVideos.value = result
        }
    }

    fun getArticleByCategory(page: Int, categoryId: Long) {
        viewModelScope.launch {
            when (val res: app.vtcnews.android.network.Resource<List<Article>> =
                articleRepo.getArticleByCategory(page, categoryId)) {
                is app.vtcnews.android.network.Resource.Success -> articleListByCategory.value =
                    res.data
                is app.vtcnews.android.network.Resource.Error -> error.value = res.message
            }
        }
    }


    fun resetVideoParams() {
        currentWindow = 0
        playbackPosition = 0
    }
}