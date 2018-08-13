package cn.bingoogolapple.photopicker.demo.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.bingoogolapple.photopicker.demo.Config.DButil;
import cn.bingoogolapple.photopicker.demo.Config.EditLayout;
import cn.bingoogolapple.photopicker.demo.R;

public class personinfoActivity extends AppCompatActivity {
    ResultSet rs=null;
    PreparedStatement prestStatement;
    Connection connect=null;
    private EditLayout e1;
    private EditLayout e2;
    private EditLayout e3;
    private EditLayout e4;
    private EditLayout e5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personinfo);
        e1=findViewById(R.id.et_layout1);
        e2=findViewById(R.id.et_layout2);
        e3=findViewById(R.id.et_layout3);
        e4=findViewById(R.id.et_layout4);
        e5=findViewById(R.id.et_layout5);
        Thread thread=new Thread(runnable);
        thread.start();


}
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            String rec[]=new String[3];
            rec=bundle.getStringArray("Message");
            e1.setContentTxt(rec[0]);
            e4.setContentTxt(rec[1]);
            e5.setContentTxt(rec[2]);

        }
    };
   Runnable runnable=new Runnable() {
        private Connection con = null;
        @Override
        public void run() {
            try {
                DButil dButil=new DButil();
                con=dButil.GetConnection();
                // 查询用户名密码
                String sql = "select * from userinfo where username =?";
                PreparedStatement prestStatement = con.prepareStatement(sql);
                prestStatement.setString(1, RegActivity.username);

                rs = prestStatement.executeQuery();
                while (rs.next()) {
                    String name=(rs.getString("username"));
                    String phone=(rs.getString("phone"));
                    String email=(rs.getString("email"));
                    String messgae[]=new String[3];
                    messgae[0]=name;
                    messgae[1]=phone;
                    messgae[2]=email;
                    Message message1=handler.obtainMessage();
                    Bundle bundle=new Bundle();
                    bundle.putStringArray("Message",messgae);
                    message1.setData(bundle);
                    handler.sendMessage(message1);
                }
                dButil.Closecon(con);
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
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    };
}
