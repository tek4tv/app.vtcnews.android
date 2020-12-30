package app.vtcnews.android.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.vtcnews.android.model.Audio.*
import app.vtcnews.android.repos.AudioRepo
import kotlinx.coroutines.launch
import javax.annotation.Resource

class AudioHomeFragViewModel @ViewModelInject constructor(
    private val audioRepo: AudioRepo
) : ViewModel() {
    val listAllPodCast= MutableLiveData<List<AllPodCast>>()
    val listChannelPodCast= MutableLiveData<List<ChannelPodcast>>()
    val listAlbumPaging= MutableLiveData<List<AlbumPaging>>()
    val listAlbumDetail= MutableLiveData<List<ListPodcast>>()
    val podcastInfo = MutableLiveData<PodcastInfo>()
    val error = MutableLiveData<String>()

    fun getListAllPC()
    {
        viewModelScope.launch {
            when(val res = audioRepo.getAllPodcast())
            {
                is app.vtcnews.android.network.Resource.Success ->listAllPodCast.value = res.data
                is app.vtcnews.android.network.Resource.Error -> error.value = res.message
            }
        }
    }

    fun getChannelPodcast(id:Long)
    {
        viewModelScope.launch {
            when(val res = audioRepo.getChannelPodcast(id))
            {
                is app.vtcnews.android.network.Resource.Success -> listChannelPodCast.value = res.data
                is app.vtcnews.android.network.Resource.Error -> error.value = res.message
            }
        }
    }
    fun getAlbumPaging(id:Long)
    {
        viewModelScope.launch {
            when(val res = audioRepo.getAlbumPaging(id))
            {
                is app.vtcnews.android.network.Resource.Success -> listAlbumPaging.value = res.data
                is app.vtcnews.android.network.Resource.Error -> error.value = res.message
            }
        }
    }
    fun getAlbumDetail(id: Long)
    {
        viewModelScope.launch {
            when (val res = audioRepo.getAlbumDetail(id))
            {
                is app.vtcnews.android.network.Resource.Success -> {
                    listAlbumDetail.value = res.data.items
                    podcastInfo.value = res.data.info
                }
                is app.vtcnews.android.network.Resource.Error -> error.value = res.message
            }
        }
    }
}