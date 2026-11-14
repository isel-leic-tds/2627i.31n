package pt.isel.tds2627.model

sealed class GameState


data class Run(val turn: Player): GameState()
data class Win(val winner: Player): GameState()
object Draw: GameState()


