package com.tistory.starcue.songgainb;

import android.os.Parcel;
import android.os.Parcelable;

public class SongModel implements Parcelable {
    private int _index;
    private String _url;
    private String _name;
    private String _time;
    private byte[] _img;
    private String position;

    public SongModel() {
    }

    public SongModel(int _index, String _url, String _name, String _time, byte[] _img, String position) {
        this._index = _index;
        this._url = _url;
        this._name = _name;
        this._time = _time;
        this._img = _img;
        this.position = position;
    }

    protected SongModel(Parcel in) {
        _index = in.readInt();
        _url = in.readString();
        _name = in.readString();
        _time = in.readString();
        _img = in.createByteArray();
        position = in.readString();
    }

    public static final Creator<SongModel> CREATOR = new Creator<SongModel>() {
        @Override
        public SongModel createFromParcel(Parcel in) {
            return new SongModel(in);
        }

        @Override
        public SongModel[] newArray(int size) {
            return new SongModel[size];
        }
    };

    public int get_index() {
        return _index;
    }

    public void set_index(int _index) {
        this._index = _index;
    }

    public String get_url() {
        return _url;
    }

    public void set_url(String _url) {
        this._url = _url;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_time() {
        return _time;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public byte[] get_img() {
        return _img;
    }

    public void set_img(byte[] _img) {
        this._img = _img;
    }

    public String getposition() {
        return position;
    }

    public void setposition(String position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._index);
        dest.writeString(this._url);
        dest.writeString(this._name);
        dest.writeString(this._time);
        dest.writeByteArray(_img);
        dest.writeString(this.position);
    }

}
