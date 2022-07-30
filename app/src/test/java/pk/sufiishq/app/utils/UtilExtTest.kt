package pk.sufiishq.app.utils

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.*
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.models.Kalam

class UtilExtTest : SufiIshqTest() {

    @Test
    fun testOptValue_shouldReturn_defaultValue() {
        val testState = mutableStateOf<String?>(null)
        assertEquals("test", testState.optValue("test"))
    }

    @Test
    fun testOptValue_shouldReturn_actualValue() {
        val testState = mutableStateOf("test")
        assertEquals("test", testState.optValue("test"))
    }

    @Test
    fun testCopyAsNew_shouldReturn_deepCopyAlongWithDefaultValue() {
        val kalam = sampleKalam()
        val copyKalam = kalam.copyWithDefaults()

        assertEquals(kalam.hashCode(), copyKalam.hashCode())

        // verify kalam assertion
        verifyKalamAssertion(kalam, copyKalam, true)
    }

    @Test
    fun testCopyAsNew_shouldReturn_deepCopyAlongWithUserDefinedValue() {
        val kalam = sampleKalam()
        val copyKalam = kalam.copyWithDefaults(
            id = 2,
            title = "new-title",
            code = 2,
            year = "1999",
            location = "Lahore",
            onlineSource = "online-src",
            offlineSource = "offline-src",
            isFavorite = 0,
            playlistId = 2
        )

        // both object should point different memory reference
        assertNotEquals(kalam.hashCode(), copyKalam.hashCode())

        // verify kalam assertion
        verifyKalamAssertion(kalam, copyKalam, false)
    }

    @Test
    fun testObserveOnce_shouldObserve_onlyOneTime() {
        val data = MutableLiveData("data")
        data.observeOnce(mockLifecycleOwner()) {
            assertEquals("data", it)
        }
        data.postValue("new data")
    }

    @Test
    fun testCheckValue_shouldReturn_notEmptyValue() {
        assertEquals("not_empty", "data".checkValue("not_empty", "empty"))
    }

    @Test
    fun testCheckValue_shouldReturn_emptyValue() {
        assertEquals("empty", "".checkValue("not_empty", "empty"))
    }

    @Test
    fun testIfNotEmpty_shouldPost_callbackWithTrimValue() {
        "    data     ".ifNotEmpty {
            assertEquals("data", it)
        }
    }

    @Test
    fun testHasOfflineSource_shouldReturn_true() {
        val kalam = mockk<Kalam> {
            every { offlineSource } returns "offline-src"
        }
        assertTrue(kalam.hasOfflineSource())
    }

    @Test
    fun testHasOfflineSource_shouldReturn_false() {
        val kalam = mockk<Kalam> {
            every { offlineSource } returns ""
        }
        assertFalse(kalam.hasOfflineSource())
    }

    @Test
    fun testCanPlay_shouldReturn_true() {
        mockkStatic(Context::isNetworkAvailable)
        mockkStatic(Kalam::hasOfflineSource)

        val context = mockk<Context> {
            every { isNetworkAvailable() } returns true
        }

        val kalam = mockk<Kalam> {
            every { hasOfflineSource() } returns true
        }

        assertTrue(kalam.canPlay(context))
    }

    @Test
    fun testCanPlay_shouldReturn_false() {
        mockkStatic(Context::isNetworkAvailable)
        mockkStatic(Context::toast)
        mockkStatic(Kalam::hasOfflineSource)

        val context = mockk<Context> {
            every { isNetworkAvailable() } returns false
            every { toast(any()) } returns Unit
        }

        val kalam = mockk<Kalam> {
            every { hasOfflineSource() } returns false
        }

        assertFalse(kalam.canPlay(context))
    }

    private fun verifyKalamAssertion(
        expectedKalam: Kalam,
        actualKalam: Kalam,
        checkEquality: Boolean
    ) {
        if (checkEquality) {
            assertEquals(expectedKalam.id, actualKalam.id)
            assertEquals(expectedKalam.title, actualKalam.title)
            assertEquals(expectedKalam.code, actualKalam.code)
            assertEquals(expectedKalam.year, actualKalam.year)
            assertEquals(expectedKalam.location, actualKalam.location)
            assertEquals(expectedKalam.onlineSource, actualKalam.onlineSource)
            assertEquals(expectedKalam.offlineSource, actualKalam.offlineSource)
            assertEquals(expectedKalam.isFavorite, actualKalam.isFavorite)
            assertEquals(expectedKalam.playlistId, actualKalam.playlistId)
        } else {
            assertNotEquals(expectedKalam.id, actualKalam.id)
            assertNotEquals(expectedKalam.title, actualKalam.title)
            assertNotEquals(expectedKalam.code, actualKalam.code)
            assertNotEquals(expectedKalam.year, actualKalam.year)
            assertNotEquals(expectedKalam.location, actualKalam.location)
            assertNotEquals(expectedKalam.onlineSource, actualKalam.onlineSource)
            assertNotEquals(expectedKalam.offlineSource, actualKalam.offlineSource)
            assertNotEquals(expectedKalam.isFavorite, actualKalam.isFavorite)
            assertNotEquals(expectedKalam.playlistId, actualKalam.playlistId)
        }
    }

    private fun sampleKalam() = Kalam(
        1,
        "kalam-title",
        1,
        "1990",
        "Karachi",
        "kalam-online-src",
        "kalam-offline-src",
        1,
        1
    )
}