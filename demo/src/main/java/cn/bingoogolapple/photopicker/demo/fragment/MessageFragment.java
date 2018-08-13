package cn.bingoogolapple.photopicker.demo.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.demo.Config.DButil;
import cn.bingoogolapple.photopicker.demo.Config.message;
import cn.bingoogolapple.photopicker.demo.Config.messageadapter;
import cn.bingoogolapple.photopicker.demo.Config.mine;
import cn.bingoogolapple.photopicker.demo.Config.mineadapter;
import cn.bingoogolapple.photopicker.demo.R;
import cn.bingoogolapple.photopicker.demo.activity.huifuActivity;
import cn.bingoogolapple.photopicker.demo.activity.RegActivity;

public class MessageFragment extends Fragment {
    ResultSet rs=null;
    public List<message> liuyan=new ArrayList<message>();
    public ListView listView;
    Connection connect=null;
    PreparedStatement prestStatement;
    public static String huifuduixiang;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_message, container, false);

            new Thread(runnable).start();
            //initFeed();
            //createDx();

        listView = (ListView)view.findViewById(R.id.message);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                huifuduixiang=liuyan.get(position).getLyz();
                Intent i=new Intent(MessageFragment.this.getContext(),huifuActivity.class);
                startActivity(i);
            }
        });

        return view;
    }
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<message> liuyan2=new ArrayList<message>();
            Bundle bundle=msg.getData();
            liuyan2=bundle.getParcelableArrayList("liuyan");
            messageadapter adapter = new messageadapter(MessageFragment.this.getContext(),
                    R.layout.messageitem, liuyan2);
            listView.setAdapter(adapter);
        }
    };
    Runnable runnable = new Runnable() {
        private Connection connection = null;
        @Override
        public void run() {
            liuyan.clear();
            try {
                DButil dButil=new DButil();
                connect=dButil.GetConnection();
                String sql = "select * from liuyan where recive =?";
                PreparedStatement prestStatement = connect.prepareStatement(sql);
                prestStatement.setString(1, RegActivity.username);

                rs = prestStatement.executeQuery();
                while (rs.next()) {
                    message mess = new message(rs.getString("liuyanzhe"),
                            rs.getString("neirong"));
                    liuyan.add(mess);
                    Message message=handler.obtainMessage();
                    Bundle bundle=new Bundle();
                    bundle.putParcelableArrayList("liuyan", (ArrayList<? extends Parcelable>) liuyan);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
                connect.close();
            } catch (Exception ex) {
                System.out.print("get data error!");
                ex.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (prestStatement != null) {
                        prestStatement.close();
                    }
                    if (connect != null) {
                        connect.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    };

}
