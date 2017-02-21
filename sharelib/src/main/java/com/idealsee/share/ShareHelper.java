package com.idealsee.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;


/**
 * 分享相关的辅助类
 */
public class ShareHelper {

    private static final int THUMB_SIZE_MAX = 150;

    public static Bitmap getThumbBitmapFromFile(String imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            Bitmap bmp = BitmapFactory.decodeFile(imageFile);
            if (bmp != null) {
                float scale = (float) THUMB_SIZE_MAX / Math.max(bmp.getWidth(), bmp.getHeight());
                int thumbWidth = (int) (scale * bmp.getWidth());
                int thumbHeight = (int) (scale * bmp.getHeight());
                return Bitmap.createScaledBitmap(bmp, thumbWidth, thumbHeight, true);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static byte[] bmpToByteArray(Bitmap bmp) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String mergeString(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String string: strings) {
            if (string != null && !string.isEmpty()) {
                   builder.append(string);
            }
        }
        return builder.toString();
    }
}
