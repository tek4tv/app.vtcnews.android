package app.vtcnews.android.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.vtcnews.android.model.Article
import app.vtcnews.android.model.ArticleDetail
import app.vtcnews.android.model.ArticleVideo
import app.vtcnews.android.model.VideoDetail
import app.vtcnews.android.network.Resource
import app.vtcnews.android.repos.ArticleRepo
import app.vtcnews.android.repos.VideoRepo
import kotlinx.coroutines.launch

class ArticleDetailViewModel @ViewModelInject constructor(
    private val articleRepo: ArticleRepo,
    private val videoRepo: VideoRepo
) : ViewModel() {
    var articleId = 0
    val articleDetail = MutableLiveData<ArticleDetail>()
    val articleListByCategory = MutableLiveData<MutableList<Article>>()
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

    fun getVideoDetail(id: Long): LiveData<List<VideoDetail>> {
        val resVideo = MutableLiveData<List<VideoDetail>>()
        viewModelScope.launch {
            when (val res = videoRepo.getVideoDetail(id)) {
                is Resource.Success -> resVideo.value = res.data
            }
        }
        return resVideo
    }

    fun getArticleByCategory(page: Int, categoryId: Long) {
        viewModelScope.launch {
            when (val res: app.vtcnews.android.network.Resource<MutableList<Article>> =
                articleRepo.getArticleByCategory(page, categoryId)) {
                is Resource.Success -> articleListByCategory.value =
                    res.data
                is Resource.Error -> error.value = res.message
            }
        }
    }

    var boolean = MutableLiveData<Boolean>()
    fun getArticleDetailIsNull() {
        viewModelScope.launch {
            when (val res = articleRepo.getArticleDetail(articleId)) {
                is Resource.Success -> {
                    boolean.value = true
                }
                is Resource.Error -> {
                    boolean.value = false
                }
            }
        }
    }
}