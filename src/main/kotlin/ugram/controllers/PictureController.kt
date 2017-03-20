package ugram.controllers

import org.springframework.web.bind.annotation.*
import ugram.services.PictureService

@RestController @CrossOrigin
class PictureController(val pictureService: PictureService) {

    @GetMapping("/pictures")
    fun findAllPicturesPaginated(@RequestParam(defaultValue = "0") page: Int,
                                 @RequestParam(defaultValue = "10") perPage: Int)
            = pictureService.getAllPicturesPaginated(page = page, perPage = perPage)

}