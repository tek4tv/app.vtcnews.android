package app.vtcnews.android.viewmodels

import android.provider.MediaStore
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.vtcnews.android.model.Article
import app.vtcnews.android.model.Video
import app.vtcnews.android.model.VideoDetail
import app.vtcnews.android.repos.ArticleRepo
import app.vtcnews.android.repos.VideoRepo
import kotlinx.coroutines.launch
import javax.annotation.Resource

class VideoHomeFragViewModel @ViewModelInject constructor(
    private val videoRepo: VideoRepo,
    private val articleRepo: ArticleRepo

) : ViewModel() {
    val listVideoHome = MutableLiveData<List<Video>>()
    val videoDetail = MutableLiveData<List<VideoDetail>>()
    val listVideoByCategory = MutableLiveData<List<Article>>()
    val error = MutableLiveData<String>()

    fun getVideoHome()
    {
        viewModelScope.launch {
            when(val res = videoRepo.getVideos())
            {
                is app.vtcnews.android.network.Resource.Success ->listVideoHome.value = res.data
                is app.vtcnews.android.network.Resource.Error -> error.value = res.message
            }
        }
    }
    fun getVideoDetail(id:Long)
    {
        viewModelScope.launch {
            when (val res = videoRepo.getVideoDetail(id))
            {
                is app.vtcnews.android.network.Resource.Success ->videoDetail.value = res.data
                is app.vtcnews.android.network.Resource.Error -> error.value = res.message
            }
        }
    }
    fun getVideoByCategory(page : Int, id:Long)
    {
        viewModelScope.launch {
            when (val res:app.vtcnews.android.network.Resource<List<Article>> = articleRepo.getArticleByCategory(page,id))
            {
                is app.vtcnews.android.network.Resource.Success ->listVideoByCategory.value = res.data
                is app.vtcnews.android.network.Resource.Error -> error.value = res.message
            }
        }
    }
}