package pk.sufiishq.app.utils

import VideoHandle.EpEditor
import VideoHandle.OnEditorListener
import io.reactivex.Completable
import org.apache.commons.io.IOUtils
import timber.log.Timber
import java.io.File

fun File.moveTo(destination: File): Completable {
    return Completable.fromAction {
        IOUtils.copy(this.inputStream(), destination.outputStream())
        this.delete()
    }
}

fun File.deleteContent() {

    list { _, name ->
        name.endsWith("mp3")
    }?.forEach {
        try {
            absolutePath.toFile(it).delete()
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }
}

fun String.toFile(filename: String): File {
    return File(this, filename)
}

fun File.split(
    output: File,
    start: String,
    end: String,
    onComplete: (returnCode: Int) -> Unit
) {

    EpEditor.execCmd(
        "-y -i $absolutePath -ss $start -codec copy -t $end ${output.absolutePath}",
        0,
        object : OnEditorListener {
            override fun onSuccess() {
                onComplete(SPLIT_SUCCESS)
            }

            override fun onFailure() {
                onComplete(SPLIT_CANCEL)
            }

            override fun onProgress(progress: Float) {
                onComplete(SPLIT_IN_PROGRESS)
            }
        }
    )
}