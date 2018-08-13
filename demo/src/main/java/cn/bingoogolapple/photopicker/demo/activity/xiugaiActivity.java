package cn.bingoogolapple.photopicker.demo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
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
                            Class.forName("com.mysql.jdbc.Driver"); // 加载MYSQL JDBC驱动程序
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            DriverManager.getLoginTimeout();
                            Connection connect = DriverManager.getConnection(
                                    "jdbc:mysql://47.106.83.240:3306/youershou", "root",
                                    "960528");
                            // 连接URL为 jdbc:mysql//服务器地址/数据库名 ，后面的2个参数分别是登陆用户名和密码
                            System.out.println("Success connect Mysql server!");
                            // 查询用户名密码
                            String sql = "delete from wuping where wupingid ="+mywupingActivity.mineduixiang;
                            PreparedStatement prestStatement = connect.prepareStatement(sql);
                            prestStatement.executeUpdate();
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
                }).start();
            }
        });
    }
    Runnable runnable = new Runnable() {
        private Connection connection = null;
        @Override
        public void run() {
            try {
                Class.forName("com.mysql.jdbc.Driver"); // 加载MYSQL JDBC驱动程序
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                DriverManager.getLoginTimeout();
                Connection connect = DriverManager.getConnection(
                        "jdbc:mysql://47.106.83.240:3306/youershou", "root",
                        "960528");
                // 连接URL为 jdbc:mysql//服务器地址/数据库名 ，后面的2个参数分别是登陆用户名和密码
                System.out.println("Success connect Mysql server!");
                // 查询用户名密码
                String sql = "select * from wuping where wupingid =?";
                PreparedStatement prestStatement = connect.prepareStatement(sql);
                prestStatement.setString(1, mywupingActivity.mineduixiang);

                rs = prestStatement.executeQuery();
                while (rs.next()) {
                        minetitle.setText(rs.getString("wupingname"));
                        minemiaoshu.setText(rs.getString("miaoshu"));
                        mineeditprice.setText(rs.getString("jiage"));
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
