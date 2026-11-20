package pt.isel.tds2627.console
import pt.isel.tds2627.model.*
import pt.isel.tds2627.storage.Storage

class Command(
    val argsSyntax: String = "",
    val isToFinish: Boolean = false,
    val execute: (args: List<String>, Game) -> Game = { _, g -> g },
)

private fun storageCommand(exec: (Game, String) -> Game) =
    Command("<name>") { args, game ->
        val name = requireNotNull(args.firstOrNull()) { "Missing name" }
        val isValidName = name.matches( Regex("[a-zA-Z][a-zA-Z0-9_]*") )
        require(isValidName) { "Invalid name $name" }
        exec(game, name)
    }


private val Play = Command("<position>") { args, game ->
    val arg = requireNotNull(args.firstOrNull()) { "Missing position" }
    val pos = requireNotNull(arg.toIntOrNull()?.toPositionOrNull())
    { "Invalid position $arg, must be in 0..${BOARD_CELLS-1}" }
    game.play(pos)
}


typealias GameStorage = Storage<String, Game>

open class Clash(val gs: GameStorage)


class ClashRun(
    gs: GameStorage,
    val id: Name,
    val sidePlayer: Player,
    val game: Game
) : Clash(gs)


val ClashRun.canNew get() = sidePlayer == when (game.state) {
    is Run -> game.state.turn
    else -> game.first.other
}

private fun Clash.runOper(actions: ClashRun.()->Game): Clash {
    check(this is ClashRun) { "Clash not started" }
    return ClashRun(gs, id, sidePlayer, actions())
}
fun Clash.deleteIfIsOwner() {
    if (this is ClashRun && sidePlayer==Player.X) gs.delete(id.toString())
}

fun Clash.start(name: Name) = ClashRun(gs, name, Player.X, Game().also {
    gs.create(name.toString(), it)
    deleteIfIsOwner()
})


fun Clash.join(name: Name) = gs.read(name.toString())?.let {
    deleteIfIsOwner()
    ClashRun(gs, name, Player.O, it)
} ?: error("Clash $name not found")

fun Clash.play(pos: Position) = runOper { game.play(pos).also {
    check((game.state as Run).turn == sidePlayer) {"Not your turn"}
    gs.update(id.toString(),it)
}}

fun Clash.new() = runOper {
    check(canNew) { "Cannot create a new board" }
    game.new().also { gs.update(id.toString(),it) }
}


fun Clash.refresh() = runOper { (gs.read(id.toString()) as Game).also {
    check(game!=it) { "No changes" }
} }




fun getCommands(st: GameStorage): Map<String, Command> = mapOf(
    "PLAY" to Play,
    "NEW" to Command { _, game -> game.new() },
    "EXIT" to Command(isToFinish = true),
    "SCORE" to Command { _, game -> game.apply { showScore() } },
    "SAVE" to storageCommand { game, name -> game.also {
        st.create(name, game)
    } },
    "LOAD" to storageCommand { _, name ->
        checkNotNull(st.read(name)) { "Game $name not found" }
    },
    )


