package app.vtcnews.android.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import app.vtcnews.android.model.comment.CommentItem
import app.vtcnews.android.paging.CommentPagingSource
import app.vtcnews.android.repos.CommentRepo

class CommentPagingViewModel @ViewModelInject constructor(private var commentRepo: CommentRepo) :
    ViewModel() {
    var id: Long = 0
        set(value) {
            field = value
            dataSource = CommentPagingSource(commentRepo, id)
        }
    var dataSource: PagingSource<Int, CommentItem>? = null
    val pagingData = Pager(
        config = PagingConfig(pageSize = 20)
    ) {
        dataSource!!
    }
        .flow
        .cachedIn(viewModelScope)

}