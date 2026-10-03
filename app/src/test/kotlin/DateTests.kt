import kotlin.test.*
import pt.isel.tds.Date
@OptIn(ExperimentalKotlinTestApi::class)

/*
Requisito inicial: Pretende-se uma classe Date para representar datas com dia, mês e ano.
Deve ser possível criar valores usando Date(2025, 7, 23) para atribuir valores às respectivas propriedades year, month e day.
 */


class DateTests {

    // Testar a criação de um objeto do tipo
/*
    @Test fun newDateTest() {

        val sut = Date(2025, 7, 23)
        assertEquals(2025, sut.year)
        assertEquals(7, sut.month)
        assertEquals(23, sut.day)


        assertNotEquals(2026, sut.year)

    }

    @Test fun alternateConst() {

        val sut = Date(month=7, year=2025)
        val sut2 = Date(month=7, year=2020)
        /*
        assertEquals (1, sut.day) {"Data sem dia na criação"}
        assertEquals (2025, sut.year) {"Data sem dia na criação"}
        assertEquals (7, sut.month) {"Data sem dia na criação"}

         */

        assertEquals (false, sut.leapYear) {"Não é bisexto"}
        assertEquals (true, sut2.leapYear) {"2020 é Esperado ser bisexto"}



        /*
        Date(2025,3).lastDayOfMonth deve ser 31, porque março tem 31 dias,
        mas Date(2020,2).lastDayOfMonth deve ser 29,
        porque 2020 é um ano bisexto e nesse caso fevereiro tem 29 dias.
         */



 */
        @Test fun testUltimoDiaMes() {

            val sut = Date(month=3, year=2025)
            val sut2 = Date(month=2, year=2020)

            assertEquals(31, sut.lastDayOfMonth) {"Esperado 31"}
            assertEquals(29, sut2.lastDayOfMonth) {"Esperado 29"}

        }

    }




