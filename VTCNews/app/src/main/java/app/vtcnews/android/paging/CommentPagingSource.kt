package app.vtcnews.android.paging

import androidx.paging.PagingSource
import app.vtcnews.android.model.comment.CommentItem
import app.vtcnews.android.network.Resource
import app.vtcnews.android.repos.CommentRepo

class CommentPagingSource(
    val commentRepo: CommentRepo,
    private val id: Long
) : PagingSource<Int, CommentItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentItem> {
        val page = params.key ?: 1

        return when (val res = commentRepo.getComment(id, page)) {
            is Resource.Success -> LoadResult.Page(
                data = res.data.items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (res.data.items.isEmpty()) null else page + 1
            )
            is Resource.Error -> LoadResult.Error(Exception(res.message))
        }
    }


}

