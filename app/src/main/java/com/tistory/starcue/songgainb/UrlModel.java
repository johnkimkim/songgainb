package com.tistory.starcue.songgainb;

import android.os.Parcel;
import android.os.Parcelable;

public class UrlModel implements Parcelable {
    private String _url;


    public UrlModel() {
    }

    public UrlModel(String _url) {
        this._url = _url;

    }

    protected UrlModel(Parcel in) {
        _url = in.readString();

    }

    public static final Creator<UrlModel> CREATOR = new Creator<UrlModel>() {
        @Override
        public UrlModel createFromParcel(Parcel in) {
            return new UrlModel(in);
        }

        @Override
        public UrlModel[] newArray(int size) {
            return new UrlModel[size];
        }
    };

    public String get_url() {
        return _url;
    }

    public void set_url(String _url) {
        this._url = _url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._url);
    }
}
