package cn.bingoogolapple.photopicker.demo.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyt.searchedittext.SearchEditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;
import cn.bingoogolapple.photopicker.demo.Config.DButil;
import cn.bingoogolapple.photopicker.demo.Config.shuju;
import cn.bingoogolapple.photopicker.demo.R;
import cn.bingoogolapple.photopicker.demo.model.Moment;
import cn.bingoogolapple.photopicker.imageloader.BGARVOnScrollListener;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

public class SearchFragment extends Fragment {
    private SearchEditText searchEditText;
    final String home_IP = "47.106.83.240:3306";
    final String sqlurl = "jdbc:mysql://" + home_IP + "/youershou";
    final String user = "root";
    final String password1 = "960528";
    private RecyclerView mMomentRv;
    private HomeAdapter mMomentAdapter;
    Connection conn2;
    Activity main1Activity;
    private List<shuju> jxsj=new ArrayList<>();
    @Nullable
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        main1Activity=getActivity();
    }

    public ArrayList<String> jxurl(String url)
    {
        ArrayList<String> arrayList=new ArrayList<>();
        String str[]=url.split("/img");
        String str2[]=new String[str.length];
        for(int i=1;i<str.length;i++)
        {
            str2[i]="http://192.168.1.100/img"+str[i];
            arrayList.add(str2[i]);
        }
        return arrayList;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_city, container, false);
        searchEditText=(SearchEditText)view.findViewById(R.id.searchEditText);
        mMomentRv = (RecyclerView) view.findViewById(R.id.searchshow);
        mMomentRv.addOnScrollListener(new BGARVOnScrollListener(main1Activity));
        mMomentAdapter = new HomeAdapter(mMomentRv);
        mMomentRv.setAdapter(mMomentAdapter);
        mMomentRv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        searchEditText.setOnSearchClickListener(view1 -> {
            String keyword=searchEditText.getText().toString();
            List<Moment> moments = new ArrayList<>();
            if(!TextUtils.isEmpty(keyword))
            {
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            DButil dButil=new DButil();
                            conn2=dButil.GetConnection();
                            String upsql="select * from wuping where wupingname like '%"+keyword+"%'";
                            Statement S=conn2.createStatement();
                            ResultSet rs=S.executeQuery(upsql);
                            while (rs.next())
                            {
                                shuju s=new shuju();
                                s.setWpid(rs.getString("wupingid"));
                                s.setWpjiage(rs.getString("jiage"));
                                s.setWpms(rs.getString("miaoshu"));
                                s.setWpname(rs.getString("wupingname"));
                                s.setWpurl(rs.getString("tupianurl"));
                                s.setWpfabuzhe(rs.getString("fabuzhe"));
                                Moment m=new Moment(s.getwpname(),s.getwpjiage(),s.getWpid(),s.getWpfabuzhe(),s.getwpms(),jxurl(s.getwpurl()));
                                moments.add(m);
                                Message message=new Message();
                                Bundle bundle=new Bundle();
                                bundle.putParcelableArrayList("moment", (ArrayList<? extends Parcelable>) moments);
                                message.setData(bundle);
                                handler.sendMessage(message);
                            }

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
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
            List<Moment> momentsrec = new ArrayList<>();
            Bundle bundle=msg.getData();
            momentsrec=bundle.getParcelableArrayList("moment");
            mMomentAdapter.setData(momentsrec);

        }
    };
    public class HomeAdapter extends BGARecyclerViewAdapter<Moment> {

        public HomeAdapter(RecyclerView recyclerView)
        {
            super(recyclerView, R.layout.item_moment);
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, Moment moment) {
            if (TextUtils.isEmpty(moment.content)) {
                helper.setVisibility(R.id.tv_item_moment_content, View.GONE);
            } else {
                helper.setVisibility(R.id.tv_item_moment_content, View.VISIBLE);
                helper.setText(R.id.tv_item_moment_content,"发布了: "+moment.content);
                helper.setText(R.id.textViewprice,"¥"+moment.price);
                helper.setText(R.id.item_username,moment.wupingfabuzhe);
                helper.setText(R.id.wupingid,moment.wupingid);
            }

            BGANinePhotoLayout ninePhotoLayout = helper.getView(R.id.npl_item_moment_photos);
            ninePhotoLayout.setData(moment.photos);
        }
    }
}
