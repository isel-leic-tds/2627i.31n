package pt.isel.tds2627.model

sealed class GameState {
}

//    val turn: Nothing?

data class Run(val turn: Player): GameState()
data class Win(val winner: Player): GameState()
object Draw: GameState()


