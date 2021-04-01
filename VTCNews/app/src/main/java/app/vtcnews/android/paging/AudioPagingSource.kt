package app.vtcnews.android.paging

import androidx.paging.PagingSource
import app.vtcnews.android.model.Audio.AlbumPaging
import app.vtcnews.android.network.Resource
import app.vtcnews.android.repos.AudioRepo

class AudioPagingSource(val audioRepo: AudioRepo, val channelid: Long) :
    PagingSource<Int, AlbumPaging>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AlbumPaging> {
        val page = params.key ?: 1

        return when (val res = audioRepo.getAlbumPaging(channelid, page)) {
            is Resource.Success -> LoadResult.Page(
                data = res.data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (res.data.isEmpty()) null else page + 1
            )
            is Resource.Error -> LoadResult.Error(Exception(res.message))
        }
    }


}