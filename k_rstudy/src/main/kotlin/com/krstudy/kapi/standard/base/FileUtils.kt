package com.krstudy.kapi.standard.base

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.io.IOException

object FileUtils {
    fun ensureDirectoryExists(filePath: String) {
        val path: Path = Paths.get(filePath).parent
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}