package cn.bingoogolapple.photopicker.demo.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.bingoogolapple.photopicker.demo.Config.DButil;
import cn.bingoogolapple.photopicker.demo.Config.mine;
import cn.bingoogolapple.photopicker.demo.R;

public class xiugaiActivity extends AppCompatActivity {
    private EditText minetitle;
    private EditText minemiaoshu;
    private EditText mineeditprice;
    private Button minexiugai;
    private Button mineshanchu;
    ResultSet rs=null;
    PreparedStatement prestStatement;
    Connection connect=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiugai);

        minetitle=findViewById(R.id.minetitle);
        minemiaoshu=findViewById(R.id.minemiaoshu);
        mineeditprice=findViewById(R.id.mineeditprice);
        minexiugai=findViewById(R.id.minexiugai);
        mineshanchu=findViewById(R.id.mineshanchu);
        new Thread(runnable).start();
        minexiugai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       try{
                           DButil dButil=new DButil();
                           connect=dButil.GetConnection();
                            String sql = "update wuping set wupingname=?,jiage=?,miaoshu=? where wupingid ="+mywupingActivity.mineduixiang;
                            PreparedStatement prestStatement = connect.prepareStatement(sql);
                            prestStatement.setString(1,minetitle.getText().toString());
                            prestStatement.setString(2,mineeditprice.getText().toString());
                            prestStatement.setString(3,minemiaoshu.getText().toString());
                            prestStatement.executeUpdate();
                            Looper.prepare();
                            new AlertDialog.Builder(xiugaiActivity.this).setTitle("提示:")
                                    .setMessage("修改成功！")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                            Intent i=new Intent(xiugaiActivity.this,mywupingActivity.class);
                                            startActivity(i);
                                        }
                                    }).show();
                            Looper.loop();
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
                }).start();
            }
        });
        mineshanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DButil dButil =new DButil();
                            connect=dButil.GetConnection();
                            String sql = "delete from wuping where wupingid ="+mywupingActivity.mineduixiang;
                            PreparedStatement prestStatement = connect.prepareStatement(sql);
                            int result=prestStatement.executeUpdate();
                            if(result>0)
                            {
                                Looper.prepare();
                                new AlertDialog.Builder(xiugaiActivity.this).setTitle("提示:")
                                        .setMessage("删除成功！")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        }).show();
                                Looper.loop();
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
                }).start();
            }
        });
    }
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            String wupingmane=bundle.getString("wupingname");
            String miaoshu=bundle.getString("miaoshu");
            String jiage=bundle.getString("jiage");
            minetitle.setText(wupingmane);
            minemiaoshu.setText(miaoshu);
            mineeditprice.setText(jiage);
        }
    };
    Runnable runnable = new Runnable() {
        private Connection connection = null;
        @Override
        public void run() {
            try {
                DButil dButil=new DButil();
                connect=dButil.GetConnection();
                String sql = "select * from wuping where wupingid =?";
                PreparedStatement prestStatement = connect.prepareStatement(sql);
                prestStatement.setString(1, mywupingActivity.mineduixiang);
                rs = prestStatement.executeQuery();
                while (rs.next()) {
                        Message message=handler.obtainMessage();
                        Bundle bundle=new Bundle();
                        bundle.putString("wupingname",rs.getString("wupingname"));
                        bundle.putString("miaoshu",rs.getString("miaoshu"));
                        bundle.putString("jiage",rs.getString("jiage"));
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
