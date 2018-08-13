package cn.bingoogolapple.photopicker.demo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import cn.bingoogolapple.photopicker.demo.Config.mine;
import cn.bingoogolapple.photopicker.demo.Config.mineadapter;
import cn.bingoogolapple.photopicker.demo.R;

public class mywupingActivity extends AppCompatActivity {
    ResultSet rs=null;
    public List<mine> mines=new ArrayList<mine>();
    public ListView listView;
    Connection connect=null;
    PreparedStatement prestStatement;
    public static String mineduixiang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("我的发布：");
        setContentView(R.layout.activity_mywuping);

        new Thread(runnable).start();

        listView = (ListView)findViewById(R.id.mywuping);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mineduixiang=mines.get(position).getUser();
                Intent i=new Intent(getApplicationContext(),xiugaiActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<mine> mines2=new ArrayList<mine>();
            Bundle bundle=msg.getData();
            mines2=bundle.getParcelableArrayList("message");
            mineadapter adapter = new mineadapter(getApplicationContext(), R.layout.mineitem, mines2);
            listView.setAdapter(adapter);
        }
    };
    Runnable runnable = new Runnable() {
        private Connection connection = null;
        @Override
        public void run() {
            mines.clear();
            try {

                DButil dButil=new DButil();
                Connection connect=dButil.GetConnection();
                String sql = "select * from wuping where fabuzhe =?";
                PreparedStatement prestStatement = connect.prepareStatement(sql);
                prestStatement.setString(1, RegActivity.username);

                rs = prestStatement.executeQuery();
                while (rs.next()) {
                    mine mess = new mine(rs.getString("wupingid"),rs.getString("wupingname"),
                            rs.getString("jiage"));
                    mines.add(mess);
                    Message message=handler.obtainMessage();
                    Bundle bundle=new Bundle();
                    bundle.putParcelableArrayList("message", (ArrayList<? extends Parcelable>) mines);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
                dButil.Closecon(connect);
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
