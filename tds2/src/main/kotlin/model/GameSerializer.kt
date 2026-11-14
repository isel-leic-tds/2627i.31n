package pt.isel.tds2627.model


object GameSerializer: Serializer<Game> {
    override fun serialize(data: Game): String = buildString {
        appendLine(data.first.name)
        appendLine(data.board.entries.joinToString(" ")
        { (pos, player) -> "${pos.index}:${player.name}" }
        )
        appendLine(GameStateSerializer.serialize(data.state))
        appendLine(data.score.entries.joinToString(" ")
        { (player, score) -> "${player?.name ?: "null"}=$score" }
        )
    }
    override fun deserialize(text: String): Game =
        text.split('\n').let { (l1, l2, l3, l4) -> Game(
            first = l1.toPlayer(),
            board = if (l2.isBlank()) emptyMap()
            else l2.split(' ')
                .map{ it.split(':') }
                .associate { (index, player) ->
                    Position(index.toInt()) to player.toPlayer()
                },
            state = GameStateSerializer.deserialize(l3),
            score = l4.split(' ')
                .map { it.split('=') }
                .associate { (player, score) ->
                    player.toPlayerOrNull() to score.toInt()
                }
        ) }
}
