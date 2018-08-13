package cn.bingoogolapple.photopicker.adapters;

import android.databinding.BindingAdapter;

import java.util.ArrayList;

import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

public class BGASortableNinePhotoLayoutAdapter {

    @BindingAdapter({"bga_snpl_delegate"})
    public static void setDelegate(BGASortableNinePhotoLayout sortableNinePhotoLayout, BGASortableNinePhotoLayout.Delegate delegate) {
        sortableNinePhotoLayout.setDelegate(delegate);
    }

    @BindingAdapter({"bga_snpl_data"})
    public static void setData(BGASortableNinePhotoLayout sortableNinePhotoLayout, ArrayList<String> data) {
        sortableNinePhotoLayout.setData(data);
    }

    @BindingAdapter({"bga_snpl_editable"})
    public static void setData(BGASortableNinePhotoLayout sortableNinePhotoLayout, boolean editable) {
        sortableNinePhotoLayout.setEditable(editable);
    }
}
