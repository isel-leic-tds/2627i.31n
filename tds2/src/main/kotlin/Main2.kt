package pt.isel.tds2627

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.*


data class Student (
    val name: String,
    var age: Int) {

    fun aniversario() : Unit {
        age = age + 1
    }

}

@Composable
fun App(
    student: Student,
    callback : (Student) -> Unit

) {

    var age by remember { mutableStateOf(student.age) }

    MaterialTheme {
        NavigationBar {
            Button(
                onClick = {
                    println("ENTROU")
                    age = age +1;
                    callback(student)
                    //age = student.age
                }
            )
            {
                Text("Name: ${student.name}; Age Student: ${student.age}")
            }
        }

    }
}

fun soma (student: Student) {
    student.aniversario()
}


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val name = "Kotlin"
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    println("Hello, " + name + "!")
    val aluno = Student(name="Jose", age=32)

    application {
        Window(onCloseRequest = ::exitApplication, title = "TDS2") {
            App(aluno,
                callback = ::soma)
        }
    }

/*

    MongoDriver(nameDb = "test").use {
        val docs = it.getCollection<Student>("student")
        docs.apply {
            check(insertDocument(aluno))
        }
    }

*/
}