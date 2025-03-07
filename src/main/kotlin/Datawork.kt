import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.transactions.transaction

class Datawork {


    object Novel : Table() {
        val id = integer("id").autoIncrement()
        val names = varchar("name", 255)
        val novellink = varchar("link", 255)
        val Novelimg = varchar("Img", 255)
        override val primaryKey = PrimaryKey(id, name = "Novel_ID")
    }
    fun save(_Name : String, _Link : String, _Img : String) {
        Database.connect("jdbc:sqlite:NovelsFollow.db", driver = "org.sqlite.JDBC")

        transaction {
            SchemaUtils.create(Novel)
        }
        transaction {
            Novel.insert {
                it[names] = _Name
                it[novellink] = _Link
                it[Novelimg] = _Img
            }
        }
    }
    val Novelname : MutableList<String> = mutableListOf()
    val Novellink : MutableList<String> = mutableListOf()
    val Imgae : MutableList<String> = mutableListOf()
    fun fetch() {
        Database.connect("jdbc:sqlite:NovelsFollow.db", driver = "org.sqlite.JDBC")
        transaction {
            Novel.selectAll().forEach {
                Novellink.add(it[Novel.novellink])
                Novelname.add(it[Novel.names])
                Imgae.add(it[Novel.Novelimg])
                println(it[Novel.names])
            }
        }
    }
}
