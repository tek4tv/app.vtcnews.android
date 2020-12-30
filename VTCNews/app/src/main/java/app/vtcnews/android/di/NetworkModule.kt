package app.vtcnews.android.di

import app.vtcnews.android.network.ArticleService
import app.vtcnews.android.network.Audio.AllPodcastService
import app.vtcnews.android.network.MenuService
import app.vtcnews.android.network.VideoService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.vtcnews.tek4tv.vn/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideMenuService(retrofit: Retrofit) : MenuService =
        retrofit.create(MenuService::class.java)

    @Provides
    @Singleton
    fun provideArticleService(retrofit: Retrofit) : ArticleService =
        retrofit.create(ArticleService::class.java)

    @Provides
    @Singleton
    fun provideVideoService(retrofit: Retrofit) : VideoService =
        retrofit.create(VideoService::class.java)
		
	@Provides
    @Singleton	
    fun provideAudioService(retrofit: Retrofit) : AllPodcastService =
        retrofit.create(AllPodcastService::class.java)


}