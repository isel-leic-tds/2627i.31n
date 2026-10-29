package pt.isel.tds2627

object GameSerializer : ISerializer {
    override fun serialize(game: Game): String {
        return "${game.first}"
    }

    override fun deserialize(text: String): Game {
        return Game (text.first())
    }


}
