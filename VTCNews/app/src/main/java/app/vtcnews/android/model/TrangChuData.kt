package app.vtcnews.android.model

import app.vtcnews.android.model.Audio.AlbumPaging

data class TrangChuData(
    var hotChannels: List<HotChannel> = listOf(),
    var hotArticles: List<Article> = listOf(),
    var articlesSuggestionsHome: List<Article> = listOf(),
    var videos: List<Video> = listOf(),
    var audioPD: List<AlbumPaging> = listOf(),
    var audioMusic: List<AlbumPaging> = listOf(),
    var audioBook: List<AlbumPaging> = listOf()
)
