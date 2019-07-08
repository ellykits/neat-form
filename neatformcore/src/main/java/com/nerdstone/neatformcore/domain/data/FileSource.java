package com.nerdstone.neatformcore.domain.data;

import android.content.Context;

import io.reactivex.Single;

/***
 * Contract for AssetFiles
 */
public interface FileSource {

    Single<String> readAssetFileAsString(Context context, String filePath);

}
