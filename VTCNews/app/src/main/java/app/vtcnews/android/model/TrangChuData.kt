package app.vtcnews.android.model

data class TrangChuData(
    var hotChannels: List<HotChannel> = listOf(),
    var hotArticles: List<Article> = listOf(),
    var articlesSuggestionsHome : List<Article> = listOf(),
    var videos : List<Video> = listOf()
)
