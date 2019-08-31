package com.nerdstone.neatformcore.domain.data

import android.content.Context

import io.reactivex.Single

/***
 * Contract for AssetFiles
 */
interface FileSource {

    fun readAssetFileAsString(context: Context, filePath: String): Single<String>

}
