package ugram.controllers

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ugram.models.PictureId
import ugram.models.PictureModel
import ugram.models.UpdatePictureModel
import ugram.models.UserId
import ugram.services.AuthorizationService
import ugram.services.PictureService

@RestController @CrossOrigin
class UserPictureController(val pictureService: PictureService, val authorizationService: AuthorizationService): UtilController() {

    @GetMapping("/users/{userId}/pictures")
    fun findUserPictures(
            @PathVariable userId: String,
            @RequestParam(defaultValue = "0") page: Int,
            @RequestParam(defaultValue = "10") perPage: Int
    )
            = pictureService.getPicturesOfUserPaginated(userId = userId, page = page, perPage = perPage)

    @PostMapping("/users/{userId}")
    fun addPicture(@PathVariable userId: String,
                   @RequestHeader(value = "Authorization", defaultValue = "") token: String,
                   @RequestParam file: MultipartFile,
                   @RequestBody updateModel: UpdatePictureModel): PictureModel {
        authorizationService.checkForAuthorization(token = token, userId = userId)
        return pictureService.addPicture(userId = userId, updateModel = updateModel, byteArr = file.bytes)
    }

    @GetMapping("/users/{userId}/pictures/{pictureId}")
    fun findSpecificPicture(@PathVariable userId: String,
                                @PathVariable pictureId: PictureId)
            = pictureService.getSpecificUserPicture(userId = userId, pictureId = pictureId)

    @PutMapping("/users/{userId}/pictures/{pictureId}")
    fun updatePicture(@PathVariable userId: String,
                 @PathVariable pictureId: PictureId,
                 @RequestHeader(value="Authorization", defaultValue = "") token: String,
                 @RequestBody updateModel: UpdatePictureModel): PictureModel {
        authorizationService.checkForAuthorization(token = token, userId = userId)
        return pictureService.updatePictureOfUser(userId = userId, pictureId = pictureId, updateModel = updateModel)
    }

    @DeleteMapping("/users/{userId}/pictures/{pictureId}")
    fun deletePicture(@PathVariable userId: UserId,
                      @PathVariable pictureId: PictureId,
                      @RequestHeader(value="Authorization") token: String): Int {
        authorizationService.checkForAuthorization(token = token, userId = userId)
        return pictureService.deletePicture(userId = userId, pictureId = pictureId)
    }
}