import kotlin.test.*
import pt.isel.tds.Date;

class DateTests {
    @OptIn(ExperimentalKotlinTestApi::class)
    @Test fun createDate() {
        val sut = Date(2025, 7, 23)
        assertEquals(2025, sut.year) {"Error in year"}
        assertEquals(7, sut.month) {"Error in month"}
        assertEquals(23, sut.day) {"Error in day"}
    }
}