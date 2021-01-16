package app.vtcnews.android.model

<<<<<<< Updated upstream
=======
import app.vtcnews.android.model.Article.Article
import app.vtcnews.android.model.Article.ChannelPaging.ItemChannel
import app.vtcnews.android.model.Audio.AlbumPaging

>>>>>>> Stashed changes
data class TrangChuData(
    var hotChannels: List<HotChannel> = listOf(),
    var hotArticles: List<Article> = listOf(),
    var articlesSuggestionsHome : List<Article> = listOf(),
<<<<<<< Updated upstream
    var videos : List<Video> = listOf()
=======
    var videos : List<Video> = listOf(),
    var audio : List<AlbumPaging> = listOf(),

>>>>>>> Stashed changes
)
