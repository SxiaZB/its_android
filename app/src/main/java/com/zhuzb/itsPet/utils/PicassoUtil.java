package com.zhuzb.itsPet.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * @author zhuzb
 * @date on 2018/5/20 0020
 * @email zhuzhibo2017@163.com
 */

public class PicassoUtil {


    /**
     * 加载指定宽高的图片
     *
     * @param context
     * @param path
     * @param width
     * @param height
     * @param imageView
     */
    public static void loadImageWithSize(Context context, String path, int width, int height, int placeholderresId, int errorResId, ImageView imageView) {
        Picasso.with(context).load(path).resize(width, height).centerCrop().placeholder(placeholderresId).error(errorResId).into(imageView);
    }

    public static void loadImageWithHodler(Context context, String path, int resId, ImageView imageView) {
        Picasso.with(context).load(path).fit().placeholder(resId).into(imageView);
    }

    public static void loadImageWithCrop(Context context, String path, ImageView imageView) {
        Picasso.with(context).load(path).transform(new CustomImageCrop()).into(imageView);
    }

    /**
     * 实现自定义图片裁剪
     */
    private static class CustomImageCrop implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result == null) {
                result.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "square()";
        }
    }
}