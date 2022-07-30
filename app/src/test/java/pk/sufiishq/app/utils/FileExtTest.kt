package pk.sufiishq.app.utils

import VideoHandle.EpEditor
import VideoHandle.OnEditorListener
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.functions.Action
import org.apache.commons.io.IOUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import java.io.File
import java.io.FilenameFilter
import java.nio.charset.Charset

class FileExtTest : SufiIshqTest() {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testMoveTo_should_createThenCopySourceFile() {
        mockkStatic(Completable::fromAction)

        val sourceFile = File(context.filesDir, "source.txt")
        IOUtils.write("custom data", sourceFile.outputStream(), Charset.defaultCharset())

        val destination = File(context.filesDir, "destination.txt")

        val actionSlot = slot<Action>()
        every { Completable.fromAction(capture(actionSlot)) } returns mockk()

        assertTrue(sourceFile.exists())

        sourceFile.moveTo(destination)

        // execute runnable
        actionSlot.captured.run()

        val dataList = IOUtils.readLines(destination.inputStream(), Charset.defaultCharset())
        assertTrue(dataList.isNotEmpty())
        assertEquals("custom data", dataList[0])

    }

    @Test
    fun testDeleteContent_shouldDelete_allMP3Files() {

        mockkStatic(String::toFile)

        val mp3File = mockk<File> {
            every { delete() } returns true
        }

        val file = mockk<File> {
            every { list(any()) } answers {
                val filter = firstArg<FilenameFilter>()
                val data = mutableListOf<String>()
                arrayOf("first.mp3", "unknown.txt", "second.mp3").forEach {
                    if (filter.accept(mockk(), it)) {
                        data.add(it)
                    }
                }
                data.toTypedArray()
            }
            every { absolutePath } returns ""
            every { absolutePath.toFile(any()) } returns mp3File
        }

        file.deleteContent()

        verify(exactly = 2) { mp3File.delete() }
    }

    @Test
    fun testToFile_shouldReturn_FileObject() {
        val file = File(context.filesDir.absolutePath)
        assertEquals(
            "${file.absolutePath}${File.separator}file.mp3",
            file.absolutePath.toFile("file.mp3").absolutePath
        )
    }

    @Test
    fun testSplit_should_verifyAllStatus() {

        mockkStatic(EpEditor::execCmd)

        val command = slot<String>()
        val slot = slot<OnEditorListener>()

        every { EpEditor.execCmd(capture(command), any(), capture(slot)) } returns Unit

        val file = mockk<File> {
            every { absolutePath } returns "kalam/input.mp3"
        }

        val outFile = mockk<File> {
            every { absolutePath } returns "kalam/output.mp3"
        }

        val start = "00:10:20"
        val end = "00:03:40"

        var status = -255
        file.split(outFile, start, end) {
            status = it
        }

        assertEquals(
            "-y -i kalam/input.mp3 -ss 00:10:20 -codec copy -t 00:03:40 kalam/output.mp3",
            command.captured
        )

        slot.captured.onSuccess()
        assertEquals(SPLIT_SUCCESS, status)

        slot.captured.onFailure()
        assertEquals(SPLIT_CANCEL, status)

        slot.captured.onProgress(0f)
        assertEquals(SPLIT_IN_PROGRESS, status)

    }
}