package ugram.config

data class Config(
        val database: DbConfig,
        val s3Repo: S3RepoConfig,
        val amazon: AmazonKeys
)

data class DbConfig(
        val username: String,
        val password: String,
        val host: String,
        val port: Int,
        val db_name: String
)

data class S3RepoConfig(
        val bucketName: String,
        val folderName: String
)

data class AmazonKeys(
        val accessKey: String,
        val secretKey: String
)