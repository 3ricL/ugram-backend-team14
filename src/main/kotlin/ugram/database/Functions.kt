package ugram.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.transactions.transaction
import ugram.config.DbConfig
import ugram.models.UserModel
import ugram.repositories.UserRepository
import ugram.services.AuthorizationService

fun initPostGre(config: DbConfig) {
    Database.connect("jdbc:postgresql://${config.host}:${config.port}/${config.db_name}?user=${config.username}&password=${config.password}", driver = "org.postgresql.Driver")
    transaction {
        dropDatabase()
        logger.addLogger(StdOutSqlLogger())
        createDatabase()
        UserRepository().create(UserModel(
                id = "Itof",
                pictureUrl = "http://images.ugram.net/boris_bresciani/1022.png",
                email = "boo@bash",
                firstName = "eric",
                lastName = "lab",
                phoneNumber = "123",
                registrationDate = 123
        ))
        AuthorizationService().addAuthorization(token = "123", userId = "Itof")
    }
}

fun createDatabase() {
    SchemaUtils.create(Users, Pictures, Mentions, Pictures_Tags, Permissions)
}

fun dropDatabase() {
    SchemaUtils.drop(Permissions, Pictures_Tags, Mentions, Pictures, Users)
}