package com.bucs.bupc.art.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by John Paul Cas on 2/1/2016.
 *
 * This will detect network state of the application
 *
 * @author John Paul Cas
 * @since February 2016
 *
 * connect to <i> John Paul Cas </i> on facebook <br />
 * <br />
 * facebook.com/cassie.next <br />
 * facebook.com/simplePlan.cas
 *
 */
public class NetworkDetector {

    private Context context;

    public NetworkDetector(Context context) { this.context = context; }

    public boolean isConnected() {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }

        return isConnected;
    }

}
