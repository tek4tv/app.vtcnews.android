package app.vtcnews.android.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import app.vtcnews.android.model.Article
import app.vtcnews.android.model.ArticleDetail
import app.vtcnews.android.model.VideoDetail
import app.vtcnews.android.network.Resource
import app.vtcnews.android.paging.ListByTagPaging
import app.vtcnews.android.repos.ArticleRepo
import app.vtcnews.android.repos.VideoRepo
import kotlinx.coroutines.launch

class ListByTagPagingViewModel @ViewModelInject constructor(
    private var articleRepo: ArticleRepo,
    private val videoRepo: VideoRepo
) :
    ViewModel() {
    var tag: String = ""
        set(value) {
            field = value
            dataSource = ListByTagPaging(articleRepo, tag)
        }
    var dataSource: PagingSource<Int, Article>? = null
    val pagingData = Pager(
        config = PagingConfig(pageSize = 20)
    ) {
        dataSource!!
    }
        .flow
        .cachedIn(viewModelScope)

    fun getVideoDetail(id: Long): LiveData<List<VideoDetail>> {
        val resVideo = MutableLiveData<List<VideoDetail>>()
        viewModelScope.launch {
            when (val res = videoRepo.getVideoDetail(id)) {
                is Resource.Success -> resVideo.value = res.data
            }
        }
        return resVideo
    }

    var articleId = 0
    val articleDetail = MutableLiveData<ArticleDetail>()
    var boolean = MutableLiveData<Boolean>()
    fun getArticleDetail() {
        viewModelScope.launch {
            when (val res = articleRepo.getArticleDetail(articleId)) {
                is Resource.Success -> {
                    articleDetail.value = res.data
                    boolean.value = true
                }
                is Resource.Error -> {
                    boolean.value = false
                }
            }
        }
    }

    fun getTag(tag: String): LiveData<Long> {
        val totalResult = MutableLiveData<Long>()
        viewModelScope.launch {
            when (val res = articleRepo.getListByTag(tag, 1)) {
                is Resource.Success -> {
                    totalResult.value = res.data.totalRecord
                }
            }
        }
        return totalResult
    }

}