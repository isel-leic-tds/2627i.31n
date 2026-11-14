package pt.isel.tds2627.console

data class CommandLine(val name: String, val args: List<String>)


tailrec fun readCommand(): CommandLine {
    print("> ")
    val line = readln().split(' ').filter(String::isNotBlank)
    return if (line.isEmpty()) readCommand()
    else CommandLine(line.first().uppercase(), line.drop(1))
}

