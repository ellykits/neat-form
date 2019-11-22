package com.nerdstone.neatformcore.domain.data

import android.content.Context

/***
 * Contract for AssetFiles
 */
interface FileSource {

    fun readAssetFileAsString(context: Context, filePath: String): String

}
