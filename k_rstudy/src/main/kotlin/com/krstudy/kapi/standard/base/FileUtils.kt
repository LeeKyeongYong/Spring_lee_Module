package com.krstudy.kapi.standard.base

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object FileUtils {

    fun saveQRCodeToFile(image: ByteArray, baseFilePath: String) {
        val filePath = generateFilePathWithDate(baseFilePath)
        val path = Paths.get(filePath)
        ensureDirectoryExists(path)
        try {
            Files.write(path, image)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun generateFilePathWithDate(baseFilePath: String): String {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val now = LocalDateTime.now().format(dateFormatter)
        val fileName = "qr_code_$now.png"
        return Paths.get(baseFilePath).parent.resolve(fileName).toString()
    }

    fun ensureDirectoryExists(path: Path) {
        val parentPath: Path = path.parent
        if (!Files.exists(parentPath)) {
            try {
                Files.createDirectories(parentPath)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
