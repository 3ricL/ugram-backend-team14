package ugram.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import ugram.exceptions.*

open class UtilController {
    @ExceptionHandler(NoAuthenticationProvided::class) @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleAppException(ex: NoAuthenticationProvided) = errorMessage(ex)

    @ExceptionHandler(InvalidTokenException::class) @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleAppException(ex: InvalidTokenException) = errorMessage(ex)

    @ExceptionHandler(NotAuthorizedException::class) @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleAppException(ex: NotAuthorizedException) = errorMessage(ex)

    @ExceptionHandler(UnableToFindPictureException::class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleAppException(ex: UnableToFindPictureException) = errorMessage(ex)

    @ExceptionHandler(UnableToFindUserException::class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleAppException(ex: UnableToFindUserException) = errorMessage(ex)

    @ExceptionHandler(UnableToDeletePictureException::class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleAppException(ex: UnableToDeletePictureException) = errorMessage(ex)

    @ExceptionHandler(UnableToUpdatePictureException::class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleAppException(ex: UnableToUpdatePictureException) = errorMessage(ex)

    @ExceptionHandler(UnableToFindPictureOfUser::class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleAppException(ex: UnableToFindPictureOfUser) = errorMessage(ex)

    @ExceptionHandler(ServletRequestBindingException::class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleAppException(ex: ServletRequestBindingException) = errorMessage(ex)

    @ExceptionHandler(HttpMessageNotReadableException::class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleAppException(ex: HttpMessageNotReadableException) = errorMessage(ex)

    private fun errorMessage(ex: Exception) = ErrorMessage(message = ex.message ?: "unknown error")
}
