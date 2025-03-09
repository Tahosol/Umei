import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cafe.adriel.voyager.navigator.Navigator
import coil3.compose.rememberAsyncImagePainter

val river = driver()
//val datawork = Datawork()

fun main() = application {
    val icon : Painter = rememberAsyncImagePainter("logo.png")
    Window(
        onCloseRequest = ::exitApplication,
        title = "Umei",
        undecorated = false,
        state = rememberWindowState(),
        icon = icon
    ) {
        Navigator(HomeScreen(1,true))
//        datawork.fetch()
    }
}