package com.yd.org.sellpopularizesystem.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.yd.org.sellpopularizesystem.clippicture.MonitoredActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by ${bai} on 17/2/7.
 */

public class BitmapUtil {


    private static final int BUFFER_SIZE = 10 * 1024;
    /**
     * 预设SD卡空间 (单位M)
     */
    public static final long CACHE_SIZE = 100;
    public static final int MB = 1024 * 1024;
    public static final String SDCARD_PATH = ("Android" + File.separator + "data" + File.separator).intern();

    /**
     * 默认为可用状
     */
    public static MountStatuds SDCardStatus = MountStatuds.SD_CARD_AVAILABLE;

    /**
     * 启动相机
     *
     * @param
     * @return
     */
    public static void startImageCapture(Activity act, int resultCode) {
        Uri photoURI=null;
        String mPublicPhotoPath="";
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断是否有相机应用
        if (takePictureIntent.resolveActivity(act.getPackageManager()) != null) {
            //创建临时图片文件
            File photoFile = null;
            try {
                photoFile = createPublicImageFile();
                mPublicPhotoPath = photoFile.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //设置Action为拍照
            if (photoFile != null) {
                takePictureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                //这里加入flag
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //如果是7.0或以上
                    photoURI = FileProvider.getUriForFile(act, "applicationId.fileprovider", photoFile);
                }else {
                    photoURI=Uri.fromFile(photoFile);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                act.startActivityForResult(takePictureIntent, resultCode);
            }
        }


        /*File photoFile=createPublicImageFile();
        Uri outputUri = Uri.fromFile(photoFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        act.startActivityForResult(intent, resultCode);*/

        /*File cameraFile = new File(BitmapUtil.getCacheDir(act), "camera.jpg");
        Uri outputUri = Uri.fromFile(cameraFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        act.startActivityForResult(intent, resultCode);*/
    }

    public static File createPublicImageFile() {
        File appDir = new File(Environment.getExternalStorageDirectory() + "/yingjia");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        return file;
    }

    /**
     * 启动系统相册
     *
     * @param resultCode
     */
    public static void gotoSysPic(Activity act, int resultCode) {
        Intent getImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getImageIntent.addCategory(Intent.CATEGORY_OPENABLE);
        getImageIntent.setType("image/*");//调用的是系统图库
        //getImageIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        act.startActivityForResult(getImageIntent, resultCode);
    }

    //根据uri获取图片路径
    public static String getImagePath(Activity act, Uri selectedPhotoUri, String selection, String[] selectionArgs) {
        String picPath = "null";
        String id = null;
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.KITKAT) {
            Log.e("content", selectedPhotoUri.toString());
            //获取图片的路径：
            String str = getDataColumn(act, selectedPhotoUri, null, null);
            Log.e("str", "getImagePath: " + str);
            return str;
        } else {
            Log.e("content", selectedPhotoUri.toString());
            if (selectedPhotoUri.toString().startsWith("content://media/external/images/media/")){
                String str = getDataColumn(act, selectedPhotoUri, null, null);
                return str;
            }
            if (DocumentsContract.isDocumentUri(act, selectedPhotoUri)) {
                if (isExternalStorageDocument(selectedPhotoUri)) {
                    String docId = DocumentsContract.getDocumentId(selectedPhotoUri);
                    String[] split = null;
                    if (docId.contains("%3A")) {
                        split = docId.split("%3A");
                    } else {
                        split = docId.split("\\:");
                    }
                    String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
            } else if (isDownloadsDocument(selectedPhotoUri)) {
                String docid = DocumentsContract.getDocumentId(selectedPhotoUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(act, selectedPhotoUri, null, null);
            } else if (isMediaDocument(selectedPhotoUri)) {
                String docId = DocumentsContract.getDocumentId(selectedPhotoUri);
                String[] split = docId.split("\\:");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    if (docId.contains("%3A")) {
                        id = docId.split("%3A")[1];
                    } else {
                        id = docId.split(":")[1];
                    }
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String sel = MediaStore.Images.Media._ID + "=?";
                return getDataColumn(act, contentUri, sel, new String[]{id});
            }
        }
        return null;
    }


    private static String getDataColumn(Activity act, Uri selectedPhotoUri, String selection, String[] selectionArgs) {
        String strPicPath = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cr = act.getContentResolver().query(selectedPhotoUri, proj, selection, selectionArgs, null);
        //获得选择的图片的索引值
        int column_index = cr.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        //将光标移至开头 ，这个很重要，不小心很容易引起越界
        cr.moveToFirst();
        //picFile = new File(String.valueOf(selectedPhotoUri));
        //最后根据索引值获取图片路径
        strPicPath = cr.getString(column_index);
        cr.close();
        return strPicPath;
    }

    //是否为内部存储文件
    private static boolean isExternalStorageDocument(Uri uri) {

        return "com.android.externalstorage.documents".equals(uri.getAuthority());

    }

    //是否为下载文件
    private static boolean isDownloadsDocument(Uri uri) {

        return "com.android.providers.downloads.documents".equals(uri.getAuthority());

    }

    //是否为媒体文件
    private static boolean isMediaDocument(Uri uri) {

        return "com.android.providers.media.documents".equals(uri.getAuthority());

    }

    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        while (true) {
            int count = is.read(bytes, 0, BUFFER_SIZE);
            if (count == -1) {
                break;
            }
            os.write(bytes, 0, count);
        }
    }

    /**
     * 2      * @param uri
     * 3      * @param width
     * 4      * @param height
     * 5      * @return
     * 6      * @throws IOException
     * 7
     */
    public static Bitmap getThumbnail(Context context, Uri uri, int width, int height) {
        Bitmap bitmap = null;
        InputStream input = null;
        try {
            ContentResolver resolver = context.getContentResolver();
            input = resolver.openInputStream(uri);
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            /**
             * 如果设置inJustDecodeBounds为true，仍可以获取到bitmap信息，但完全不用分配内存，因为没有获取像素，所以我们可以利用得到的Bitmap的大小，
             * 重新压缩图片，然后在内存中生成一个更小的Bitmap，这样即便是一个4MB的JPG，我们也可以随心所欲地把他压缩到任意大小，从而节省了内存
             */
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            if (input != null) {
                input.close();
            }
            if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
                return bitmap;
            }
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            /**
             * 计算Bitmap的缩放比例
             */
            bitmapOptions.inSampleSize = calculateInSampleSize(onlyBoundsOptions, width, height);
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            input = resolver.openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        } catch (Exception ex) {
            return bitmap;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ioEx) {
                }
            }
        }
        return bitmap;
    }

    /**
     * 2      * 计算图片的缩放比例
     * 3      * @param options
     * 4      * @param reqWidth
     * 5      * @param reqHeight
     * 6      * @return
     * 7
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        /**
         10          * 原始图片的高度和宽度
         11          */
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 在保证解析出的bitmap宽高分别大于目标尺寸宽高的前提下，取可能的inSampleSize的最大值
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    //设置自定义大小bitmap
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // 首先设置 inJustDecodeBounds=true 来获取图片尺寸
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 计算 inSampleSize 的值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 根据计算出的 inSampleSize 来解码图片生成Bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * BITMAP保存
     *
     * @param photo 要保存的BITMAP
     * @param
     */
    public static void writeBitmap(Bitmap photo, String fPath) {
        try {
            File sdCard = new File(fPath);
            FileOutputStream outStreamz = new FileOutputStream(sdCard);
            photo.compress(Bitmap.CompressFormat.PNG, 100, outStreamz);
            outStreamz.flush();
            outStreamz.close();
            if (photo.isRecycled()) {
                photo.recycle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getRootPath(Context context) {
        StringBuilder sb = new StringBuilder();

        SDCardStatus = getSDCardStatus();
        switch (SDCardStatus) {
            case SD_CARD_AVAILABLE:
            case SD_CARD_SPACE_NOT_ENOUGH:
                sb.append(Environment.getExternalStorageDirectory().getPath()).append(File.separator).append(SDCARD_PATH).append(context.getPackageName());
                break;
            case SD_CARD_NOT_AVAILABLE:
                sb.append(context.getCacheDir().getPath());
                break;
        }
        return sb.toString();
    }

    public static MountStatuds getSDCardStatus() {
        MountStatuds status;
        String sdState = Environment.getExternalStorageState();
        if (sdState.equals(Environment.MEDIA_MOUNTED)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long availCount = sf.getAvailableBlocks();
            long blockSize = sf.getBlockSize();
            long availSize = availCount * blockSize / MB;
            /** 100M内存空间大小 */
            if (availSize < CACHE_SIZE) {
                /** TODO 是否提示用户空间不够 */
                status = MountStatuds.SD_CARD_SPACE_NOT_ENOUGH;
            } else {
                status = MountStatuds.SD_CARD_AVAILABLE;
            }
        } else {
            status = MountStatuds.SD_CARD_NOT_AVAILABLE;
        }
        return status;
    }


    public static String getCacheDir(Context context) {
        File f = new File(getRootPath(context), "cache".intern());
        if (!f.exists()) {
            f.mkdirs();
        }
        return f.getPath();
    }


    public static void startBackgroundJob(MonitoredActivity activity,
                                          String title, String message, Runnable job, Handler handler) {
        ProgressDialog dialog = ProgressDialog.show(activity, title, message,
                true, false);
        new Thread(new BackgroundJob(activity, job, dialog, handler)).start();
    }


    private static class BackgroundJob extends
            MonitoredActivity.LifeCycleAdapter implements Runnable {

        private final MonitoredActivity activity;
        private final ProgressDialog dialog;
        private final Runnable job;
        private final Handler handler;
        private final Runnable cleanupRunner = new Runnable() {
            public void run() {
                activity.removeLifeCycleListener(BackgroundJob.this);
                if (dialog.getWindow() != null)
                    dialog.dismiss();
            }
        };

        public BackgroundJob(MonitoredActivity activity, Runnable job,
                             ProgressDialog dialog, Handler handler) {
            this.activity = activity;
            this.dialog = dialog;
            this.job = job;
            this.activity.addLifeCycleListener(this);
            this.handler = handler;
        }

        public void run() {
            try {
                job.run();
            } finally {
                handler.post(cleanupRunner);
            }
        }

        @Override
        public void onActivityDestroyed(MonitoredActivity activity) {
            cleanupRunner.run();
            handler.removeCallbacks(cleanupRunner);
        }

        @Override
        public void onActivityStopped(MonitoredActivity activity) {
            dialog.hide();
        }

        @Override
        public void onActivityStarted(MonitoredActivity activity) {
            dialog.show();
        }
    }


    /**
     * 获取图片文件的信息，是否旋转了90度，如果是则反转
     *
     * @param bitmap 需要旋转的图片
     * @param path   图片的路径
     */
    public static Bitmap reviewPicRotate(Bitmap bitmap, String path) {
        Bitmap returnBm = null;
        int degree = getPicRotate(path);
        if (degree != 0) {
            // 根据旋转角度，生成旋转矩阵
            Matrix m = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            m.setRotate(degree); // 旋转angle度
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);// 从新生成图片
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return bitmap;
    }


    /**
     * 读取图片文件旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片旋转的角度
     */
    public static int getPicRotate(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * SD卡三种状
     */
    enum MountStatuds {
        SD_CARD_AVAILABLE, SD_CARD_NOT_AVAILABLE, SD_CARD_SPACE_NOT_ENOUGH
    }
}