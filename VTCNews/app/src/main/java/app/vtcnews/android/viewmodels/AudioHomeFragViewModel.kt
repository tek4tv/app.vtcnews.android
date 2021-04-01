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
import app.vtcnews.android.model.Audio.*
import app.vtcnews.android.paging.AudioPagingSource
import app.vtcnews.android.repos.AudioRepo
import kotlinx.coroutines.launch

class AudioHomeFragViewModel @ViewModelInject constructor(
    private val audioRepo: AudioRepo
) : ViewModel() {
    val listAllPodCast = MutableLiveData<List<AllPodCast>>()
    val listChannelPodCast = MutableLiveData<List<ChannelPodcast>>()
    val listAlbumDetail = MutableLiveData<List<ListPodcast>>()
    val podcastInfo = MutableLiveData<PodcastInfo>()
    val error = MutableLiveData<String>()

    fun getListAllPC() {
        viewModelScope.launch {
            when (val res = audioRepo.getAllPodcast()) {
                is app.vtcnews.android.network.Resource.Success -> listAllPodCast.value = res.data
                is app.vtcnews.android.network.Resource.Error -> error.value = res.message
            }
        }
    }

    fun getChannelPodcast(id: Long) {
        viewModelScope.launch {
            when (val res = audioRepo.getChannelPodcast(id)) {
                is app.vtcnews.android.network.Resource.Success -> listChannelPodCast.value =
                    res.data
                is app.vtcnews.android.network.Resource.Error -> error.value = res.message
            }
        }
    }

    fun getAlbumPaging(id: Long, page: Int): MutableLiveData<MutableList<AlbumPaging>> {
        val resAlbumPaging = MutableLiveData<MutableList<AlbumPaging>>()
        viewModelScope.launch {
            when (val res = audioRepo.getAlbumPaging(id, page)) {
                is app.vtcnews.android.network.Resource.Success -> resAlbumPaging.value = res.data
                is app.vtcnews.android.network.Resource.Error -> error.value = res.message
            }
        }
        return resAlbumPaging
    }

    fun getAlbumDetail(id: Long) {
        viewModelScope.launch {
            when (val res = audioRepo.getAlbumDetail(id)) {
                is app.vtcnews.android.network.Resource.Success -> {
                    listAlbumDetail.value = res.data.items
                    podcastInfo.value = res.data.info

                }
                is app.vtcnews.android.network.Resource.Error -> {
                    error.value = res.message

                }
            }
        }
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


    //Paging album
    var id: Long = 0
        set(value) {
            field = value
            dataSource = AudioPagingSource(audioRepo, id)
        }
    var dataSource: PagingSource<Int, AlbumPaging>? = null
    val pagingData = Pager(
        config = PagingConfig(pageSize = 5)
    ) {
        dataSource!!
    }
        .flow
        .cachedIn(viewModelScope)
}