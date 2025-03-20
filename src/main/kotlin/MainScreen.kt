import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Toolkit

@Composable
fun LoadMain(imageUrl: String, description: String, Link : String, Page: Int) {
    val navigator = LocalNavigator.current
    var Change = 0
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        AsyncImage(
            model = "https://wsrv.nl/?url="+imageUrl,
            contentDescription = description,
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Gray)
                .shadow(5.dp, RoundedCornerShape(12.dp))
                .height(210.dp)
                .width(170.dp)
                .clickable {
//                    println(river._BasePage+Link)
                    val FunctionnalLink = river._BasePage+Link
                    navigator?.push(NovelDetail("https://wsrv.nl/?url="+imageUrl, description, FunctionnalLink, Page))
                    Change = 2
                },
            contentScale = ContentScale.Crop
        )
        Text (
            text = if (description.length > 20) description.take(20) + "..." else description,
            color = Color(0xFFc6d0f5),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
data class HomeScreen(val Page : Int, var T : Boolean) : Screen {
    @Composable
    @Preview
    override fun Content() {
        var page by remember { mutableStateOf(1) }
        val novelCovers = remember { mutableStateListOf<String>() }
        val novelNames = remember { mutableStateListOf<String>() }
        val novellink = remember { mutableStateListOf<String>() }
        var Loading by remember { mutableStateOf(true) }

        val navigator = LocalNavigator.current

        LaunchedEffect(T) {
            page = Page
            T = false
        }

        LaunchedEffect(page) {
            val result = withContext(Dispatchers.IO) {
                river.TrangChu(page)
                Loading = false
            }
        }
        novelCovers.clear()
        novelNames.clear()
        novellink.clear()
        novelCovers.addAll(river.BiaTruyen)
        novelNames.addAll(river.TruyenList)
        novellink.addAll(river.TruyenLink)

        val gridState = rememberLazyGridState()

        val reachedBottom: Boolean by remember {
            derivedStateOf {
                val lastVisibleItem = gridState.layoutInfo.visibleItemsInfo.lastOrNull()
                lastVisibleItem?.index != 0 && lastVisibleItem?.index == gridState.layoutInfo.totalItemsCount - 1
            }
        }
        LaunchedEffect(reachedBottom) {
            if (reachedBottom) {
                page++
                println(novelNames.size)
                println(gridState.layoutInfo.totalItemsCount)
            }
        }
        var search by remember { mutableStateOf("") }
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val screenWidth = screenSize.width
        val halfscreenwidth = screenWidth/2
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF232634)),
            contentAlignment = Alignment.Center
        ) {
            Column {
                if (Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(150.dp)
//                            .align(alignment = Alignment.Center)
                            .height(150.dp),
                        color = Color(0xFFc6d0f5)
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp,start = 60.dp, end = 60.dp)
                    ) {
                        TextField(
                            value = search,
                            onValueChange = { newText -> search = newText },
                            label = { Text("Search")},
                            singleLine = true,
                            modifier = Modifier
                                .border(1.dp, Color(0xFFc6d0f5), shape = RoundedCornerShape(10.dp))
                                .clip(shape = RoundedCornerShape(30.dp))
                                .width(halfscreenwidth.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    println(search)
                                }
                            ),
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = Color(0xFFc6d0f5),
                                cursorColor = Color(0xFFb4befe),
                                focusedIndicatorColor = Color.Transparent,
                                focusedLabelColor = Color(0xFF89b4fa),
                                unfocusedIndicatorColor = Color.Transparent,
                                unfocusedLabelColor = Color(0xFF89b4fa)
                            ),
                        )
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(200.dp),
                        state = gridState,
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            top = 24.dp,
                            end = 20.dp,
                            bottom = 24.dp
                        ),
                        modifier = Modifier
                            .background(color = Color(0xFF232634))
                            .fillMaxSize(),
                        content = {
                            items(novelNames.size) { index -> LoadMain(novelCovers[index], novelNames[index], novellink[index], page+1) }
                        }
                    )
//                LazyRow() {
//                    val pageOffset : List<Int> = listOf(-2, -1, 0, 1, 2)
//
//                    items(pageOffset.size) {index ->
//                        Button(
//                            onClick = {
//                                page = page + pageOffset[index]
//                            }
//                        ) {
//                            Text(
//                                text = "${page + pageOffset[index]}"
//                            )
//                        }
//                    }
//                }
                }
            }
        }
    }
}
