package app.vtcnews.android.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.vtcnews.android.model.*
import app.vtcnews.android.model.Audio.AlbumPaging
import app.vtcnews.android.network.Resource
import app.vtcnews.android.repos.ArticleRepo
import app.vtcnews.android.repos.AudioRepo
import app.vtcnews.android.repos.MenuRepo
import app.vtcnews.android.repos.VideoRepo
import kotlinx.coroutines.launch

class TrangChuFragViewModel @ViewModelInject constructor(
    private val menuRepo: MenuRepo,
    private val articleRepo: ArticleRepo,
    private val videoRepo: VideoRepo,
    private val audioRepo: AudioRepo
) : ViewModel() {
    val menuList = MutableLiveData<List<MenuItem>>()
    val error = MutableLiveData<String>()
    val data = MutableLiveData<TrangChuData>()


    fun getMenuList() {
        viewModelScope.launch {
            when (val res = menuRepo.getMenuList()) {
                is Resource.Success -> menuList.value = menuRepo.parentMenus
                is Resource.Error -> error.value = res.message
            }
        }
    }

    fun getData() {
        viewModelScope.launch {
            var hotChannels = listOf<HotChannel>()
            when (val res = articleRepo.getHotChannels()) {
                is Resource.Success -> hotChannels = res.data
                is Resource.Error -> error.value = res.message
            }

            var hotArticles = listOf<Article>()
            when (val res = articleRepo.getHotArticles()) {
                is Resource.Success -> hotArticles = res.data
                is Resource.Error -> error.value = res.message
            }

            var articleSuggestionHome = listOf<Article>()
            when (val res = articleRepo.getArticleSuggestionHome()) {
                is Resource.Success -> articleSuggestionHome = res.data
                is Resource.Error -> error.value = res.message
            }

            var videos = listOf<Video>()
            if (videoRepo.videoList.isNotEmpty()) videos = videoRepo.videoList.take(5)
            else {
                when (val res = videoRepo.getVideos()) {
                    is Resource.Success -> videos = res.data.take(5)
                    is Resource.Error -> error.value = res.message
                }
            }
            var audioListBook = listOf<AlbumPaging>()
//            if(videoRepo.videoList.isNotEmpty() && videoRepo.videoList.size >= 3 ) videos = videoRepo.videoList.take(3)
//            else
//            {
            when (val res = audioRepo.getAlbumPaging(3, 1)) {
                is Resource.Success -> audioListBook = res.data.take(3)
                is Resource.Error -> error.value = res.message
            }
            var audioListMusic = listOf<AlbumPaging>()
            when (val res = audioRepo.getAlbumPaging(14, 1)) {
                is Resource.Success -> audioListMusic = res.data.take(3)
                is Resource.Error -> error.value = res.message
            }
            var audioListPd = listOf<AlbumPaging>()
            when (val res = audioRepo.getAlbumPaging(5, 1)) {
                is Resource.Success -> audioListPd = res.data.take(3)
                is Resource.Error -> error.value = res.message
            }
            data.value = TrangChuData(
                hotChannels = hotChannels,
                hotArticles = hotArticles,
                articlesSuggestionsHome = articleSuggestionHome,
                videos = videos,
                audioBook = audioListBook,
                audioMusic = audioListMusic,
                audioPD = audioListPd
            )
        }
    }

    fun getVideoDetail(id: Long): LiveData<List<VideoDetail>> {
        val resVideo = MutableLiveData<List<VideoDetail>>()
        viewModelScope.launch {
            when (val res = videoRepo.getVideoDetail(id)) {
                is Resource.Success -> resVideo.value = res.data
                is Resource.Error -> error.value = res.message
            }
        }
        return resVideo
    }

    fun checkAlbumDetail(id: Long): LiveData<Boolean> {
        var boolean = MutableLiveData<Boolean>()
        viewModelScope.launch {
            when (val res = audioRepo.getAlbumDetail(id)) {
                is app.vtcnews.android.network.Resource.Success -> {
                    boolean.value = true
                }
                is app.vtcnews.android.network.Resource.Error -> {
                    boolean.value = false
                }
            }
        }
        return boolean
    }

//   fun getTrending(page :Int) {
//       viewModelScope.launch {
//        when (val res = articleRepo.getTrendingArticles(page)) {
//            is Resource.Success -> listTrending.value = res.data
//            is Resource.Error -> error.value = res.message
//        }
//    }
//   }

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

}