package cn.bingoogolapple.photopicker.demo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.demo.Config.DButil;
import cn.bingoogolapple.photopicker.demo.Config.shuju;
import cn.bingoogolapple.photopicker.demo.R;
import cn.bingoogolapple.photopicker.demo.model.Moment;

public class LoginActivity extends AppCompatActivity {
    Connection conn;
    private EditText log1;
    private EditText log2;
    private Button login;
    public Connection conn2;
    private List<shuju> jxsj = new ArrayList<>();
    public static List<Moment> moments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denglui);
        log1 = findViewById(R.id.lgusername);
        log2 = findViewById(R.id.lgpassword);
        login = findViewById(R.id.blogin);
        login.setOnClickListener(v -> new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DButil dButil=new DButil();
                    conn=dButil.GetConnection();
                    String sql1 = "select * from userinfo where username = ? and password = ?";
                    if(conn!=null)
                    {
                        PreparedStatement ps = conn.prepareStatement(sql1);
                        ps.setString(1, log1.getText().toString());
                        ps.setString(2, log2.getText().toString());
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            Looper.prepare();
                            RegActivity.username = log1.getText().toString();
                            Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_LONG);
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                            Looper.loop();

                        } else {
                            Looper.prepare();
                            new AlertDialog.Builder(LoginActivity.this).setTitle("提示:")
                                    .setMessage("用户名或密码错误！")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                            Looper.loop();
                        }
                    }


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start());
    }

//    public void getdata() {
//        Thread r = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    DButil dButil=new DButil();
//                    conn2=dButil.GetConnection();
//                    String upsql = "select * from wuping order by wupingid desc";
//                    Statement S = conn2.createStatement();
//                    ResultSet rs = S.executeQuery(upsql);
//                    while (rs.next()) {
//                        shuju s = new shuju();
//                        s.setWpid(rs.getString("wupingid"));
//                        s.setWpjiage(rs.getString("jiage"));
//                        s.setWpms(rs.getString("miaoshu"));
//                        s.setWpname(rs.getString("wupingname"));
//                        s.setWpurl(rs.getString("tupianurl"));
//                        s.setWpfabuzhe(rs.getString("fabuzhe"));
//                        jxsj.add(s);
//
//                    }
//                    if (!rs.next()) {
//
//                        for (int i = 0; i < jxsj.size(); i++) {
//                            ArrayList<String> ls1 = new ArrayList<>();
//                            String s = jxsj.get(i).getwpurl();
//                            String[] ls = s.split("images/");
//                            String[] sp = new String[ls.length];
//                            for (int j = 1; j < ls.length; j++) {
//                                sp[j] = "http://47.106.83.240/images/" + ls[j];
//                                ls1.add(sp[j]);
//                            }
//                            Moment m = new Moment(jxsj.get(i).getwpname(), jxsj.get(i).getwpjiage(), jxsj.get(i).getWpid(), jxsj.get(i).getWpfabuzhe(), jxsj.get(i).getwpms(), ls1);
//                            moments.add(m);
//                        }
//
//
//                    }
//
//
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        r.start();
//    }

}
