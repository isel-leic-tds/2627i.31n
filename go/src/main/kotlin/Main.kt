package pt.isel.tds2627

import okio.*
import okio.Path.Companion.toPath



// R1. Linha comando
// NEW, PLAY <pos>, EXIT
private const val EMPTY: Char = ' '
private const val FILENAME: String = "galo.txt"

data class Game (

    val first : Char = 'X',
    val turn : Char = first,


    val board: List<Char> = listOf(
        EMPTY, EMPTY, EMPTY, // 0, 1, 2
        EMPTY, EMPTY, EMPTY, // 3, 4, 5
        EMPTY, EMPTY, EMPTY  // 6, 7, 8
    )



)

// criar novo JOGO
fun Game.new () = Game ()

// print do tabuleiro
fun Game.show() {

    println("------------")
    println("NEXT: $turn")
    println("------------")
    println("")
    board.chunked(3).forEach {
        val linha = it
        println("${it[0]}|${it[1]}|${it[2]}")
        println("-+-+-")
    }
    println("")
    println("------------")
    println("WINNER: " + isWinner())
    println("------------")
}

fun Game.nexPlayer() : Char {
    if (turn=='X') return 'O' else return 'X'
}

fun Game.canPlay(pos : Int) : Boolean {
    return (board[pos] == EMPTY)
}

fun Game.play(pos : Int) : Game  {

    var next = turn
    val new_board = board.mapIndexed { index, elem ->
        if (index == pos)  next else elem
    }
    next = nexPlayer()
    return Game(turn,next, new_board)

}


fun Game.serialize(): String {
    return "$first|$turn|$board"
}

fun Game.deserialize() : Game {
    return Game()
}

fun Game.save() {
    val path = FILENAME.toPath()
    val me = this
    with (path) {
        FileSystem.SYSTEM.write(this,false) {
            writeUtf8(me.serialize() )
        }
    }
}

fun Game.load() {
    val path = FILENAME.toPath()

    val bytes: ByteArray = FileSystem.SYSTEM.read(path) {
        readByteArray()
    }

    println("Lidos ${bytes.size} bytes")
}

fun Game.isWinner() : Boolean {
    //
    return (board[0] == board[1] && board[0] == board[2] && board[0] != EMPTY)

}


fun main() {
    var game : Game = Game()





    while (true) {

        print("> ")
        val cmd = readLine()?.uppercase()?.split(" ")

        // Parse command
        val command =cmd!![0]
        when (command) {

            "NEW" -> {
                println("COMMAND ISSUED: $command")
                game = game.new()
                game.show()
                game.save()
                game.load()
            }
            "PLAY" -> {
                val pos =cmd!![1].toInt()
                println("COMMAND ISSUED: $command")
                println("POSITION ISSUED: $pos")

                if (pos >=0 && pos <=8 && game.canPlay(pos)) {
                    game = game.play(pos)
                    game.show()
                }
            }
            "EXIT" -> {
                break;
            }
            else -> {
                println("Unknown command $command")
            }

        }



        //

    }

    println("END")



}