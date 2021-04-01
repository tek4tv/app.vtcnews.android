package app.vtcnews.android.paging

import androidx.paging.PagingSource
import app.vtcnews.android.model.Video
import app.vtcnews.android.network.Resource
import app.vtcnews.android.repos.VideoRepo

class VideoMenuPagingSource(val videoRepo: VideoRepo) : PagingSource<Int, Video>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Video> {
        val page = params.key ?: 1
        return when (val res = videoRepo.getMenuVideosPaging(page)) {
            is Resource.Success -> LoadResult.Page(
                data = res.data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (res.data.isEmpty()) null else page + 1
            )
            is Resource.Error -> {
                LoadResult.Error(Exception(res.message))
            }
        }
    }


}