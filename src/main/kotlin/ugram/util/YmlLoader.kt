package ugram.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.nio.file.Files
import java.nio.file.Paths

fun <T> loadYml(fileName: String, configClass: Class<T>): T {
    val mapper = ObjectMapper(YAMLFactory()).registerModule(KotlinModule())
    val path = Paths.get(ClassLoader.getSystemResource(fileName).toURI())

    return Files.newBufferedReader(path).use {
        mapper.readValue(it, configClass)
    }
}