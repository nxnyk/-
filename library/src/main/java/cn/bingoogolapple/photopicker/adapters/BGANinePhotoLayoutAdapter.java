package cn.bingoogolapple.photopicker.adapters;

import android.databinding.BindingAdapter;

import java.util.ArrayList;

import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

public class BGANinePhotoLayoutAdapter {

    @BindingAdapter({"bga_npl_delegate"})
    public static void setDelegate(BGANinePhotoLayout ninePhotoLayout, BGANinePhotoLayout.Delegate delegate) {
        ninePhotoLayout.setDelegate(delegate);
    }

    @BindingAdapter({"bga_npl_data"})
    public static void setData(BGANinePhotoLayout ninePhotoLayout, ArrayList<String> data) {
        ninePhotoLayout.setData(data);
    }
}
