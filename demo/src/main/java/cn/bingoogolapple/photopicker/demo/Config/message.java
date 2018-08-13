package cn.bingoogolapple.photopicker.demo.Config;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yinyu on 2018/6/3.
 */
@SuppressLint("ParcelCreator")
public class message implements Parcelable{
    public String lyz;
    public String lynr;
    public message(String lyz,String lynr){
    this.lyz=lyz;
    this.lynr=lynr;
}
    public String getLyz()
    {
        return lyz;
    }
    public String getLynr()
    {
        return lynr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lynr);
        dest.writeString(this.lyz);

    }
}
