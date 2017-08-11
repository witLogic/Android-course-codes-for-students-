package com.example.muthuveerappans.networkinghelpers.Internet;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by muthuveerappans on 7/18/17.
 */

public class NetworkObject implements Parcelable {

    public static enum HTTP_METHOD {
        GET, POST
    }

    private String URL;
    private HTTP_METHOD httpMethod;
    private String postData;

    public NetworkObject(String URL, HTTP_METHOD httpMethod) {
        this.URL = URL;
        this.httpMethod = httpMethod;
    }

    public NetworkObject(String URL, HTTP_METHOD httpMethod, String postData) {
        this(URL, httpMethod);
        this.postData = postData;
    }

    public String getURL() {
        return URL;
    }

    public String getHttpMethod() {
        switch (httpMethod) {
            case GET:
                return "GET";
            case POST:
                return "POST";
        }

        return "N/A";
    }

    public String getPostData() {
        return postData;
    }

    protected NetworkObject(Parcel in) {
        URL = in.readString();
        postData = in.readString();
    }

    public static final Creator<NetworkObject> CREATOR = new Creator<NetworkObject>() {
        @Override
        public NetworkObject createFromParcel(Parcel in) {
            return new NetworkObject(in);
        }

        @Override
        public NetworkObject[] newArray(int size) {
            return new NetworkObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(URL);
        dest.writeString(postData);
    }
}
