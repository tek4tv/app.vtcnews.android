package app.vtcnews.android.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.vtcnews.android.model.MenuItem
import app.vtcnews.android.repos.MenuRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrangChuSubSectionViewModel @ViewModelInject constructor(
    private val menuRepo: MenuRepo
) : ViewModel() {
    val menuItem  = MutableLiveData<List<MenuItem>>()

    fun getMenuItem(parentMenuId : Int)
    {
        viewModelScope.launch {
            var r = listOf<MenuItem>()
            withContext(Dispatchers.Default)
            {
                r = menuRepo.menuList.filter { it.parentId == parentMenuId }
            }

            menuItem.value = r
        }
    }
}