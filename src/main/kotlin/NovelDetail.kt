import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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