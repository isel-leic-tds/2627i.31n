package pt.isel.tds2627

interface ISerializer {
    fun serialize(data: Game): String
    fun deserialize(text: String): Game
}
