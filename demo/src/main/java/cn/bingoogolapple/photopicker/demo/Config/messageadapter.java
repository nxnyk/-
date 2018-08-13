package cn.bingoogolapple.photopicker.demo.Config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cn.bingoogolapple.photopicker.demo.R;

/**
 * Created by yinyu on 2018/6/3.
 */

public class messageadapter extends ArrayAdapter {
    private final int Resoueceid;
    Context mcontext;
    LayoutInflater layoutInflater;

    public messageadapter(Context context, int resoueceid, List<message> objects) {
        super(context, resoueceid, objects);
        this.Resoueceid = resoueceid;
        mcontext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int p = position;
        View view = null;
        message mess = (message) getItem(position);
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.messageitem, null);
        } else {
            view = convertView;

        }
        TextView messfbz = (TextView) view.findViewById(R.id.messagefrom);
        TextView messnr = (TextView) view.findViewById(R.id.messagenr);

        messfbz.setText("用户" + mess.getLyz() + "给您留言:");
        messnr.setText(mess.getLynr());
        return view;
    }
}

