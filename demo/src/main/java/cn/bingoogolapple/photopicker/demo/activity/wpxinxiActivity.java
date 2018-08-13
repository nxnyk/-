package cn.bingoogolapple.photopicker.demo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.demo.Config.DButil;
import cn.bingoogolapple.photopicker.demo.Config.shuju;
import cn.bingoogolapple.photopicker.demo.R;
import cn.bingoogolapple.photopicker.demo.fragment.HomeFragment;
import ezy.ui.view.BannerView;

public class wpxinxiActivity extends AppCompatActivity {
    private TextView bt;
    private TextView ms;
    private TextView fbz;
    private TextView jg;
    private Button ly;
    private EditText edly;
    private List<shuju> jxsj=new ArrayList<>();
    private String[] imgurl;
    List<BannerViewFactory.BannerItem> list = new ArrayList<>();
    Connection conn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wpxinxi);
        bt=findViewById(R.id.xxbt);
        ms=findViewById(R.id.xxms);
        fbz =findViewById(R.id.xxfbz);
        jg=findViewById(R.id.xxjg);
        ly=findViewById(R.id.tijiao);
        edly=findViewById(R.id.edly);
        bt.setText("物品标题:"+ HomeFragment.chuandiname);
        ms.setText("物品详情:"+HomeFragment.chuandimiaoshu);
        fbz.setText("发布者:"+HomeFragment.chuandifbz);
        jg.setText("价格为:¥"+HomeFragment.chuandijiage);
        for (int i = 0; i < HomeFragment.chuandiurl.length; i++) {
            BannerViewFactory.BannerItem item = new BannerViewFactory.BannerItem();
            item.image = HomeFragment.chuandiurl[i];

            list.add(item);
        }
        final BannerView banner1 = (BannerView) findViewById(R.id.zhanshi11);
        banner1.setViewFactory(new BannerViewFactory());

        banner1.setDataList(list);
        banner1.start();

        ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edly.getText().toString().equals(""))
                {
                    new AlertDialog.Builder(wpxinxiActivity.this).setTitle("提示:")
                            .setMessage("留言失败！留言不能为空！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }
                else
                    {
                        Thread r=new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    DButil dButil=new DButil();
                                    conn2= dButil.GetConnection();
                                    String upsql="insert into liuyan values (?,?,?,?)";
                                    PreparedStatement ps=conn2.prepareStatement(upsql);
                                    ps.setString(1,null);
                                    ps.setString(2,HomeFragment.chuandifbz);
                                    ps.setString(3,edly.getText().toString());
                                    ps.setString(4,RegActivity.username);
                                    int result=ps.executeUpdate();
                                    if(result>0){
                                        Looper.prepare();
                                        new AlertDialog.Builder(wpxinxiActivity.this).setTitle("提示:")
                                                .setMessage("留言成功！")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                    }
                                                }).show();
                                        Looper.loop();
                                    }
                                   else {
                                        Looper.prepare();
                                        new AlertDialog.Builder(wpxinxiActivity.this).setTitle("提示:")
                                                .setMessage("留言失败！")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                }).show();
                                        Looper.loop();
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Looper.prepare();
                                    new AlertDialog.Builder(wpxinxiActivity.this).setTitle("提示:")
                                            .setMessage("留言失败！")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).show();
                                    Looper.loop();
                                }
                            }
                        });

                        r.start();
                    }


            }

        });





    }
    public static class BannerViewFactory implements BannerView.ViewFactory<BannerViewFactory.BannerItem> {
        @Override
        public View create(BannerItem item, int position, ViewGroup container) {
            ImageView iv = new ImageView(container.getContext());
            RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA);
            Glide.with(container.getContext().getApplicationContext()).load(item.image).apply(options).into(iv);
            return iv;
        }

        public static class BannerItem {
            public String image;
            public String title;

        }
    }
}