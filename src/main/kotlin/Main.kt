import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import coil3.compose.AsyncImage
import kotlinx.coroutines.*

val river = driver()
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
            }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF232634)),
            contentAlignment = Alignment.Center
        ) {
            if (Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(150.dp)
                        .align(alignment = Alignment.Center)
                        .height(150.dp),
                    color = Color(0xFFc6d0f5)
                )
            } else {
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
            }
        }
    }
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

@Composable
fun Top(img : String, name : String, likes : String, Tacgia : String, Theloai : List<String>) {
    Row(
        modifier = Modifier
            .background(color = Color(0xFF232634))
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = img,
            contentDescription = name,
            modifier = Modifier
                .padding(26.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color.Gray)
                .shadow(5.dp, RoundedCornerShape(7.dp))
                .border(3.dp, color = Color(0xFFc6d0f5)),
            contentScale = ContentScale.Crop
        )
        Column() {
            Text(
                modifier = Modifier
                    .padding(28.dp),
                text = name,
                fontSize = 36.sp,
                color = Color(0xFFc6d0f5),
                fontWeight = FontWeight.Bold
            )
            LazyRow(
                modifier = Modifier
                    .padding(5.dp),
            ) {
                val RTheLoai = Theloai.take(Theloai.size/2)
                for (Strin in RTheLoai) {
                    item {
                        Box(
                            modifier = Modifier
                                .padding(10.dp)
                                .border(1.dp, Color(0xFFc6d0f5))
                        ) {
                            Text(
                                text = Strin,
                                color = Color(0xFFc6d0f5),
                                modifier = Modifier
                                    .padding(16.dp),
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
            Text(
                text = "Author: "+Tacgia,
                color = Color(0xFFc6d0f5),
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(16.dp)
            )
            Text(
                text = "Likes: "+likes,
                color = Color(0xFFc6d0f5),
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}
@Composable
fun Body(Name : List<String>, Link : List<String>, sum : List<String>) {
    val navigator = LocalNavigator.current
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .border(3.dp, color = Color(0xFFc6d0f5))
                .padding(16.dp)
                .width(800.dp)
        ) {
            items(Name.size) { index ->
                Text(
                    text = Name[index],
                    modifier = Modifier
                        .clickable {
                            val link = river._BasePage+Link[index]
//                            println(link)
                            navigator?.push(Reader(Link, link, index))
                        }
                        .padding(10.dp),
                    color = Color(0xFFc6d0f5),
                    fontSize = 23.sp
                )
            }
        }
        Column(
            modifier = Modifier
                .border(3.dp, color = Color(0xFFc6d0f5))
                .padding(16.dp)
        ) {
            Text(
                text = "Summary",
                color = Color(0xFFc6d0f5),
                modifier = Modifier
                    .padding(10.dp),
                fontSize = 28.sp
            )
            for (item in sum) {
                Text(
                    text = item,
                    color = Color(0xFFc6d0f5),
                    modifier = Modifier
                        .padding(10.dp),
                    fontSize = 26.sp
                )
            }
        }
    }
}

data class NovelDetail(val Img: String, val name: String, val link : String, val page : Int) : Screen {
    @Composable
    override fun Content() {
        var IsLoading by remember { mutableStateOf(true) }
        var ChuongLink = remember { mutableStateListOf<String>() }
        var Chuong = remember { mutableStateListOf<String>() }
        var Sum = remember { mutableStateListOf<String>() }
        river.Clear()
        LaunchedEffect(Unit) {
            val i = withContext(Dispatchers.IO) {
                river.TrangTruyen(link)
                ChuongLink.addAll(river.ChuongLink)
                Chuong.addAll(river.ChuongName)
                Sum.addAll(river.Sum)
                IsLoading = false
            }
        }
        val navigator = LocalNavigator.current

        val Like = river.Likes
        val Tacgia = river.Tacgia
        val Theloai : List<String> = river.TheLoai
        Box(
            modifier = Modifier
                .background(color = Color(0xFF232634))
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (!IsLoading) {
                    Top(Img, name, Like, Tacgia, Theloai)
                    Body(Chuong, ChuongLink, Sum)
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(150.dp)
                            .height(150.dp),
                        color = Color(0xFFc6d0f5)
                    )
                }
            }
            Button(
                onClick = {
                    navigator?.push(HomeScreen(page, true))
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8caaee)),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Text("Back")
            }
        }
    }
}


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Umei",
        undecorated = false
    ) {
        Navigator(HomeScreen(1,true))
    }
}