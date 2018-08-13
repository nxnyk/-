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
 * Created by yinyu on 2018/6/1.
 */

public class mineadapter extends ArrayAdapter {
    private final int Resoueceid;
    Context mcontext;
    LayoutInflater layoutInflater;

    public mineadapter(Context context, int resoueceid, List<mine> objects) {
        super(context, resoueceid, objects);
        this.Resoueceid = resoueceid;
        mcontext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int p = position;
        View view = null;
        mine mess = (mine) getItem(position);
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.mineitem, null);
        } else {
            view = convertView;

        }
        TextView minename = (TextView) view.findViewById(R.id.minename);
        TextView mineprice = (TextView) view.findViewById(R.id.mineprice);
        TextView minefabu = (TextView) view.findViewById(R.id.minefabu);
        minefabu.setText("  我发布了:");
        minename.setText("  " + mess.getTitle());
        mineprice.setText("  价格为:  " + mess.getPrice());
        return view;
    }
}
