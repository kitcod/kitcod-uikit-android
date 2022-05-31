package com.kitcod.android.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;


public class BackgroundNotificationService extends IntentService {

    public BackgroundNotificationService() {
        super("Service");
    }

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private int totalFileSize;


    @Override
    protected void onHandleIntent(Intent intent) {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("id", "an", NotificationManager.IMPORTANCE_LOW);

            notificationChannel.setDescription("no sound");
            notificationChannel.setSound(null, null);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        String file = intent.getStringExtra("TEST");
        String ImageText = file.substring(file.lastIndexOf("/") + 1, file.length());
        notificationBuilder = new NotificationCompat.Builder(this, "id")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle("Download")
                .setContentText("Downloading " + ImageText)
                .setDefaults(0)
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        initRetrofit(intent.getStringExtra("TEST"));

    }

    private void initRetrofit(String test) {
        String baseURL = test.substring(0, test.lastIndexOf("/") + 1);
        String ImageText = test.substring(test.lastIndexOf("/") + 1, test.length());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .build();

        APIsInterface retrofitInterface = retrofit.create(APIsInterface.class);

        Call<ResponseBody> request = retrofitInterface.downloadImage(ImageText);
        try {

            downloadImage(request.execute().body(), ImageText);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }


    private void downloadImage(ResponseBody body, String imageText) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream inputStream = new BufferedInputStream(body.byteStream(), 1024 * 8);
//        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), imageText);
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/CollabDeen");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        File outputFile = new File(myDir, imageText);


        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = outputFile.getName().substring(outputFile.getName().lastIndexOf(".") + 1);
        String type = mime.getMimeTypeFromExtension(ext);
        Intent openFile = new Intent(Intent.ACTION_VIEW, Uri.fromFile(outputFile));
        openFile.setDataAndType(Uri.fromFile(outputFile), type);
        openFile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent p = PendingIntent.getActivity(getApplicationContext(), 0, openFile, 0);
        OutputStream outputStream = new FileOutputStream(outputFile);
        long total = 0;
        boolean downloadComplete = false;
        //int totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));

        while ((count = inputStream.read(data)) != -1) {

            total += count;
            int progress = (int) ((double) (total * 100) / (double) fileSize);
            totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));
            sendIntent(progress, totalFileSize, current);
            updateNotification(progress);
            outputStream.write(data, 0, count);
            downloadComplete = true;
        }
        onDownloadComplete(downloadComplete, p, outputFile.getName());
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        Uri path = Uri.fromFile(outputFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (outputFile.getPath().contains(".db")) {
            return;
        }
        if (outputFile.getPath().toString().contains(".doc") || outputFile.getPath().contains(".docx")) {
            // Word document
            intent.setDataAndType(path, "application/msword");
        } else if (outputFile.getPath().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(path, "application/pdf");
        } else if (outputFile.getPath().contains(".ppt") || outputFile.getPath().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(path, "application/vnd.ms-powerpoint");
        } else if (outputFile.getPath().contains(".xls") || outputFile.getPath().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(path, "application/vnd.ms-excel");
        } else if (outputFile.getPath().contains(".zip") || outputFile.getPath().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(path, "application/x-wav");
        } else if (outputFile.getPath().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(path, "application/rtf");
        } else if (outputFile.getPath().contains(".wav") || outputFile.getPath().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(path, "audio/x-wav");
        } else if (outputFile.getPath().contains(".gif")) {
            // GIF file
            intent.setDataAndType(path, "image/gif");
        } else if (outputFile.getPath().contains(".jpg") || outputFile.getPath().contains(".jpeg") || outputFile.getPath().contains(".png")) {
            // JPG file
            intent.setDataAndType(path, "image/jpeg");
        } else if (outputFile.getPath().contains(".txt")) {
            // Text file
            intent.setDataAndType(path, "text/plain");
        } else if (outputFile.getPath().contains(".3gp") || outputFile.getPath().contains(".mpg") || outputFile.getPath().contains(".mpeg") || outputFile.getPath().contains(".mpe") || outputFile.getPath().contains(".mp4") || outputFile.getPath().contains(".avi")) {
            // Video files
            intent.setDataAndType(path, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(path, "*/*");
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {

        }

    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

    private void updateNotification(int currentProgress) {
        notificationBuilder.setProgress(100, currentProgress, false);
        notificationBuilder.setContentText("Downloaded: " + currentProgress + "%");
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download);
        notificationManager.notify(0, notificationBuilder.build());
    }


    /* private void sendProgressUpdate(boolean downloadComplete) {

         Intent intent = new Intent(MainActivity.PROGRESS_UPDATE);
         intent.putExtra("downloadComplete", downloadComplete);
         LocalBroadcastManager.getInstance(BackgroundNotificationService.this).sendBroadcast(intent);
     }*/
    private void sendIntent(int progress, int totalFileSize, double current) {

        Intent intent = new Intent("message_progress");
        intent.putExtra("progress", progress);
        intent.putExtra("fileSize", totalFileSize);
        intent.putExtra("current", current);
        LocalBroadcastManager.getInstance(BackgroundNotificationService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(boolean downloadComplete, PendingIntent p, String name) {
//        sendProgressUpdate(downloadComplete);
        sendIntent(100, totalFileSize, totalFileSize);
        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText(name + " Downloaded");
        notificationBuilder.setContentIntent(p);
        notificationManager.notify(0, notificationBuilder.build());

    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

}
