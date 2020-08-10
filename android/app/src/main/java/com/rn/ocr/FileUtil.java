/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.rn.ocr;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class FileUtil {
    private static String cameraPath = "/ocr_app/camera";

    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }
}
