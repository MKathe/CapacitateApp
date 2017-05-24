package com.cerezaconsulting.compendio.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by Hesam on 15/04/14
 */
public class MyImageClient {

  /*  private static final  String TAG = "ImageDownloader";
    public static final String DIRECTORY_NAME = "APP-NAME";

    private static MyImageClient instance = null;
    private ImageLoader imageLoader;
    private DisplayImageOptions mDisplayImageOptions;

    protected MyImageClient(Context context) {
        // Exists only to defeat instantiation.
        configImageDownloader(context);
    }

    public static MyImageClient getInstance(Context context) {
        if(instance == null) {
            instance = new MyImageClient(context);
        }

        return instance;
    }

    *//**
     * This constructor will configure loader object in order to display image.
     * @param context
     *//*
    private void configImageDownloader(Context context) {

        File cacheDir = StorageUtils.getOwnCacheDirectory(context, DIRECTORY_NAME + "/Cache");

        // Get singleton instance of ImageLoader
        imageLoader = ImageLoader.getInstance();

        // Display options
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .resetViewBeforeLoading(true)
                .build();

        // Create configuration for ImageLoader (all options are optional)
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .denyCacheImageMultipleSizesInMemory()
                .threadPoolSize(4)
                .memoryCache(new WeakMemoryCache()) // LruMemoryCache(2 * 1024 * 1024)
                .memoryCacheSizePercentage(15)
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .defaultDisplayImageOptions(mDisplayImageOptions)
                .build();

        // Initialize ImageLoader with created configuration.
        imageLoader.init(config);
    }


    public void displayImage(final ImageView imageView, String imageURI) {
        if(imageView == null  ||  imageURI == null) {
            Log.e(TAG, "Either of image view or image uri is null");
            return;
        }

        // Load and display image
        imageLoader.displayImage(imageURI, imageView, mDisplayImageOptions, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String s, View view) {}

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {}

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {}

            @Override
            public void onLoadingCancelled(String s, View view) {}
        });
    }*/
}
