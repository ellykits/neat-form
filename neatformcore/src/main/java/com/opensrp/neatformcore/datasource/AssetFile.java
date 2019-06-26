package com.opensrp.neatformcore.datasource;

import android.content.Context;

import com.opensrp.neatformcore.domain.data.FileSource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import io.reactivex.Single;

public class AssetFile implements FileSource {

    @Override
    public Single<String> readFile(Context context, String filePath) {
        return Single.fromCallable(() -> {
            InputStream inputStream = context.getAssets().open(filePath);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer, StandardCharsets.UTF_8);
        });
    }
}
