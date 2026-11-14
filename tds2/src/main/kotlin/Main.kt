package pt.isel.tds2627

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import pt.isel.tds2627.console.getCommands
import pt.isel.tds2627.console.readCommand
import pt.isel.tds2627.data.MongoDriver
import pt.isel.tds2627.model.Game
import pt.isel.tds2627.model.GameSerializer
import pt.isel.tds2627.model.Player
import pt.isel.tds2627.model.show
import pt.isel.tds2627.storage.MongoStorage
import composeResources.*
import org.jetbrains.compose.resources.painterResource
import pt.isel.tds2627.resources.Res
import pt.isel.tds2627.resources.*


@Composable
fun App() {
    var player by remember { mutableStateOf(Player.X) }
    MaterialTheme {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PlayerView(250.dp, player)
            Button(
                onClick = { player = player.other }
            ) {
                Text("Other player")
            }
        }
    }
}


@Composable
fun PlayerView(size: Dp, player: Player?) {
   val modifier = Modifier.size(size).background(Color.White)
   if (player == null) Box(modifier)  // Empty square (size x size)
   else {
       val resource = when (player) {
           Player.X -> Res.drawable.cross
           Player.O -> Res.drawable.circle
       }
       Image(
           painter = painterResource(resource),
           contentDescription = null,
           modifier = modifier  // Image resized to (size x size)
       )
   }
}


@Composable
fun PlayerPreview() {
    PlayerView(100.dp, Player.O)
    PlayerView(50.dp, null)
    PlayerView(25.dp, Player.X)
}


fun main2() = application {
    Window(
        title = "Jogo do Galo",
        state = WindowState(size = DpSize.Unspecified),
        onCloseRequest = ::exitApplication,
        icon = painterResource(Res.drawable.cross)
    ) { App() }
}




fun main() = MongoDriver("galo").use {
    var game = Game()

    val commands = getCommands(MongoStorage("games", it, GameSerializer))

    // Versao FILE
    //val commands = getCommands(TextFileStorage("games", GameSerializer))

    while (true) {
        val (name, args) = readCommand()
        // Execute the command
        val cmd = commands[name]
        if (cmd==null) println("Invalid command $name")
        else try {
            game = cmd.execute(args, game)
            if (cmd.isToFinish) break
            game.show()
        } catch (e: IllegalStateException) {
            println(e.message)
        } catch (e: IllegalArgumentException) {
            println("${e.message}\nUse: $name ${cmd.argsSyntax}")
        }
    }
}
