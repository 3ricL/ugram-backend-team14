package ugram.exceptions

class UnableToFindPictureException(msg: String? = null, cause: Throwable? = null): Exception(msg, cause)
class UnableToFindUserException(msg: String? = null, cause: Throwable? = null): Exception(msg, cause)
class UnableToDeletePictureException(msg: String? = null, cause: Throwable? = null): Exception(msg, cause)
class UnableToUpdatePictureException(msg: String? = null, cause: Throwable? = null): Exception(msg, cause)
class UnableToFindPictureOfUser(msg: String? = null, cause: Throwable? = null): Exception(msg, cause)
class NotAuthorizedException(msg: String? = null, cause: Throwable? = null): Exception(msg, cause)
class InvalidTokenException(msg: String? = null, cause: Throwable? = null): Exception(msg, cause)
class NoAuthenticationProvided(msg: String? = null, cause: Throwable? = null): Exception(msg, cause)