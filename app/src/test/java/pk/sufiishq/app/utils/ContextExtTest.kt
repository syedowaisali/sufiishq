package pk.sufiishq.app.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.widget.Toast
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest

class ContextExtTest : SufiIshqTest() {

    @Test
    fun testIsNetworkAvailable_shouldReturn_true() {
        mockkStatic(NetworkCapabilities::hasAnyOneTransport)

        val context = mockContext(true)
        assertTrue(context.isNetworkAvailable())
    }

    @Test
    fun testIsNetworkAvailable_shouldReturn_false() {
        mockkStatic(NetworkCapabilities::hasAnyOneTransport)

        val context = mockContext(false)
        assertFalse(context.isNetworkAvailable())
    }

    @Test
    fun testHasAnyOneTransport_shouldReturn_true() {
        val capabilities = mockNetworkCapabilities()

        assertTrue(capabilities.hasAnyOneTransport(TRANSPORT_CELLULAR))
        assertTrue(capabilities.hasAnyOneTransport(TRANSPORT_WIFI))
        assertTrue(capabilities.hasAnyOneTransport(TRANSPORT_ETHERNET))
    }

    @Test
    fun testHasAnyOneTransport_shouldReturn_false() {
        val capabilities = mockNetworkCapabilities()

        assertFalse(capabilities.hasAnyOneTransport(TRANSPORT_USB))
        assertFalse(capabilities.hasAnyOneTransport(TRANSPORT_BLUETOOTH))
        assertFalse(capabilities.hasAnyOneTransport(TRANSPORT_VPN))
    }

    @Test
    fun testDpToPx_shouldReturn_convertedValue() {

        val context = mockk<Context> {
            every { resources } returns mockk {
                every { displayMetrics } returns mockk {
                    density = 2f
                }
            }
        }

        assertEquals(20f, context.dpToPx(10f))
    }

    @Test
    fun testToast_shouldVerify_makeTextAndShowMethod() {
        mockkStatic(Toast::class)

        var toastLength = Toast.LENGTH_SHORT
        var toastMessage = ""

        val toast = mockk<Toast>()
        every { toast.show() } returns Unit
        every { Toast.makeText(any(), any<String>(), any()) } answers {
            toastLength = thirdArg()
            toastMessage = secondArg()
            toast
        }

        val context = mockk<Context>()
        context.toast("toast showing")

        verify(exactly = 1) { toast.show() }

        assertEquals(toastLength, Toast.LENGTH_LONG)
        assertEquals("toast showing", toastMessage)
    }

    private fun mockNetworkCapabilities(): NetworkCapabilities {
        return mockk {
            every { hasTransport(any()) } answers {
                when (firstArg<Int>()) {
                    TRANSPORT_CELLULAR, TRANSPORT_WIFI, TRANSPORT_ETHERNET -> true
                    else -> false
                }
            }
        }
    }

    private fun mockContext(supportAnyOneTransport: Boolean): Context {
        val capabilities = mockk<NetworkCapabilities>()
        every { capabilities.hasAnyOneTransport(*anyIntVararg()) } returns supportAnyOneTransport

        val connectivityManager = mockk<ConnectivityManager>()
        every { connectivityManager.activeNetwork } returns mockk()
        every { connectivityManager.getNetworkCapabilities(any()) } returns capabilities

        val context = mockk<Context>()
        every { context.getSystemService(any()) } returns connectivityManager

        return context
    }
}