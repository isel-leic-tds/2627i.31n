package pt.isel.tds2627

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
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
import pt.isel.tds2627.model.AppVMClashMongo
import pt.isel.tds2627.model.BOARD_SIZE
import pt.isel.tds2627.model.Board
import pt.isel.tds2627.model.Draw
import pt.isel.tds2627.model.GameState
import pt.isel.tds2627.model.Name
import pt.isel.tds2627.model.Position
import pt.isel.tds2627.model.Run
import pt.isel.tds2627.model.Score
import pt.isel.tds2627.model.Win
import pt.isel.tds2627.model.play
import pt.isel.tds2627.resources.Res
import pt.isel.tds2627.resources.*

enum class InputName(val txt: String) {
    ForStart("start"), ForJoin("join")
}

val CELL_SIDE = 100.dp       // Size of each cell
val GRID_THICKNESS = 5.dp    // Thickness of grid lines
val BOARD_SIDE = CELL_SIDE * BOARD_SIZE + GRID_THICKNESS * (BOARD_SIZE-1)
val STATUS_HEIGHT = 70.dp  // Height of the status bar




@Composable
fun StartOrJoinDialog(
    type: InputName, onCancel: ()->Unit, onAction: (Name)->Unit
) {
    var name by remember { mutableStateOf("") }  // Name in edition
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(text = "Name to ${type.txt}",
            style = MaterialTheme.typography.headlineSmall
        ) },
        text = { OutlinedTextField(value = name,
            onValueChange = { name = it },
            label = { Text("Name of game") }
        ) },
        confirmButton = {
            TextButton(enabled = Name.isValid(name),
                onClick = { onAction(Name(name)) }
            ) { Text(type.txt) }
        },
        dismissButton = {
            TextButton(onClick = onCancel){ Text("cancel") }
        }  )   }

// zz 20/11
@Composable
private fun FrameWindowScope.App(onExit: ()-> Unit) {
    val vm = remember { AppVMClashMongo() /* AppVMClashTextFile() */ }
    MenuBar { Menu("Game") {
        Item("Start game", onClick = vm::start)
        Item("Join game", onClick = vm::join)
        Item("Refresh", enabled = vm.isRun, onClick = vm::refresh)
        Item("New board", enabled= vm.newAvailable, onClick=vm::newBoard)
        Item("Score", enabled = vm.isRun, onClick = vm::showScore)
        Item("Exit") { vm.exit(); onExit() }
    } }
    MaterialTheme {
        if (vm.isRun) Column {
            Board(vm.game.board) { vm.play(it) }
            StatusBar(vm.game.state, vm.run.sidePlayer)
        } else
            Box(Modifier.size(BOARD_SIDE, BOARD_SIDE+STATUS_HEIGHT))
        if (vm.viewScore)
            ScoreInfo(vm.game.score, vm.run.id, onClose = vm::hideScore)
        vm.errorMessage?.let { ErrorInfo(it, onClose = vm::hideError) }
        vm.inputName?.let { StartOrJoinDialog(it,
            onCancel = vm::cancelInput, onAction= vm::startOrJoin
        ) }
    }
}



@Composable
private fun LabeledCell(label: String,  player: Player?) {
    Text(label, style = MaterialTheme.typography.displaySmall)
    player?.let { Cell(modifier= Modifier.padding(4.dp), player= player) }
}


@Composable
fun StatusBar(state: GameState, you: Player? = null) = Row(
    modifier = Modifier.background(Color.LightGray)
        .width(BOARD_SIDE).height(STATUS_HEIGHT),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
) {
    you?.let{
        LabeledCell("You:", you)
        Spacer(Modifier.width(30.dp))
    }
    val (txt, player) = when (state) {
        is Run -> "Turn:" to state.turn
        is Win -> "Winner:" to state.winner
        is Draw -> "Draw" to null
    }
    LabeledCell(txt,player)
}



@Composable
fun InfoDialog(
    title: String, onClose: ()->Unit, content: @Composable ()->Unit
) = AlertDialog(
    onDismissRequest = onClose,
    title = { Text(title) },
    text = content,
    confirmButton = { TextButton(onClick = onClose) { Text("Close") } }
)


@Composable
fun ErrorInfo(message: String, onClose: ()->Unit) =
    InfoDialog("Error", onClose) {
        Text(message, style = MaterialTheme.typography.displaySmall)
    }

@Composable
fun ScoreInfo(score: Score, name: Name, onClose: ()->Unit) =
    InfoDialog("Score in $name", onClose) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            val modifier = Modifier.padding(vertical = 10.dp)
            Column { Player.entries.forEach { player ->
                Row(modifier, verticalAlignment= Alignment.CenterVertically) {
                    Cell(Modifier.size(20.dp), player)
                    Text(" - ${score[player]}")
                }   }   }
            Text("Draws - ${score[null]}", modifier)
        }   }



@Composable
fun App() {
    //var player by remember { mutableStateOf(Player.X) }

    var game by remember { mutableStateOf(Game()) }
    MaterialTheme {
        Column {
            Board(game.board) { pos -> game = game.play(pos) }
            StatusBar(game.state)
        }
    }


    /*
    MaterialTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PlayerView(250.dp, player)
            Button(
                onClick = { player = player.other }
            ) {
                Text("Other player")
            }
        }
    }
    */

}

@Composable
fun Cell(
    modifier: Modifier,     // Customize the view
    player: Player?,        // Player to show
    onClick: ()->Unit = {}  // Action to execute when clicked
) {
    if (player == null)
        Box(modifier.clickable(onClick = onClick))
    else {
        val resource = when (player) {
            Player.X -> Res.drawable.cross
            Player.O -> Res.drawable.circle
        }
        Image(
            painter = painterResource(resource),
            contentDescription = null,
            modifier = modifier
        )
}  }

@Composable
fun Board(board: Board, onClick: (Position)->Unit) = Column(
    modifier = Modifier.background(Color.Black).size(BOARD_SIDE),
    verticalArrangement = Arrangement.SpaceBetween
) {
    repeat(BOARD_SIZE) { row -> Row(
        modifier = Modifier.fillMaxWidth().height(CELL_SIDE),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(BOARD_SIZE) { col ->
            val pos = Position(row,col)
            Cell( Modifier.background(Color.White).size(CELL_SIDE),
                player = board[pos],
                onClick = { onClick(pos) }
            )
} } } }


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


fun main() = application {
    Window(
        title = "Jogo do Galo",
        state = WindowState(size = DpSize.Unspecified),
        onCloseRequest = ::exitApplication,
        icon = painterResource(Res.drawable.cross)
    ) { App() }
}




fun main3() = MongoDriver("galo").use {
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
