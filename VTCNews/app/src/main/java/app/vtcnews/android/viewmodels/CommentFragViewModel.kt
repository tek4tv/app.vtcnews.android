package app.vtcnews.android.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.vtcnews.android.model.comment.CommentItem
import app.vtcnews.android.repos.CommentRepo
import kotlinx.coroutines.launch

class CommentFragViewModel @ViewModelInject constructor(
    val commentRepo: CommentRepo
) : ViewModel() {
    val totalComment = MutableLiveData<Int>()
    val totalAllComment = MutableLiveData<Int>()
    val totalPage = MutableLiveData<Int>()
    val currentPage = MutableLiveData<Int>()
    val error = MutableLiveData<String>()
    val success = MutableLiveData<Long>()
    val listIteamComment = MutableLiveData<List<CommentItem>>()

    fun getComment(id: Long, page: Int) {
        viewModelScope.launch {
            when (val res = commentRepo.getComment(id, page)) {
                is app.vtcnews.android.network.Resource.Success -> {
                    listIteamComment.value = res.data.items
                    totalComment.value = res.data.totalRecord
                    totalAllComment.value = res.data.totalAllRecord
                    totalPage.value = res.data.totalPage
                    currentPage.value = res.data.currentPage
                }
                is app.vtcnews.android.network.Resource.Error -> error.value = res.message
            }
        }
    }

    suspend fun postComment(username: String, email: String, articleId: String,idCm:String, value: String) {
        when (val res = commentRepo.postComment(username, email, articleId,idCm, value)) {
            is app.vtcnews.android.network.Resource.Success ->success.value = res.data.toLong()
            is app.vtcnews.android.network.Resource.Error -> error.value = res.message
        }
    }

}