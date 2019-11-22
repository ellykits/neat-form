package com.nerdstone.neatformcore.datasource

import android.content.Context
import com.nerdstone.neatformcore.domain.data.FileSource
import java.io.InputStream
import java.nio.charset.StandardCharsets

object AssetFile : FileSource {

    override fun readAssetFileAsString(context: Context, filePath: String): String {
        val inputStream = openFileAsset(context, filePath)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer, StandardCharsets.UTF_8)
    }

    fun openFileAsset(context: Context, path: String): InputStream {
        return context.resources.assets.open(path)
    }

}
