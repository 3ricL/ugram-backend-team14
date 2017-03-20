package ugram.services

import org.jetbrains.exposed.sql.transactions.transaction
import ugram.exceptions.*
import ugram.models.*
import ugram.repositories.PictureRepository
import ugram.repositories.S3ImageRepository
import ugram.util.calculateNbPages
import ugram.util.nowTimestamp

class PictureService(private val pictureRepository: PictureRepository,
                     private val imageRepository: S3ImageRepository) {

    fun getAllPicturesPaginated(page: Int, perPage: Int): Pagination<PictureModel> =
            transaction {
                val totalEntries = pictureRepository.countTotal()
                Pagination(
                        items = pictureRepository.findAllPaginated(pageNumber = page, perPage = perPage),
                        totalEntries = totalEntries,
                        totalPages = calculateNbPages(total = totalEntries, perPage = perPage)
                )

            }

    fun getPicturesOfUserPaginated(userId: UserId, page: Int, perPage: Int): Pagination<PictureModel> =
            transaction {
                val totalEntries = pictureRepository.countTotalByUser(userId)
                Pagination(
                        items = pictureRepository.findByUserPaginated(userId = userId,  pageNumber = page, perPage = perPage),
                        totalEntries = 2,
                        totalPages = calculateNbPages(total = totalEntries, perPage = perPage)
                )
            }

    fun getSpecificUserPicture(userId: UserId, pictureId: PictureId): PictureModel =
            transaction {
                val picture = pictureRepository.find(picId = pictureId)
                if(picture === null || picture.userId == userId) throw UnableToFindPictureOfUser()
                picture
            }

    fun addPicture(userId: UserId, updateModel: UpdatePictureModel, byteArr: ByteArray): PictureModel =
            transaction {
                val pictureModel = PictureModel.create(
                        updateModel = updateModel,
                        userId = userId,
                        creationDate = nowTimestamp(),
                        url = ""
                )
                val pictureId = pictureRepository.create(pictureModel)
                val url = imageRepository.uploadPng(
                        data = byteArr,
                        path = pictureUrlBuilder(userId = userId, pictureId = pictureId)
                )
                pictureRepository.updateUrl(picId = pictureId, url = url)
                pictureModel.id = pictureId
                pictureModel.url = url
                pictureModel
            }

    fun updatePictureOfUser(userId: UserId, pictureId: PictureId, updateModel: UpdatePictureModel): PictureModel =
            transaction {
                val picture = pictureRepository.find(pictureId)
                if (picture === null) { throw UnableToFindPictureException() }
                if (picture.userId !== userId) { throw UnableToUpdatePictureException() }

                picture.applyUpdate(updateModel)
                pictureRepository.update(picId = pictureId, model = picture)

                picture
            }

    fun deletePicture(userId: UserId, pictureId: PictureId) =
            transaction {
                val picture = pictureRepository.find(picId = pictureId)
                if (picture === null) { throw UnableToFindPictureException() }
                if (picture.userId !== userId) { throw UnableToDeletePictureException() }
                pictureRepository.delete(pictureId)
            }

    private fun pictureUrlBuilder(userId: UserId, pictureId: PictureId): String {
        return "$pictureId"
    }
}