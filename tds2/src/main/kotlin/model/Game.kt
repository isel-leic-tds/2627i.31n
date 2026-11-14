package pt.isel.tds2627.model

private const val EMPTY = ' '

const val BOARD_SIZE = 3
const val BOARD_CELLS = BOARD_SIZE * BOARD_SIZE

typealias Board = Map<Position, Player>
typealias Score = Map<Player?, Int>
private fun Score.advance(player: Player?): Score =
    plus(player to getValue(player) + 1)


data class Game(
    // The first player
    val first: Player = Player.X,
    // Current player turn
    val turn: Player = first,
    val score: Score = (Player.entries + null).associateWith { 0 },
    // Moves in each board position, where null means empty
    val board: Board = emptyMap(),
    // The current state of the game, initially running
    val state: GameState = Run(first),

)


fun Game.new() = Game(
    first = first.other,
    score = if (state is Run) score.advance(state.turn.other) else score,
)

//fun Game.canPlay(pos: Position) = board[pos.index] == null


fun Game.play(pos: Position): Game = when(state) {
    is Run -> {
        check(board[pos] == null) { "Position $pos is already used" }
        val board = board + Pair(pos, state.turn)
        copy( board = board, state = when {
            board.winnerIn(pos) -> Win(state.turn)
            board.size == BOARD_CELLS -> Draw
            else -> Run(state.turn.other)
        } )
    }
    is Win, is Draw -> error("Game is already finished")
}



// Check if the game has a winner with the given player
fun Board.winnerIn(pos: Position): Boolean {
    if (size < BOARD_SIZE * 2 - 1) return false
    val player = this[pos]
    val playerMoves = filterValues { it == player }.keys
    return listOf<(Position) -> Boolean>( // Conditions to check
        { it.row == pos.row },
        { it.col == pos.col },
        { it.backSlash },
        { it.slash }
    ).any { playerMoves.count(it) == BOARD_SIZE }
}



private val separator = "---+".repeat(BOARD_SIZE-1)+"---"


// Show the current game state in the console
fun Game.show() {
    // Show the board
    Position.values.chunked(BOARD_SIZE).forEach { line ->
        val plays = line.map { board[it]?.name ?: " " }
        println(plays.joinToString(separator = " | ", prefix = " "))
        if (line.first().row < BOARD_SIZE - 1) println(separator)
    }
    // Show the game status
    println( when(state) {
        is Win -> "Winner: ${state.winner}"
        is Draw -> "Draw"
        is Run -> "Turn: ${state.turn}"
    })
}

