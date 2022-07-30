package pk.sufiishq.app.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest

class TimeExtTest : SufiIshqTest() {

    @Test
    fun testFormatTime_shouldReturn_defaultFormattedTime() {
        assertEquals("00:00:00", 0.formatTime)
    }

    @Test
    fun testFormatTime_shouldReturn_validFormattedTime() {
        val dayInMills = 86400000
        assertEquals("24:00:00", dayInMills.formatTime)
    }
}