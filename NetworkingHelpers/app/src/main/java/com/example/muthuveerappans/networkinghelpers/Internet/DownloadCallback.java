package com.example.muthuveerappans.networkinghelpers.Internet;

import android.net.NetworkInfo;

/**
 * Created by muthuveerappans on 7/18/17.
 */

public interface DownloadCallback<T> {

    interface Progress {
        int ERROR = -1;
        int CONNECT_SUCCESS = 0;
        int DOWNLOADING = 1;
        int DONE = 2;
    }

    /*
    Called when the response is received from the HTTP call.
     */
    void onResponseReceived(T result);

    /*
    Get the device's active network status in the form of a NetworkInfo object.
     */
    NetworkInfo getActiveNetworkInfo();

    /*
    Callback for receiving the update of the data bytes received/sent.
     */
    void onProgressUpdate(int progressCode, int percentComplete);

    /*
    This is method is called when the network operation is finished. This is even called if the operation is un-successful.
     */
    void onFinish();
}
