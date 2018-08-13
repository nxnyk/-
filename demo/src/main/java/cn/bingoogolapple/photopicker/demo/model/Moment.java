package cn.bingoogolapple.photopicker.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Moment implements Parcelable {
    public String content;
    public String price;
    public String ID;
    public String wupingid;
    public String wupingfabuzhe;
    public ArrayList<String> photos;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.price);
        dest.writeString(this.content);
        dest.writeStringList(this.photos);
        dest.writeString(this.ID);
        dest.writeString(this.wupingfabuzhe);
        dest.writeString(this.wupingid);
    }

    public Moment() {
    }

    public Moment(String content,String price,String id,String wupingfabuzhe,String wupingid,ArrayList<String> photos) {
        this.content = content;
        this.photos = photos;
        this.price=price;
        this.ID=id;
        this.wupingfabuzhe=wupingfabuzhe;
        this.wupingid=wupingid;
    }

    protected Moment(Parcel in) {
        this.content = in.readString();
        this.price=in.readString();
        this.photos = in.createStringArrayList();
        this.ID=in.readString();
        this.wupingid=in.readString();
        this.wupingfabuzhe=in.readString();
    }

    public static final Parcelable.Creator<Moment> CREATOR = new Parcelable.Creator<Moment>() {
        @Override
        public Moment createFromParcel(Parcel source) {
            return new Moment(source);
        }

        @Override
        public Moment[] newArray(int size) {
            return new Moment[size];
        }
    };
}