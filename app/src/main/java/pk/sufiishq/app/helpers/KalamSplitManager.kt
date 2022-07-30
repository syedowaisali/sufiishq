package pk.sufiishq.app.helpers

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.*
import java.io.File
import javax.inject.Inject

class KalamSplitManager @Inject constructor(@ApplicationContext val appContext: Context) {

    private val splitStatus = MutableLiveData<SplitStatus>(SplitCompleted())
    private val splitStart = MutableLiveData(0)
    private val splitEnd = MutableLiveData(0)

    private val kalamLength = MutableLiveData(0)
    private val kalamPreviewLength = MutableLiveData(0)
    private val previewPlayStart = MutableLiveData(false)
    private val previewKalamProgress = MutableLiveData(0)

    private val mediaPlayer = PreviewAudioPlayer().apply {

        setOnCompletionListener {
            releaseProgressListener()
            previewKalamProgress.value = 0
            previewPlayStart.value = false
        }

        setOnProgressListener {
            previewKalamProgress.value = it
        }
    }

    private var kalam: Kalam? = null

    fun setKalam(kalam: Kalam) {
        this.kalam = kalam
        val duration =
            mediaPlayer.getDuration(appContext.filesDir.absolutePath + "/" + kalam.offlineSource)
        setSplitEnd(duration)
        kalamLength.value = duration

    }

    fun startPreview() {
        previewKalamProgress.value = 0
        setSplitStatus(SplitInProgress)
        kalam?.let {
            val sourceFile = File(appContext.filesDir.absolutePath + "/" + it.offlineSource)
            val outFile = File(appContext.cacheDir, CACHE_SPLIT_FILENAME)
            sourceFile.split(
                outFile,
                splitStart.value!!.formatTime,
                (splitEnd.value!! - splitStart.value!!).formatTime
            ) { returnCode ->

                if (returnCode == SPLIT_IN_PROGRESS) return@split

                if (returnCode == SPLIT_SUCCESS) {
                    val duration = mediaPlayer.getDuration(outFile.absolutePath)
                    kalamPreviewLength.postValue(duration)
                }

                setSplitStatus(SplitCompleted(returnCode))
            }
        }
    }

    fun playPreview() {

        SufiIshqApp.getInstance().getPlayerController()?.let { playerController ->
            if (playerController.isPlaying()) playerController.doPause()
        }

        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            previewPlayStart.value = false
        } else {
            mediaPlayer.start()
            previewPlayStart.value = true
        }
    }

    fun setSplitStart(start: Int) {
        splitStart.value = start
    }

    fun setSplitEnd(end: Int) {
        splitEnd.value = end
    }

    fun setSplitStatus(status: SplitStatus) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            previewPlayStart.value = false
        }
        splitStatus.postValue(status)
    }

    fun updateSeekbarValue(value: Float) {
        previewKalamProgress.value = value.toInt()
        mediaPlayer.seekTo(value.toInt())
    }

    fun getKalam() = kalam!!
    fun getSplitFile() = File(appContext.cacheDir, CACHE_SPLIT_FILENAME)
    fun newInstance(context: Context) = KalamSplitManager(context)
    fun getSplitStatus(): LiveData<SplitStatus> = splitStatus
    fun getSplitStart(): LiveData<Int> = splitStart
    fun getSplitEnd(): LiveData<Int> = splitEnd
    fun getKalamLength(): LiveData<Int> = kalamLength
    fun getKalamPreviewLength(): LiveData<Int> = kalamPreviewLength
    fun getPreviewPlayStart(): LiveData<Boolean> = previewPlayStart
    fun getPreviewKalamProgress(): LiveData<Int> = previewKalamProgress
}