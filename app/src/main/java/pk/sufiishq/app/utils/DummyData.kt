package pk.sufiishq.app.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.helpers.PlayerState
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamItemParam
import pk.sufiishq.app.models.Playlist
import java.io.File

// ----------------------------------------- //
// PLAYER DATA PROVIDER
// ----------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun dummyPlayerDataProvider() = object : PlayerDataProvider {

    override fun getSeekbarValue(): LiveData<Float> {
        return MutableLiveData(0f)
    }

    override fun updateSeekbarValue(value: Float) { /* no comment */
    }

    override fun getSeekbarAccess(): LiveData<Boolean> {
        return MutableLiveData(true)
    }

    override fun onSeekbarChanged(value: Float) { /* no comment */
    }

    override fun getPlayerState(): LiveData<PlayerState> {
        return MutableLiveData(PlayerState.IDLE)
    }

    override fun doPlayOrPause() { /* no comment */
    }

    override fun getActiveKalam(): LiveData<Kalam?> {
        return MutableLiveData(Kalam(0, "Kalam Title", 1, "1991", "Karachi", "", "", 0, 0))
    }

    override fun changeTrack(kalam: Kalam) { /* no comment */
    }

    override fun getDownloadProgress(): LiveData<Float> {
        return MutableLiveData(0f)
    }

    override fun getDownloadError(): LiveData<String> {
        return MutableLiveData("")
    }

    override fun setDownloadError(error: String) { /* no comment */
    }

    override fun startDownload(kalam: Kalam) { /* no comment */
    }

    override fun disposeDownload() { /* no comment */
    }
}

// ----------------------------------------- //
// KALAM DATA PROVIDER
// ----------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun dummyKalamDataProvider() = object : KalamDataProvider {

    override fun init(trackType: String, playlistId: Int) { /* no comment */
    }

    override fun getKalamDataFlow(): Flow<PagingData<Kalam>> {
        return emptyFlow()
    }

    override fun searchKalam(keyword: String, trackType: String, playlistId: Int) { /* no comment */
    }

    override fun update(kalam: Kalam) { /* no comment */
    }

    override fun delete(kalam: Kalam, trackType: String) { /* no comment */
    }

    override fun save(sourceKalam: Kalam, splitFile: File, kalamTitle: String) { /* no comment */
    }

    override fun countAll(): LiveData<Int> = MutableLiveData(0)

    override fun countFavorites(): LiveData<Int> = MutableLiveData(0)

    override fun countDownloads(): LiveData<Int> = MutableLiveData(0)

    override fun markAsFavorite(kalam: Kalam) { /* no comment */
    }

    override fun removeFavorite(kalamItemParam: KalamItemParam) { /* no comment */
    }
}

@ExcludeFromJacocoGeneratedReport
fun dummyTrack() = Kalam(1, "Kalam Title", 2, "1993", "Karachi", "", "", 0, 0)

// ----------------------------------------- //
// PLAYLIST DATA PROVIDER
// ----------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun dummyPlaylistDataProvider() = object : PlaylistDataProvider {

    override fun getAll(): LiveData<List<Playlist>> = MutableLiveData(
        listOf(
            Playlist(1, "Karachi"),
            Playlist(2, "Lahore")
        )
    )

    override fun get(id: Int) = MutableLiveData(Playlist(1, "Karachi"))

    override fun add(playlist: Playlist) { /* no comment */
    }

    override fun update(playlist: Playlist) { /* no comment */
    }

    override fun delete(playlist: Playlist) { /* no comment */
    }

    override fun countAll(): LiveData<Int> = MutableLiveData(0)
}

@ExcludeFromJacocoGeneratedReport
fun dummyPlaylist() = Playlist(1, "Karachi")