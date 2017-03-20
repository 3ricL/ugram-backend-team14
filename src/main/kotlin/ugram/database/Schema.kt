package ugram.database

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = varchar("id", 80).primaryKey()
    val email = varchar("email", 255)
    val firstName = varchar("first_name", 80)
    val lastName = varchar("last_name", 80)
    val phoneNumber = varchar("phone_number", 20).nullable()
    val pictureUrl = varchar("picture_url", 255)
    val registrationDate = integer("registered_at")
}

object Pictures : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val description = text("description")
    val url = varchar("url", 255)
    val creationDate = integer("created_at")
    val userId = varchar("user_id", 80) references Users.id
}

object Pictures_Tags : Table() {
    val pictureId = integer("picture_id").primaryKey() references Pictures.id
    val tag = varchar("tag", 80).primaryKey()
}

object Mentions: Table() {
    val pictureId = integer("picture_id").primaryKey() references Pictures.id
    val userId = varchar("user_id", 80).primaryKey() references Users.id
}

object Permissions: Table() {
    val token = varchar("token", 100).primaryKey()
    val userId = varchar("id", 80) references Users.id
}