package ugram.models

typealias PictureId = Int

data class PictureModel(
        var id: Int?,
        val creationDate: Int,
        var description: String,
        var mentions: List<String>,
        var tags: List<String>,
        var url: String,
        val userId: String)
{
    fun applyUpdate(update: UpdatePictureModel) {
        this.description = update.description
        this.mentions = update.mentions
        this.tags = update.tags
    }
    companion object Factory {
        fun create(updateModel: UpdatePictureModel, userId: UserId, creationDate: Int, url: String): PictureModel =
                PictureModel(
                        id = null,
                        creationDate = creationDate,
                        description = updateModel.description,
                        mentions = updateModel.mentions,
                        tags = updateModel.tags,
                        url = url,
                        userId = userId
                )
    }
}

data class UpdatePictureModel(
        val description: String,
        val mentions: List<String>,
        val tags : List<String>
)