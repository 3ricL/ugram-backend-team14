package ugram.services

import org.jetbrains.exposed.sql.transactions.transaction
import ugram.models.UpdateUserModel
import ugram.models.UserId
import ugram.models.UserModel
import ugram.repositories.UserRepository
import ugram.exceptions.UnableToFindUserException
import ugram.models.Pagination
import ugram.util.calculateNbPages
import ugram.util.nowTimestamp

class UserService(val userRepository: UserRepository) {

    fun addUser(model: UpdateUserModel, userId: UserId, profileUrl: String) {
        val user = UserModel.create(
                updateModel = model,
                userId = userId,
                profileUrl = profileUrl,
                registeredAt = nowTimestamp()
        )
        transaction {
            userRepository.create(user)
        }
    }

    fun getUsersPaginated(page: Int, perPage: Int): Pagination<UserModel> =
            transaction {
                val totalEntries = userRepository.countTotal()
                Pagination(
                        items = userRepository.findAll(page = page, perPage = perPage),
                        totalEntries = totalEntries,
                        totalPages = calculateNbPages(total = totalEntries, perPage = perPage)
                )
            }

    fun getUser(id: UserId): UserModel? =
            transaction {
                userRepository.find(id)
            }

    fun updateUser(id: UserId, updateModel: UpdateUserModel): UserModel =
            transaction {
                val user = userRepository.find(id)
                if (user === null) { throw UnableToFindUserException() }
                user.applyUpdate(updateModel)
                userRepository.update(userId = id, u = user)
                user
            }

}