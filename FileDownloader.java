package com.bucs.bupc.art.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by John Paul Cas on 2/8/2016.
 *
 * @author John Paul Cas
 *
 * connect to <i> John Paul Cas </i> on facebook <br />
 * <br />
 * facebook.com/cassie.next <br />
 * facebook.com/simplePlan.cas
 *
 */
public class FileDownloader {
    private static final int MEGABYTE = 1024 * 1024;

    public static boolean downloadFile(String fileUrl, File dir) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(dir);
            int totalSize = urlConnection.getContentLength();

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
