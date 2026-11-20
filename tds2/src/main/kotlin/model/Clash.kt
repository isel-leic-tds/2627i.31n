package pt.isel.tds2627.model

import pt.isel.tds2627.storage.Storage

typealias GameStorage = Storage<Name, Game>


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
    if (this is ClashRun && sidePlayer==Player.X) gs.delete(id)
}

fun Clash.start(name: Name) = ClashRun(gs, name, Player.X, Game().also {
    gs.create(name, it)
    deleteIfIsOwner()
})
fun Clash.join(name: Name) = gs.read(name)?.let {
    deleteIfIsOwner()
    ClashRun(gs, name, Player.O, it)
} ?: error("Clash $name not found")


fun Clash.play(pos: Position) = runOper { game.play(pos).also {
    check((game.state as Run).turn == sidePlayer) {"Not your turn"}
    gs.update(id,it)
}}
fun Clash.new() = runOper {
    check(canNew) { "Cannot create a new board" }
    game.new().also { gs.update(id,it) }
}
/*
fun Clash.refresh() = runOper { (gs.read(id) as Game).also {
    check(game!=it) { "No changes" }
} }
*/


fun Clash.refresh() = runOper {
    (gs.read(id) ?: throw GameDeletedException()).also {
        if (game == it) throw NoChangesException()
    }  }
