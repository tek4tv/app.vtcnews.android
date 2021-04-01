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
import app.vtcnews.android.model.Video
import app.vtcnews.android.model.VideoDetail
import app.vtcnews.android.paging.VideoMenuPagingSource
import app.vtcnews.android.repos.ArticleRepo
import app.vtcnews.android.repos.VideoRepo
import kotlinx.coroutines.launch

class VideoHomeFragViewModel @ViewModelInject constructor(
    private val videoRepo: VideoRepo,
    private val articleRepo: ArticleRepo

) : ViewModel() {
    var articleId: Long = 0
    val articleDetail = MutableLiveData<ArticleDetail>()

    //    val listVideoHome = MutableLiveData<List<Video>>()
    val listVideoMenuHome = MutableLiveData<List<Video>>()
    val videoDetail = MutableLiveData<List<VideoDetail>>()
    val listVideoByCategory = MutableLiveData<List<Article>>()
    val error = MutableLiveData<String>()

//    fun getVideoHome() {
//        viewModelScope.launch {
//            when (val res = videoRepo.getVideos()) {
//                is app.vtcnews.android.network.Resource.Success -> listVideoHome.value = res.data
//                is app.vtcnews.android.network.Resource.Error -> error.value = res.message
//            }
//        }
//    }

    fun getVideoMenuHome() {
        viewModelScope.launch {
            when (val res = videoRepo.getMenuVideos()) {
                is app.vtcnews.android.network.Resource.Success -> listVideoMenuHome.value =
                    res.data
                is app.vtcnews.android.network.Resource.Error -> error.value = res.message
            }
        }
    }

    fun getVideoDetail(id: Long): LiveData<List<VideoDetail>> {
        val resVideo = MutableLiveData<List<VideoDetail>>()
        viewModelScope.launch {
            when (val res = videoRepo.getVideoDetail(id)) {
                is app.vtcnews.android.network.Resource.Success -> resVideo.value = res.data
                is app.vtcnews.android.network.Resource.Error -> error.value = res.message
            }
        }
        return resVideo
    }

    fun getVideoByCategory(page: Int, id: Long) {
        viewModelScope.launch {
            when (val res: app.vtcnews.android.network.Resource<MutableList<Article>> =
                articleRepo.getArticleByCategory(page, id)) {
                is app.vtcnews.android.network.Resource.Success -> listVideoByCategory.value =
                    res.data
                is app.vtcnews.android.network.Resource.Error -> error.value = res.message
            }
        }
    }

    fun getArticleDetail() {
        viewModelScope.launch {
            when (val res = articleRepo.getArticleDetail(articleId.toInt())) {
                is app.vtcnews.android.network.Resource.Success -> articleDetail.value = res.data
                is app.vtcnews.android.network.Resource.Error -> error.value =
                    "ArticleDetail: ${res.message}"
            }
        }
    }

    //video menu paging
    var dataSource: PagingSource<Int, Video> = VideoMenuPagingSource(videoRepo)
    val pagingData = Pager(
        config = PagingConfig(pageSize = 8)
    ) {
        dataSource
    }
        .flow
        .cachedIn(viewModelScope)
}