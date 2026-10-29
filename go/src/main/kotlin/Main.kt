package pt.isel.tds2627

import okio.*
import okio.Path.Companion.toPath



// R1. Linha comando
// NEW, PLAY <pos>, EXIT
private const val EMPTY: Char = ' '
private const val KEY: String = "jogo.txt"

// GAME CLASSS
data class Game (

    val first : Char = 'X',
    val turn : Char = first,
    // BOARD
    val board: List<Char> = listOf(
        EMPTY, EMPTY, EMPTY, // 0, 1, 2
        EMPTY, EMPTY, EMPTY, // 3, 4, 5
        EMPTY, EMPTY, EMPTY  // 6, 7, 8
    )
)

// NEW
fun Game.new () = Game ()

// SHOW
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

// NEXT PLAYER
fun Game.nexPlayer() : Char {
    if (turn=='X') return 'O' else return 'X'
}

// CAN PLAY
fun Game.canPlay(pos : Int) : Boolean {
    return (board[pos] == EMPTY)
}

// PLAY
fun Game.play(pos : Int) : Game  {

    var next = turn
    val new_board = board.mapIndexed { index, elem ->
        if (index == pos)  next else elem
    }
    next = nexPlayer()
    return Game(turn,next, new_board)

}

// WINNER
fun Game.isWinner() : Boolean {
    //
    return (board[0] == board[1] && board[0] == board[2] && board[0] != EMPTY)

}

// -------------- NEXT CLASS --------------

// SAVE
fun Game.save() {
    val store : IStorage = FileStorage(GameSerializer)
    store.create(KEY, this)
}

// LOAD
fun Game.load() : Game? {
    val store : IStorage = FileStorage(GameSerializer)
    return store.read(KEY)
}



fun main() {
    var game : Game = Game()





    while (true) {

        print("> ")
        val cmd = readLine()?.uppercase()?.split(" ")

        // Parse command
        val command =cmd!![0]
        when (command) {
            "LOAD" -> {
                println("COMMAND ISSUED: $command")
                game = game.new()
                game = game.load()!!
            }
            "NEW" -> {
                println("COMMAND ISSUED: $command")
                game = game.new()
                game.show()
            }
            "PLAY" -> {
                val pos =cmd!![1].toInt()
                println("COMMAND ISSUED: $command")
                println("POSITION ISSUED: $pos")

                if (pos >=0 && pos <=8 && game.canPlay(pos)) {
                    game = game.play(pos)
                    game.show()
                    game.save()
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