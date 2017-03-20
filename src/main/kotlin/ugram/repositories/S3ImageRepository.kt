package ugram.repositories

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.slf4j.LoggerFactory
import java.io.IOException
import ugram.config.S3RepoConfig
import java.io.ByteArrayInputStream


class S3ImageRepository(private val s3Client: AmazonS3,
                        private val config: S3RepoConfig) {

    fun uploadPng(data: ByteArray, path: String): String {
        logger.info("Uploading '{}' to folder '{}' in bucket '{}'", config.folderName, config.bucketName)

        try {
            val stream: ByteArrayInputStream = ByteArrayInputStream(data)
            val meta: ObjectMetadata = ObjectMetadata()
            meta.contentLength = data.size.toLong()
            meta.contentType = "image/png"
            s3Client.putObject(PutObjectRequest(config.bucketName, path, stream, meta))
            s3Client.setObjectAcl(config.bucketName, path, CannedAccessControlList.PublicRead)
        } catch (e: AmazonServiceException) {
            logger.error("Unable to uploadPng file to S3", e)
        } catch (e: IOException) {
            logger.error("Unable to uploadPng file to S3", e)
        }
        return path
    }

    companion object {
        private val logger = LoggerFactory.getLogger(S3ImageRepository::class.java)
    }
}