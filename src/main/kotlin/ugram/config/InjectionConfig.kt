package ugram.config

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ugram.repositories.PictureRepository
import ugram.repositories.S3ImageRepository
import ugram.repositories.UserRepository
import ugram.services.AuthorizationService
import ugram.services.PictureService
import ugram.services.UserService
import ugram.util.loadYml

@Configuration
open class InjectionConfig {
    val config by lazy {
        loadYml("config.yml", Config::class.java)
    }
    @Bean open fun pictureService(): PictureService = PictureService(PictureRepository(), s3ImageRepository())
    @Bean open fun userService(): UserService = UserService(UserRepository())
    @Bean open fun authorizationService(): AuthorizationService = AuthorizationService()
    @Bean open fun s3ImageRepository(): S3ImageRepository = S3ImageRepository(amazonS3Client(), config.s3Repo)
    @Bean open fun amazonS3Client(): AmazonS3 = AmazonS3Client(
            BasicAWSCredentials(config.amazon.accessKey, config.amazon.secretKey)
    )
}