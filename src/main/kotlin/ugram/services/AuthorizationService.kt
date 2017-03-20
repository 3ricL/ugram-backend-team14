package ugram.services

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ugram.database.Permissions
import ugram.exceptions.InvalidTokenException
import ugram.exceptions.NoAuthenticationProvided
import ugram.exceptions.NotAuthorizedException
import ugram.models.UserId

class AuthorizationService {

    fun addAuthorization(token: String, userId: UserId) =
            transaction {
                Permissions.insert {
                    it[Permissions.token] = token
                    it[Permissions.userId] = userId
                }
            }

    fun checkForAuthorization(token: String, userId: UserId) =
            transaction {
                if(token.isBlank()) throw NoAuthenticationProvided("No provided authentication")
                val tokenWithoutBearer = token.substring(7)
                val associatedUserId = Permissions
                        .select { Permissions.token eq tokenWithoutBearer }
                        .map { it[Permissions.userId] }
                        .firstOrNull() ?: throw InvalidTokenException("Provided token is invalid")

                if (associatedUserId != userId) throw NotAuthorizedException("Editing on forbidden user account for current authentication")
            }
}