package cn.bingoogolapple.photopicker.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.startsmake.mainnavigatetabbar.widget.MainNavigateTabBar;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.demo.Config.shuju;
import cn.bingoogolapple.photopicker.demo.R;
import cn.bingoogolapple.photopicker.demo.fragment.SearchFragment;
import cn.bingoogolapple.photopicker.demo.fragment.HomeFragment;
import cn.bingoogolapple.photopicker.demo.fragment.MessageFragment;
import cn.bingoogolapple.photopicker.demo.fragment.PersonFragment;
import cn.bingoogolapple.photopicker.demo.model.Moment;

public class MainActivity extends AppCompatActivity {
    private static final int RC_ADD_MOMENT = 1;
    private static final String TAG_PAGE_HOME = "首页";
    private static final String TAG_PAGE_CITY = "搜索";
    private static final String TAG_PAGE_PUBLISH = "发布";
    private static final String TAG_PAGE_MESSAGE = "消息";
    private static final String TAG_PAGE_PERSON = "我的";

    public Connection conn2;
    private List<shuju> jxsj=new ArrayList<>();
    public static List<Moment> moments = new ArrayList<>();
    private MainNavigateTabBar mNavigateTabBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        mNavigateTabBar = (MainNavigateTabBar) findViewById(R.id.mainTabBar);

        mNavigateTabBar.onRestoreInstanceState(savedInstanceState);

            mNavigateTabBar.addTab(HomeFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.comui_tab_home, R.mipmap.comui_tab_home_selected, TAG_PAGE_HOME));
            mNavigateTabBar.addTab(SearchFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.search1, R.mipmap.search2, TAG_PAGE_CITY));
            mNavigateTabBar.addTab(null, new MainNavigateTabBar.TabParam(0, 0, TAG_PAGE_PUBLISH));
            mNavigateTabBar.addTab(MessageFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.comui_tab_message, R.mipmap.comui_tab_message_selected, TAG_PAGE_MESSAGE));
            mNavigateTabBar.addTab(PersonFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.comui_tab_person, R.mipmap.comui_tab_person_selected, TAG_PAGE_PERSON));


    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigateTabBar.onSaveInstanceState(outState);
    }


    public void onClickPublish(View v) {
        startActivityForResult(new Intent(this, MomentAddActivity.class), RC_ADD_MOMENT);
    }
}
