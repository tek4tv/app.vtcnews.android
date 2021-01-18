package app.vtcnews.android.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import app.vtcnews.android.model.HotChannel
import app.vtcnews.android.paging.ChannelArticlePagingSource
import app.vtcnews.android.repos.ArticleRepo

class HotChannelPagingViewModel @ViewModelInject constructor(
    articleRepo: ArticleRepo
): ViewModel() {
    var dataSource: PagingSource<Int, HotChannel> = ChannelArticlePagingSource(articleRepo)
    val pagingData = Pager(
        config = PagingConfig(pageSize = 30)
    ) {
        dataSource
    }
        .flow
        .cachedIn(viewModelScope)

}