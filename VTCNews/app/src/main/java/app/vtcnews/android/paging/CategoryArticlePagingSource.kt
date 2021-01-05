package app.vtcnews.android.paging

import androidx.paging.PagingSource
import app.vtcnews.android.model.Article
import app.vtcnews.android.network.Resource
import app.vtcnews.android.repos.ArticleRepo

class CategoryArticlePagingSource (
    private val articleRepo: ArticleRepo,
    private val categoryId : Int
) : PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1

        return when (val res = articleRepo.getArticleByCategory(page, categoryId.toLong())) {
            is Resource.Success -> LoadResult.Page(
                data = res.data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (res.data.isEmpty()) null else page + 1
            )
            is Resource.Error -> LoadResult.Error(Exception(res.message))
        }
    }
}