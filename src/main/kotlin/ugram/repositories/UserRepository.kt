package ugram.repositories

import ugram.database.Users
import ugram.models.UserId
import ugram.models.UserModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder

class UserRepository {

    fun create(user: UserModel) =
            Users.insert( toRow(user) )

    fun update(userId: UserId, u: UserModel) =
            Users.update(where = { Users.id eq userId }, body = toRow(u))

    fun find(id: UserId): UserModel? =
            Users.select { Users.id eq id }
                    .map { fromRow(it) }
                    .firstOrNull()

    fun findAll(page: Int, perPage: Int): List<UserModel> =
            Users.selectAll()
                    .limit(n = perPage, offset = page*perPage)
                    .map { fromRow(it) }

    fun countTotal() =
            Users.selectAll().count()

    private fun toRow(u: UserModel): Users.(UpdateBuilder<*>) -> Unit = {
        it[id] = u.id
        it[email] = u.email
        it[firstName] = u.firstName
        it[lastName] = u.lastName
        it[phoneNumber] = u.phoneNumber
        it[pictureUrl] = u.pictureUrl
        it[registrationDate] = u.registrationDate
    }

    private fun fromRow(r: ResultRow) =
            UserModel(
                    id = r[Users.id],
                    email = r[Users.email],
                    firstName = r[Users.firstName],
                    lastName = r[Users.lastName],
                    phoneNumber = r[Users.lastName],
                    pictureUrl = r[Users.pictureUrl],
                    registrationDate = r[Users.registrationDate]
            )
}