import okhttp3.internal.wait
import org.jsoup.Jsoup
import kotlin.concurrent.thread

class driver {
    val _MainPage: String = "https://ln.hako.vn/danh-sach?truyendich=1&sangtac=1&dangtienhanh=1&tamngung=1&hoanthanh=1&sapxep=capnhat&page="
    val _BasePage: String = "https://ln.hako.vn"


    val TruyenList : MutableList<String> = mutableListOf()
    val TruyenLink : MutableList<String> = mutableListOf()
    val BiaTruyen : MutableList<String> = mutableListOf()
    fun TrangChu(_Page : Int = 0) {
        val page = _Page
        Thread.sleep(2000)
        val driver = Jsoup.connect(_MainPage+page)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.3")
            .get()
        val TitleRaw = driver.select("div[class*=thumb-item-flow]")
        for (i in TitleRaw) {
            val Title = i.select("a").last()!!.text()
            val Link  = i.select("a").last()!!.attr("href")
            val Img   = i.select("div[data-bg]").attr("data-bg")
            TruyenList.add(Title)
            TruyenLink.add(Link)
            BiaTruyen.add(Img)
            println(page.toString() + Title)
            println(Link)
            println(Img)
            println("----------------------------------------------------------------------------------------------------------------")
        }
    }

    var Tacgia = ""
    val TheLoai : MutableList<String> = mutableListOf()
    var Likes = ""
    val Sum : MutableList<String> = mutableListOf()
    val ChuongLink : MutableList<String> = mutableListOf()
    val ChuongName : MutableList<String> = mutableListOf()

    fun TrangTruyen(_Truyen : String) {
        Thread.sleep(1000)
        val Truyen = _Truyen
        val driver = Jsoup.connect(Truyen).get()
//      val driver = Jsoup.connect("https://ln.hako.vn/truyen/20806-shannon-muon-di-chet").get()
        Tacgia = driver.select("a[href*=https://ln.hako.vn/tac-gia]").text()
        Likes = driver.getElementsByClass("block feature-name").first()!!.text()
        val _TheLoai = driver.select("a[href*=https://ln.hako.vn/the-loai]")
        for (ele in _TheLoai) {
            val one = ele.text()
            TheLoai.add(one)
        }
        val Tomtat = driver.getElementsByClass("summary-content")
        for (ele in Tomtat) {
            val t = ele.select("p").text()
            Sum.add(t)
        }
        val Chuongs = driver.select("a[title]")
        for (ele in Chuongs) {
            val Li = ele.attr("href")
            val Ch = ele.attr("title")
            ChuongLink.add(Li)
            ChuongName.add(Ch)
        }
        ChuongLink.removeFirst()
        ChuongName.removeFirst()
    }
    val read : MutableList<String> = mutableListOf()

    fun Reader(_Ch : String) {
        Thread.sleep(1000)
        val Ch = _Ch
//        println(_BasePage+ChuongLink[Ch])
        val driver = Jsoup.connect(_Ch).get()
        val Content = driver.select("p[id]")
        for (ele in Content) {
            val text = ele.text()
            val img = ele.select("img").attr("src")
            val e = text+img
            read.add(e)
        }
    }
    fun Clear() {
        read.clear()
        TheLoai.clear()
        Sum.clear()
        ChuongLink.clear()
        ChuongName.clear()
    }
}