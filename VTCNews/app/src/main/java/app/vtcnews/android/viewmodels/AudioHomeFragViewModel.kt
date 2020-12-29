package app.vtcnews.android.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.vtcnews.android.model.Audio.AllPodCast
import app.vtcnews.android.model.Audio.ChannelPodcast
import app.vtcnews.android.repos.AudioRepo
import kotlinx.coroutines.launch
import javax.annotation.Resource

class AudioHomeFragViewModel @ViewModelInject constructor(
    private val audioRepo: AudioRepo
) : ViewModel() {
    val listAllPodCast= MutableLiveData<List<AllPodCast>>()
    val listChannelPodCast= MutableLiveData<List<ChannelPodcast>>()
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
}