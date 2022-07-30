package pk.sufiishq.app.helpers

sealed class Screen(val route: String) {

    object Dashboard : Screen("screen_dashboard")
    object Playlist : Screen("screen_playlist")
    object Help : Screen("screen_help")
    object Tracks : Screen("screen_tracks") {

        // params
        const val PARAM_TRACK_TYPE = "trackType"
        const val PARAM_TITLE = "title"
        const val PARAM_PLAYLIST_ID = "playlistId"

        // track types
        const val ALL = "all"
        const val DOWNLOADS = "downloads"
        const val FAVORITES = "favorites"
        const val PLAYLIST = "playlist"

    }

    fun withArgs(vararg args: String): String {

        return buildString {
            append(route)

            args.forEach { arg ->
                append("/").append(arg)
            }
        }
    }
}
