package app.vtcnews.android.repos

import app.vtcnews.android.model.MenuItem
import app.vtcnews.android.network.MenuService
import app.vtcnews.android.network.Resource
import app.vtcnews.android.network.performNetworkCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuRepo @Inject constructor(private val menuService: MenuService) {
    var menuList = listOf<MenuItem>()
    var parentMenus = listOf<MenuItem>()

    suspend fun getMenuList(): Resource<List<MenuItem>> {
        val response = performNetworkCall { menuService.getMenuList() }
        if (response is Resource.Success) {
            menuList = response.data
            parentMenus =
                menuList.filter { it.parentId == null && it.orderMobile!! > 0 }
        }

        return response
    }
}
//&& it.isShowMenu?:true }.sortedBy { it.orderMobile }