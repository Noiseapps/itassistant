package com.noiseapps.itassistant.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@EBean
public class ImageUtils {

    private final static String AVATARS = "/avatars/";
    @RootContext
    Context context;

    public String saveAvatar(Bitmap avatarBitmap, String avatarFilename) {
        try {
            final File file = new File(getBaseDirectory(), avatarFilename);
            final FileOutputStream outputStream = new FileOutputStream(file);
            avatarBitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            return file.getAbsolutePath();
        } catch (IOException e) {
            Logger.e(e, e.getMessage());
            return null;
        }
    }

    private String getBaseDirectory() {
        File filesDirectory = context.getExternalFilesDir(null);
        if (filesDirectory == null) {
            filesDirectory = context.getFilesDir();
        }
        return filesDirectory.getPath();

    }

    private boolean makeDir(@NonNull String fileDirectoryPath) {
        final File f = new File(fileDirectoryPath);
        return f.exists() || f.mkdirs();
    }

}
