//package com.meembusoft.iot.util;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.PorterDuff;
//import android.graphics.PorterDuffXfermode;
//import android.util.DisplayMetrics;
//import android.util.TypedValue;
//import android.widget.ImageView;
//
///**
// * @author Md. Rashadul Alam
// * Email: rashed.droid@gmail.com
// */
//public class ShapeManager {
//
//    public static final String TAG = ShapeManager.class.getSimpleName();
//
//    /**
//     * @param context        you need to pass the context (ex: Your current activity's instance)
//     * @param shapedImage    pass an image from your drawable folder (Ex: R.drawable.sample_image_name)
//     *                       which is shaped for your real image.Your expected image will look like this.
//     * @param originalImage  pass your image from your drawable (Ex: R.drawable.your_image_name).
//     *                       This is your original image.
//     * @param imgView        pass an ImageView in which you want to set your expected image.
//     * @param expectedHeight (int value) this is expected height for your image.
//     * @param expectedWidth  (int value) this is expected width for your image.
//     *                       <p>
//     *                       ***YOUR SHAPED IMAGE SIZE WILL BE FOR YOUR MDPI DEVICE.
//     *                       IF YOUR SHAPED IMAGE SIZE IS 100PX THEN YOU
//     *                       NEED TO PASS HEIGHT AND WIDTH 100 FOR YOUR MDPI DEVICE.
//     *                       ***100X1.5 FOR YOUR HDPI DEVICE
//     *                       ***100X2 FOR YOUR XHDPI DEVICE
//     *                       ***100X3 FOR YOUR XXHDPI DEVICE
//     *                       ***100X4 FOR YOUR XXXHDPI DEVICE
//     *                       ######## height and width can be different ########
//     *                       ***
//     */
//    public static void shape(Context context, int originalImage, int shapedImage, ImageView imgView, int expectedHeight, int expectedWidth) {
//
//        Bitmap original = BitmapFactory.decodeResource(context.getResources(), originalImage);
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), shapedImage);
//
//        Bitmap mask = bitmap;
//
////        original = getResizedBitmap(original, expectedHeight, expectedWidth);
//        original = getResizedBitmap(original, dipToPixels(context, expectedHeight), dipToPixels(context, expectedWidth));
//
//        int bitmapHeight = bitmap.getHeight();
//        int bitmapWidth = bitmap.getWidth();
//        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
//        final Canvas mCanvas = new Canvas(result);
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//
//        int widthMask = mask.getWidth();
//        int heightMask = mask.getHeight();
//        float centerX = (widthMask - original.getWidth()) * 0.5f;
//        float centerY = (heightMask - original.getHeight()) * 0.5f;
//
//        mCanvas.drawBitmap(original, centerX, centerY, null);
//        mCanvas.drawBitmap(mask, 0, 0, paint);
//        paint.setXfermode(null);
//
//        imgView.getLayoutParams().height = bitmapHeight;
//        imgView.getLayoutParams().width = bitmapWidth;
//        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imgView.setImageBitmap(result);
//        imgView.setAdjustViewBounds(true);
//
//    }
//
//    /**
//     * By using this method you can resize your image
//     *
//     * @param image     pass a bitmap image for resizing.
//     * @param newHeight pass your expected new height in px (int value).
//     * @param newWidth  pass your expected new width in px (int value).
//     */
//    public static Bitmap getResizedBitmap(Bitmap image, float newHeight, float newWidth) {
//        int width = image.getWidth();
//        int height = image.getHeight();
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleWidth, scaleHeight);
//        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height, matrix, false);
//
//        return resizedBitmap;
//    }
//
//
//    public static float dipToPixels(Context context, float dipValue) {
//        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
//    }
//}