package com.bucs.bupc.art.rest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by John Paul Cas on 1/31/2016.
 *
 * @author John Paul Cas
 * @since January 2016
 *
 * connect to <i> John Paul Cas </i> on facebook <br />
 * <br />
 * facebook.com/cassie.next <br />
 * facebook.com/simplePlan.cas
 *
 */
public class RestClient {

    static String stream = null;

    public RestClient() {}

    /**
     *
     * @param urlString the url of json data
     * @return data from specified url
     */
    public String getHttpDataRequest(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

            if(httpURLConnection.getResponseCode() == 200) {

                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                // end reading
                stream = stringBuilder.toString();

                // disconnect the HttpUrlConnection
                httpURLConnection.disconnect();
            } else {
                /* Do nothing */
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stream;
    }
}
