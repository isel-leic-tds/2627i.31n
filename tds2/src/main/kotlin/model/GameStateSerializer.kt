package pt.isel.tds2627.model

object GameStateSerializer: Serializer<GameState> {
    override fun serialize(data: GameState) = when (data) {
        is Run -> "Run:${data.turn.name}"
        is Win -> "Win:${data.winner.name}"
        Draw -> "Draw:"
    }
    override fun deserialize(text: String): GameState =
        text.split(':').let { (type, player) ->
            when (type) {
                "Run" -> Run(player.toPlayer())
                "Win" -> Win(player.toPlayer())
                "Draw" -> Draw
                else -> error("Unknown game state: $type")
            }
        }
}
