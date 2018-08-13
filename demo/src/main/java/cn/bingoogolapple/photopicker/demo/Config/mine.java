package cn.bingoogolapple.photopicker.demo.Config;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yinyu on 2018/6/8.
 */

@SuppressLint("ParcelCreator")
public class mine implements Parcelable {
    public String user;
    public String title;
    public String price;

    public mine(String user, String title, String price) {
        this.price = price;
        this.title = title;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.price);
        dest.writeString(this.title);
        dest.writeString(this.user);
    }
}
