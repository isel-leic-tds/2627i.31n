package pt.isel.tds2627

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import pt.isel.tds2627.console.*
import pt.isel.tds2627.model.Game
import pt.isel.tds2627.model.new
import pt.isel.tds2627.model.play
import pt.isel.tds2627.model.show
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.*
import pt.isel.tds2627.model.Player
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import pt.isel.tds2627.model.GameSerializer
import pt.isel.tds2627.storage.TextFileStorage


@Composable
fun App() {
    var player by remember { mutableStateOf(Player.X) }
    MaterialTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PlayerView(250.dp, player)
            Button(
                onClick = {
                    player = player.other
                }
            )
            {
                Text("Other player")
            }
            PlayerView(250.dp, player)
        }
    }
}


@Composable
fun PlayerView(size: Dp, player: Player?) {
   val modifier = Modifier.size(size).background(Color.White)
   if (player == null) Box(modifier)  // Empty square (size x size)
   else {
       val resource = when (player) {
           Player.X -> painterResource("images/x.png")
           Player.O -> painterResource("images/o.png")
           //Player.O -> Res.drawable.o
       }
       Image(
           painter = resource,
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
        icon = painterResource("images/x.png")
    ) { App() }
}




fun main() {

    var game = Game()
    val commands = getCommands(TextFileStorage("games", GameSerializer))
 // Available commands


    while (true) {
        val (name, args) = readCommand()
        // Execute the command
        val cmd = commands[name]
        if (cmd==null) println("Invalid command $name")
        else try {
            game = cmd.execute(args, game)
            if (cmd.isToFinish) break
            game?.show()
        } catch (e: IllegalStateException) {
            println(e.message)
        } catch (e: IllegalArgumentException) {
            println("${e.message}\nUse: $name ${cmd.argsSyntax}")
        }
    }
}
