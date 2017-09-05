package com.course4.posthttprequestsample.Internet;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by muthuveerappans on 8/11/17.
 */

public class NetworkTask {
    // Holds the data for internet connection
    private NetworkObject networkObject;

    // Background thread to handle network connection
    private DownloadTask mDownloadTask;

    // Callback to send results once fetched from the internet
    private DownloadCallback<Result> mCallback;

    public NetworkTask(NetworkObject networkObject, DownloadCallback<Result> callback) {
        this.networkObject = networkObject;
        this.mCallback = callback;
    }

    /**
     * Start non-blocking execution of DownloadTask.
     */
    public void startDownload() {
        cancelDownload();
        mDownloadTask = new DownloadTask(mCallback);
        mDownloadTask.execute(networkObject);
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing DownloadTask execution.
     */
    public void cancelDownload() {
        if (mDownloadTask != null) {
            mDownloadTask.cancel(true);
        }
    }

    /**
     * Implementation of AsyncTask designed to get/post data from/to the network.
     */
    private static class DownloadTask extends AsyncTask<NetworkObject, Integer, Result> {

        private static final int HTTP_TIMEOUT = 3000; //in millis

        private DownloadCallback<Result> mCallback;

        DownloadTask(DownloadCallback<Result> callback) {
            setCallback(callback);
        }

        private void setCallback(DownloadCallback<Result> callback) {
            mCallback = callback;
        }

        /**
         * Cancel background network operation if we do not have network connectivity.
         */
        @Override
        protected void onPreExecute() {
            if (mCallback != null) {
                NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected() ||
                        (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                                && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                    // If no connectivity, cancel task and update Callback with null data.
                    mCallback.onResponseReceived(new Result(new Exception("No Internet connection.")));
                    cancel(true);
                }
            }
        }

        /**
         * Defines work to perform on the background thread.
         */
        @Override
        protected Result doInBackground(NetworkObject... networkObjects) {
            Result results = null;

            if (!isCancelled() && networkObjects[0] != null) {
                results = downloadUrl(networkObjects[0]);
            }

            return results;
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(Result result) {
            if (result != null && mCallback != null) {
                mCallback.onResponseReceived(result);
            }

            if (mCallback != null)
                mCallback.onFinish();
        }

        /**
         * Given a URL, sets up a connection and gets the HTTP response body from the server.
         * If the network request is successful, it returns the response body in String form. Otherwise,
         * it will throw an IOException.
         */
        private Result downloadUrl(NetworkObject networkObject) {
            InputStream stream = null;
            HttpsURLConnection connection = null;
            Result result = null;
            try {
                URL url = new URL(networkObject.getURL());

                connection = (HttpsURLConnection) url.openConnection();
                // Timeout for reading InputStream arbitrarily set to 3000ms.
                connection.setReadTimeout(HTTP_TIMEOUT);
                // Timeout for connection.connect() arbitrarily set to 3000ms.
                connection.setConnectTimeout(HTTP_TIMEOUT);
                // set http method
                connection.setRequestMethod(networkObject.getHttpMethod());
                // Already true by default but setting just in case; needs to be true since this request
                // is carrying an input (response) body.
                connection.setDoInput(true);

                if (networkObject.getHttpMethod().equals("POST")) {
                    // set the connection content-type as JSON, meaning we are sending JSON data.
                    connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    // Send POST data.
                    publishProgress(DownloadCallback.Progress.DOWNLOADING);
                    DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
                    printout.write(networkObject.getPostData().getBytes("UTF-8"));
                    printout.flush();
                    printout.close();
                }

                publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);

                // Open communications link (network traffic occurs here).
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK ||
                        responseCode == HttpsURLConnection.HTTP_CREATED) {
                    // Retrieve the response body as an InputStream.
                    stream = connection.getInputStream();
                    publishProgress(DownloadCallback.Progress.DOWNLOADING);
                    if (stream != null) {
                        // Converts Stream to String with max length of contentLength.
                        // The content length may be null if the server uses Chunked Transfer Encoding to send data
                        // More about Chunked Transfer Encoding https://en.wikipedia.org/wiki/Chunked_transfer_encoding.
                        //
                        // Algorithm for dynamically allocating the buffer can be found
                        // https://stackoverflow.com/questions/10439829/urlconnection-getcontentlength-returns-1
                        //
                        // For simplicity we are using a constant value as the max buffer size.
                        // Should not use this in production.
                        String contentLength = connection.getHeaderField("Content-Length");
                        long contentLengthLong = contentLength == null ? 16384 : Long.parseLong(contentLength);
                        result = new Result(readStream(stream, (int) contentLengthLong), networkObject.getURL());
                    }
                } else {
                    result = new Result(new Exception("HTTP error code: " + responseCode));
                }
            } catch (Exception e) {
                result = new Result(e);
            } finally {
                // Close Stream and disconnect HTTPS connection.
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        result = new Result(e);
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            publishProgress(DownloadCallback.Progress.DONE);
            return result;
        }

        /**
         * Converts the contents of an InputStream to a String.
         */
        private String readStream(InputStream stream, int maxReadSize)
                throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] rawBuffer = new char[maxReadSize];
            int readSize;
            StringBuffer buffer = new StringBuffer();
            while (((readSize = reader.read(rawBuffer)) != -1) && maxReadSize > 0) {
                if (readSize > maxReadSize) {
                    readSize = maxReadSize;
}
                buffer.append(rawBuffer, 0, readSize);
                maxReadSize -= readSize;
            }
            return buffer.toString();
        }

    }
}
