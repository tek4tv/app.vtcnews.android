package app.vtcnews.android.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import app.vtcnews.android.model.Article.Article
import app.vtcnews.android.paging.CategoryArticlePagingSource
import app.vtcnews.android.paging.TrendingArticlePagingSource
import app.vtcnews.android.repos.ArticleRepo

class PagingArticleFragmentViewModel @ViewModelInject constructor(
    private val articleRepo: ArticleRepo
) : ViewModel() {
    var categoryId = 0
        set(value) {
            field = value
            dataSource = if (value != -1) CategoryArticlePagingSource(articleRepo, categoryId)
            else TrendingArticlePagingSource(articleRepo)
        }

    var dataSource: PagingSource<Int, Article>? = null
    val pagingData = Pager(
        config = PagingConfig(pageSize = 20)
    ) {
        dataSource!!
    }
        .flow
        .cachedIn(viewModelScope)
}