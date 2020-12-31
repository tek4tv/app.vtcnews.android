package app.vtcnews.android.viewmodels

import android.provider.MediaStore
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.vtcnews.android.model.Video
import app.vtcnews.android.repos.VideoRepo

class VideoHomeFragViewModel @ViewModelInject constructor(
    private val videoRepo: VideoRepo
) : ViewModel() {
    val listVideoHome = MutableLiveData<List<Video>>()
}