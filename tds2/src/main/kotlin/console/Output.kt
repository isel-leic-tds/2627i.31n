package pt.isel.tds2627.console

import pt.isel.tds2627.model.Game

// Show the current score
fun Game.showScore() {
    score.entries.joinToString(",", "Scores:") { (player,points) ->
        " ${player?:"Draws"}=$points"
    }.let(::println)
}
