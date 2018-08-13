package cn.bingoogolapple.photopicker.demo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cn.bingoogolapple.photopicker.demo.Config.DButil;
import cn.bingoogolapple.photopicker.demo.R;

public class RegActivity extends AppCompatActivity {
    public static String username=null;
    private Button REG;
    private EditText ed1;
    private EditText ed2;
    private EditText ed3;
    private EditText ed4;
    private EditText ed5;
    Connection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ed1=findViewById(R.id.ed1);
        ed2=findViewById(R.id.ed2);
        ed3=findViewById(R.id.ed3);
        ed4=findViewById(R.id.ed4);
        ed5=findViewById(R.id.ed5);
        REG=(Button)findViewById(R.id.reg);
        REG.setOnClickListener(v -> new Thread(() -> {
            try {

                DButil dButil=new DButil();
                conn=dButil.GetConnection();
                String sql1="select * from userinfo where username = '"+ed1.getText().toString()+"'";
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(sql1);
                if(rs.next())
                {
                    Looper.prepare();
                    new AlertDialog.Builder(RegActivity.this).setTitle("提示:")
                    .setMessage("用户名已经存在！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                    }).show();
                    Looper.loop();
                }
                else if(ed2.getText().toString().equals(ed3.getText().toString()))
                {
                    Looper.prepare();
                    String sql2="insert into userinfo values (?,?,?,?,?)";
                    PreparedStatement ps1=conn.prepareStatement(sql2);
                    ps1.setString(1,null);
                    ps1.setString(2,ed1.getText().toString());
                    ps1.setString(3,ed2.getText().toString());
                    ps1.setString(4,ed4.getText().toString());
                    ps1.setString(5,ed5.getText().toString());
                    ps1.execute();
                    username=ed1.getText().toString();
                    new AlertDialog.Builder(RegActivity.this).setTitle("提示:")
                    .setMessage("注册成功并自动登录！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);

                        try {
                            dButil.Closecon(conn);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        finish();}
                    }).show();
                    Looper.loop();
                }
                else
                    {
                        Looper.prepare();
                        new AlertDialog.Builder(RegActivity.this).setTitle("提示:")
                        .setMessage("两次输入的密码不一致！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                        }).show();
                        Looper.loop();
                    }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start());
    }

}
