package app.vtcnews.android.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.vtcnews.android.model.Article
import app.vtcnews.android.model.HotChannel
import app.vtcnews.android.model.MenuItem
import app.vtcnews.android.model.TrangChuData
import app.vtcnews.android.network.Resource
import app.vtcnews.android.repos.ArticleRepo
import app.vtcnews.android.repos.MenuRepo
import kotlinx.coroutines.launch

class TrangChuFragViewModel @ViewModelInject constructor(
    private val menuRepo: MenuRepo,
    private val articleRepo: ArticleRepo
) : ViewModel() {
    val menuList = MutableLiveData<List<MenuItem>>()
    val error = MutableLiveData<String>()

    val data = MutableLiveData<TrangChuData>()

    fun getMenuList() {
        viewModelScope.launch {
            when (val res = menuRepo.getMenuList()) {
                is Resource.Success -> menuList.value = menuRepo.parentMenus
                is Resource.Error -> error.value = res.message
            }
        }
    }

    fun getData() {
        viewModelScope.launch {
            var hotChannels = listOf<HotChannel>()
            when (val res = articleRepo.getHotChannels()) {
                is Resource.Success -> hotChannels = res.data
                is Resource.Error -> error.value = res.message
            }

            var hotArticles = listOf<Article>()
            when (val res = articleRepo.getHotArticles()) {
                is Resource.Success -> hotArticles = res.data
                is Resource.Error -> error.value = res.message
            }

            var articleSuggestionHome = listOf<Article>()
            when (val res = articleRepo.getArticleSuggestionHome()) {
                is Resource.Success -> articleSuggestionHome = res.data
                is Resource.Error -> error.value = res.message
            }

            data.value = TrangChuData(
                hotChannels = hotChannels,
                hotArticles = hotArticles,
                articlesSuggestionsHome = articleSuggestionHome
            )
        }
    }

}