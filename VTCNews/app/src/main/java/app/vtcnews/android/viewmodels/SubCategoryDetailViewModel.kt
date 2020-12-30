package app.vtcnews.android.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import app.vtcnews.android.paging.CategoryArticlePagingSource
import app.vtcnews.android.repos.ArticleRepo

class SubCategoryDetailViewModel @ViewModelInject constructor(
    private val articleRepo: ArticleRepo
) : ViewModel() {
    var categoryId = 0
    val pagingData = Pager(
        config = PagingConfig(pageSize = 30)
    ) {
        CategoryArticlePagingSource(articleRepo, categoryId)
    }
        .flow
        .cachedIn(viewModelScope)
}