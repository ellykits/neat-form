package com.nerdstone.neatformcore.datasource;

import android.content.Context;

import com.nerdstone.neatformcore.domain.data.FileSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import io.reactivex.Single;

public class AssetFile implements FileSource {

    public static InputStream openFileAsset(Context context, String path) throws IOException {
        return context.getResources().getAssets().open(path);
    }

    @Override
    public Single<String> readAssetFileAsString(Context context, String filePath) {
        return Single.fromCallable(() -> {
            InputStream inputStream = openFileAsset(context, filePath);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer, StandardCharsets.UTF_8);
        });
    }
}
