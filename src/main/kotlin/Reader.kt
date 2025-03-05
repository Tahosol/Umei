import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LoadImg(link : String) {
    AsyncImage(
        model = link,
        contentDescription = null,
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp)),
        contentScale = ContentScale.Fit,
        alignment = Alignment.Center
    )
}


data class Reader(val Chaplink : List<String>, val link: String, val chap : Int) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        var content = remember { mutableStateListOf<String>() }
        var Load by remember { mutableStateOf(true) }
        var Chap by remember { mutableStateOf(chap) }
//        println(Chap)
//        println(Chaplink.size)
        river.Clear()
        LaunchedEffect(Chap) {
            val I = withContext(Dispatchers.IO) {
                content.clear()
                river.Clear()
                river.Reader(river._BasePage+Chaplink[Chap])
                content.addAll(river.read)
                Load = false
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF232634)),
            contentAlignment = Alignment.Center
        ) {

            Button(
                onClick = {
                    if (Chap < Chaplink.size - 1) Chap++
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8caaee)),
                modifier = Modifier
                    .align(alignment = Alignment.BottomEnd)
                    .clip(RoundedCornerShape(12.dp))
                    .alpha(0.4f)
            ) {
                Text(">")
            }
            Button(
                onClick = {
                    if (Chap > 0) Chap--
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8caaee)),
                modifier = Modifier
                    .align(alignment = Alignment.BottomStart)
                    .clip(RoundedCornerShape(12.dp))
                    .alpha(0.4f)
            ) {
                Text("<")
            }

            if (Load) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(150.dp)
                        .align(alignment = Alignment.Center)
                        .height(150.dp),
                    color = Color(0xFFc6d0f5)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .background(color = Color(0xFF232634))
                        .width(1000.dp),
                ) {
                    items(content.size) { index: Int ->
                        val line : String = content[index]
                        when {
                            line.length < 2 -> Text(
                                text = content[index],
                                modifier = Modifier
                                    .padding(16.dp),
                                fontSize = 26.sp,
                                color = Color(0xFFdce0e8),
                            )
                            line.substring(0,2) == "ht" -> LoadImg(content[index])
                            else -> Text(
                                text = content[index],
                                modifier = Modifier
                                    .padding(16.dp),
                                fontSize = 26.sp,
                                color = Color(0xFFdce0e8),
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                navigator?.pop()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8caaee)),
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .alpha(0.4f)
        ) {
            Text("Back")
        }
    }
}