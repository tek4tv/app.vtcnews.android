package app.vtcnews.android.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.vtcnews.android.model.HotChannel
import app.vtcnews.android.network.Resource
import app.vtcnews.android.repos.ArticleRepo

class ChannelArticlePagingSource(val articleRepo: ArticleRepo) : PagingSource<Int,HotChannel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HotChannel> {
        val page = params.key ?: 1
        return when (val res = articleRepo.getListChannel(page)) {
            is Resource.Success -> LoadResult.Page(
                data = res.data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (res.data.isEmpty()) null else page + 1
            )
            is Resource.Error -> LoadResult.Error(Exception(res.message))
        }
    }


}