package ugram.models

typealias UserId = String

data class UserModel(
        val id: UserId,
        var email: String,
        var firstName: String,
        var lastName: String,
        var phoneNumber: String,
        val pictureUrl: String,
        val registrationDate: Int)
{
    fun applyUpdate(update: UpdateUserModel) {
        this.email = update.email
        this.firstName = update.firstName
        this.lastName = update.lastName
        this.phoneNumber = update.phoneNumber
    }
    companion object Factory {
        fun create(updateModel: UpdateUserModel, userId: UserId, registeredAt: Int, profileUrl: String): UserModel =
                UserModel(
                        id = userId,
                        email = updateModel.email,
                        firstName = updateModel.firstName,
                        lastName = updateModel.lastName,
                        phoneNumber = updateModel.phoneNumber,
                        pictureUrl = profileUrl,
                        registrationDate = registeredAt
                )
    }
}

data class UpdateUserModel(
        val email: String,
        val firstName: String,
        val lastName: String,
        val phoneNumber: String
)