package app.vtcnews.android.network

import app.vtcnews.android.model.MenuItem
import retrofit2.Response
import retrofit2.http.GET

interface MenuService {
    @GET("home/news/menu")
    suspend fun getMenuList(): Response<List<MenuItem>>
}