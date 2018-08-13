package cn.bingoogolapple.photopicker.demo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cn.bingoogolapple.photopicker.demo.Config.DButil;
import cn.bingoogolapple.photopicker.demo.R;
import cn.bingoogolapple.photopicker.demo.fragment.MessageFragment;

public class huifuActivity extends AppCompatActivity {
    private EditText huifu;
    private Button bthuifu;
    Connection conn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huifu);
        huifu=(EditText)findViewById(R.id.huifu);
        bthuifu=(Button)findViewById(R.id.btn_huifu);
        bthuifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DButil dButil=new DButil();
                            conn2=dButil.GetConnection();
                            String upsql="insert into liuyan values (?,?,?,?)";
                            PreparedStatement ps=conn2.prepareStatement(upsql);
                            ps.setString(1,null);
                            ps.setString(2, MessageFragment.huifuduixiang);
                            ps.setString(3,huifu.getText().toString());
                            ps.setString(4, RegActivity.username);
                            ps.execute();

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }catch (SQLException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }).start();
                new AlertDialog.Builder(huifuActivity.this).setTitle("提示:")
                        .setMessage("回复成功！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
            }
        });

    }
}
