package com.nerdstone.neatformcore.datasource

import android.content.Context
import com.nerdstone.neatformcore.domain.data.FileSource
import io.reactivex.Single
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

class AssetFile : FileSource {

    override fun readAssetFileAsString(context: Context, filePath: String): Single<String> {
        return Single.fromCallable {
            val inputStream = openFileAsset(context, filePath)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, StandardCharsets.UTF_8)
        }
    }

    companion object {

        @Throws(IOException::class)
        fun openFileAsset(context: Context, path: String): InputStream {
            return context.resources.assets.open(path)
        }
    }
}
