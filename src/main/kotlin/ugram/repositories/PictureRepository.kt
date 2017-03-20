package ugram.repositories

import ugram.database.Mentions
import ugram.database.Pictures
import ugram.database.Pictures_Tags
import ugram.models.PictureId
import ugram.models.PictureModel
import ugram.models.UserId
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder

class PictureRepository {

    fun create(model: PictureModel): PictureId {
        val picId = Pictures.insert( toRow(model) )[Pictures.id]
        updateTags(picId, model.tags)
        updateMentions(picId, model.mentions)
        return picId
    }

    fun update(picId: PictureId, model: PictureModel) {
        Pictures.update(where = { Pictures.id eq picId }, body = toRow(model))
        updateTags(picId, model.tags)
        updateMentions(picId, model.mentions)
    }

    fun updateUrl(picId: PictureId, url: String) {
        Pictures.update(where = { Pictures.id eq picId }, body = { it[Pictures.url] = url })
    }

    fun find(picId: PictureId): PictureModel? =
            Pictures.select { Pictures.id eq picId }
                    .map { fromRow(it) }
                    .firstOrNull()

    fun findAllPaginated(pageNumber: Int, perPage: Int): List<PictureModel> =
            Pictures.selectAll()
                    .limit(n = perPage, offset = pageNumber*perPage)
                    .map { fromRow(it) }

    fun findByUserPaginated(userId: UserId, pageNumber: Int, perPage: Int): List<PictureModel> =
            Pictures.select { Pictures.userId eq userId }
                    .limit(n = perPage, offset = pageNumber*perPage)
                    .map { fromRow(it) }

    fun countTotal() =
            Pictures.selectAll().count()

    fun countTotalByUser(userId: UserId) =
            Pictures.select { Pictures.userId eq userId }.count()

    fun delete(pictureId: PictureId) =
            Pictures.deleteWhere { Pictures.id eq pictureId }

    private fun toRow(u: PictureModel): Pictures.(UpdateBuilder<*>) -> Unit = {
        it[description] = u.description
        it[creationDate] = u.creationDate
        it[url] = u.url
        it[userId] = u.userId
    }

    private fun fromRow(r: ResultRow) =
        PictureModel(
                id = r[Pictures.id],
                creationDate = r[Pictures.creationDate],
                description = r[Pictures.description],
                mentions = Mentions.select { Mentions.pictureId eq r[Pictures.id] }.map { it[Mentions.userId] },
                tags = Pictures_Tags.select { Pictures_Tags.pictureId eq r[Pictures.id] }.map { it[Pictures_Tags.tag] },
                url = r[Pictures.url],
                userId = r[Pictures.userId]
        )


    //TODO find something simpler
    private fun updateMentions(pictureId: PictureId, usersId: List<String>) {

        fun toMentionRow(picId: PictureId, uId: UserId): Mentions.(UpdateBuilder<*>) -> Unit = {
            it[Mentions.pictureId] = picId
            it[Mentions.userId] = uId
        }

        val mentions = Mentions.select { Mentions.pictureId eq pictureId }
        usersId
                .filter { uId -> mentions.all { it[Mentions.userId] != uId } }
                .forEach { uId -> Mentions.insert( toMentionRow(picId = pictureId, uId = uId) ) }

        mentions
                .filter { row -> usersId.all { uId -> row[Mentions.userId] != uId } }
                .forEach { row -> Mentions.deleteWhere {
                    (Mentions.userId eq row[Mentions.userId]) and (Mentions.pictureId eq pictureId)
                } }
    }

    //TODO find something simpler
    private fun updateTags(pictureId: PictureId, tags: List<String>){

        fun toTagRow(picId: PictureId, tag: String): Pictures_Tags.(UpdateBuilder<*>) -> Unit = {
            it[Pictures_Tags.pictureId] = picId
            it[Pictures_Tags.tag] = tag
        }

        val pictureTags = Pictures_Tags.select { Pictures_Tags.pictureId eq pictureId }
        tags
                .filter { tag -> pictureTags.all { it[Pictures_Tags.tag] != tag } }
                .forEach { tag -> Pictures_Tags.insert( toTagRow(picId = pictureId, tag = tag) ) }

        pictureTags
                .filter { row -> tags.all { tag -> row[Pictures_Tags.tag] != tag } }
                .forEach { row -> Pictures_Tags.deleteWhere {
                    (Pictures_Tags.tag eq row[Pictures_Tags.tag]) and (Pictures_Tags.pictureId eq pictureId)
                } }
    }
}