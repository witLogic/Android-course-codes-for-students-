package com.example.muthuveerappans.course3.NetworkHelpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by muthuveerappans on 7/18/17.
 */

public class NetworkFragment extends Fragment {

    public static final String TAG = "NetworkFragment";

    private static final String DATA_OBJECT = "data_object";

    private DownloadCallback<Result> mCallback;
    private DownloadTask mDownloadTask;
    private NetworkObject networkObject;

    public static NetworkFragment getInstance(FragmentManager fragmentManager, NetworkObject networkObject) {
        NetworkFragment networkFragment = new NetworkFragment();
        Bundle args = new Bundle();
        args.putParcelable(DATA_OBJECT, networkObject);
        networkFragment.setArguments(args);
        // add the invisible fragment.
        fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        fragmentManager.executePendingTransactions();
        return networkFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this Fragment across configuration changes in the host Activity.
        setRetainInstance(true);

        networkObject = getArguments().getParcelable(DATA_OBJECT);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Host Activity will handle callbacks from task.
        mCallback = (DownloadCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity to avoid memory leak.
        mCallback = null;
    }

    @Override
    public void onDestroy() {
        // Cancel task when Fragment is destroyed.
        cancelDownload();
        super.onDestroy();
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
                // Open communications link (network traffic occurs here).
                connection.connect();

                publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);

                if (networkObject.getHttpMethod().equals("POST")) {
                    // Send POST data.
                    publishProgress(DownloadCallback.Progress.DOWNLOADING);
                    DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
                    printout.writeBytes(URLEncoder.encode(networkObject.getPostData(), "UTF-8"));
                    printout.flush();
                    printout.close();
                }

                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    result = new Result(new Exception("HTTP error code: " + responseCode));
                } else {
                    // Retrieve the response body as an InputStream.
                    stream = connection.getInputStream();
                    publishProgress(DownloadCallback.Progress.DOWNLOADING);
                    if (stream != null) {
                        // Converts Stream to String with max length of contentLength.
                        long contentLength = Long.parseLong(connection.getHeaderField("Content-Length"));
                        result = new Result(readStream(stream, (int) contentLength));
                    }
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
