/*

- Class valor
- Data class pode ser usada?
- funclções utilitarias
- Exemplos hands-on



 */

package pt.isel.tds

private val daysOfMonth = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
private const val GREGORIAN_START_YEAR = 1582
private const val YEAR_LIMIT = 2200
private val YEAR_RANGE = GREGORIAN_START_YEAR..YEAR_LIMIT
private val MONTHS_IN_YEAR = daysOfMonth.size


class Date(val year: Int, val month: Int = 1, val day: Int = 1) {
    init {
        require(year in YEAR_RANGE){ "Invalid year=$year" }
        require(month in 1..MONTHS_IN_YEAR){ "Invalid month=$month" }
        //require(day in 1..lastDayOfMonth){ "Invalid day=$day" }
    }


    override fun toString(): String {

        return "$day/$month/$year"

    }




    override fun equals(other: Any?): Boolean {

        val isDate = other is Date

        return (isDate && this.year == other.year && this.month == other.month && this.day == other.day)


        //return super.equals(other)
    }
}

/*

class Date (val year: Int, val month: Int, val day: Int = 1) {

    private var daysOfMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    val leapYear get() = year%4 == 0 && year%100 != 0 || year%400 == 0


    val lastDayOfMonth: Int get() {

        return if ( this.month==2 && leapYear  ) 29 else daysOfMonth[month-1]

    }
*/
    /*
    override fun toString(): String {
        return "day: $day; year: $year; month: $month; "
    }

    override fun equals(other: Any?): Boolean {


        if (this === other) return true
        if (other !is Date) return false
        if (this.year == other.year) return true
        return false
    }
    */

    // algoritmo Bisexto



fun main() {
    val name = "Kotlin"

    var data_a = Date(2000, 1, 31)
    var data_b = Date(2000, 1, 1)


    //val igual = data_a.equals(1)

    //println("igual: $igual")

    println("A nossa data: $data_a")
    println("A nossa data: $data_b")


    //imutableDate = Date(2020, 12, 25)
    //imutableDate.day =26
    //val imutableDate2 = Date(2020, 12, 25)

    //
    // val eq = (imutableDate.equals(imutableDate2))

    //7println("eq, $eq!")
    //println("Hello, $name!")
    //println("It's, $imutableDate!")
}


