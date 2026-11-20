package pt.isel.tds2627.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import pt.isel.tds2627.InputName
import pt.isel.tds2627.console.Clash
import pt.isel.tds2627.console.ClashRun
import pt.isel.tds2627.console.GameStorage
import pt.isel.tds2627.console.canNew
import pt.isel.tds2627.console.deleteIfIsOwner
import pt.isel.tds2627.console.new
import pt.isel.tds2627.console.play
import pt.isel.tds2627.console.*
import pt.isel.tds2627.data.MongoDriver
import pt.isel.tds2627.storage.MongoStorage
import pt.isel.tds2627.storage.TextFileStorage


const val BaseName = "games" // Base name for Clash games

open class AppViewModelClash(storage: GameStorage) {
    var clash by mutableStateOf(Clash(storage))  // Model state
        private set  // Prevent external modification
    val isRun: Boolean get() = clash is ClashRun
    val run get() = clash as ClashRun
    val game get() = run.game
    val newAvailable get() = isRun && run.canNew


    private fun exec(fx: Clash.()->Clash) = // Executes a Clash function
        try { clash = clash.fx() }       // and updates the Clash state
        catch (e: Exception) {        // Report exceptions in ErrorDialog
            errorMessage = e.message
        }


    fun newBoard() = exec(Clash::new)  // Create a new game board


    fun play(pos: Position) = exec{
        play(pos)
    }


    fun refresh() = exec(Clash::refresh)


    open fun exit() { clash.deleteIfIsOwner() }


    var inputName by mutableStateOf<InputName?>(null) //StartOrJoinDialog
        private set                                   //  state
    fun cancelInput() { inputName = null }


    fun start() { inputName = InputName.ForStart }
    fun join() { inputName = InputName.ForJoin }
    fun startOrJoin(name: Name) {
        if (inputName == InputName.ForStart) exec{ start(name)}
        else exec { join(name) }
        cancelInput()
    }
    // Error dialog management
    var errorMessage by mutableStateOf<String?>(null)
        private set
    fun hideError() { errorMessage = null } // Hide the error message
    // view Score dialog management
    var viewScore by mutableStateOf(false)
        private set
    fun showScore() { viewScore = true }  // Show the score dialog
    fun hideScore() { viewScore = false }  // Hide the score dialog
}




class AppVMClashTextFile : AppViewModelClash(
    TextFileStorage(BaseName, GameSerializer)
)


class AppVMClashMongo(
    val driver : MongoDriver = MongoDriver("galo"),
    storage : GameStorage = MongoStorage(BaseName, driver, GameSerializer),
): AppViewModelClash(storage) {
    override fun exit() {
        super.exit()
        driver.close() // Close the MongoDB driver when exiting
    }
}
