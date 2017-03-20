package ugram.controllers

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ugram.models.UpdateUserModel
import ugram.models.UserModel
import ugram.services.AuthorizationService
import ugram.services.UserService

@RestController @CrossOrigin
class UserController(val userService: UserService, val authorizationService: AuthorizationService): UtilController() {

    @GetMapping("/users")
    fun findAllUsersPaginated(@RequestParam(defaultValue = "0") page: Int,
                              @RequestParam(defaultValue = "10") perPage: Int)
            = userService.getUsersPaginated(page = page, perPage = perPage)

    @GetMapping("/users/{userId}")
    fun findUser(@PathVariable userId: String)
            = userService.getUser(id = userId)

    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun updateUser(@PathVariable userId: String,
                   @RequestBody updateModel: UpdateUserModel,
                   @RequestHeader(value="Authorization", defaultValue = "") token: String): UserModel {
        authorizationService.checkForAuthorization(token = token, userId = userId)
        return userService.updateUser(id = userId, updateModel = updateModel)
    }
}