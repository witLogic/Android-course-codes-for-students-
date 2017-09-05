package com.course4.posthttprequestsample.Internet;

/**
 * Wrapper class that serves as a union of a result value and an exception. When the download
 * task has completed, either the result value or exception can be a non-null value.
 * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
 */
public class Result {
    public String mResultValue;
    public Exception mException;
    public String url;

    public Result(String resultValue, String url) {
        mResultValue = resultValue;
        this.url = url;
    }

    public Result(Exception exception) {
        mException = exception;
    }
}