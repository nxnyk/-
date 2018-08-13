package cn.bingoogolapple.photopicker.demo.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.baseadapter.BGAOnRVItemLongClickListener;
import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.demo.Config.DButil;
import cn.bingoogolapple.photopicker.demo.Config.shuju;
import cn.bingoogolapple.photopicker.demo.R;
import cn.bingoogolapple.photopicker.demo.activity.MainActivity;
import cn.bingoogolapple.photopicker.demo.activity.wpxinxiActivity;
import cn.bingoogolapple.photopicker.demo.model.Moment;
import cn.bingoogolapple.photopicker.imageloader.BGARVOnScrollListener;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeFragment extends Fragment implements EasyPermissions.PermissionCallbacks, BGANinePhotoLayout.Delegate, BGAOnRVItemClickListener, BGAOnRVItemLongClickListener {
    Activity mainActivity;
    private RecyclerView mMomentRv;
    private HomeAdapter mMomentAdapter;
    private BGANinePhotoLayout mCurrentClickNp1;
    public static String chuandifbz;
    public static String chuandiname;
    public static String chuandijiage;
    public static String chuandimiaoshu;
    public static String chuandiid;
    public static String[] chuandiurl;
    private static final int PRC_PHOTO_PREVIEW = 1;
    public Connection conn2;
    private ArrayList<String> jxsj=new ArrayList<>();
    List<Moment> moments = new ArrayList<>();
    @Nullable
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mainActivity=getActivity();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home, container, false);
            mMomentRv = view.findViewById(R.id.homeshow);
            mCurrentClickNp1=view.findViewById(R.id.npl_item_moment_photos);
            mMomentRv.addOnScrollListener(new BGARVOnScrollListener(mainActivity));
            mMomentAdapter = new HomeAdapter(mMomentRv);
            mMomentRv.setAdapter(mMomentAdapter);
            mMomentRv.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mMomentAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
                @Override
                public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                    chuandijiage=moments.get(position).price;
                    chuandifbz=moments.get(position).wupingfabuzhe;
                    chuandiname=moments.get(position).content;
                    chuandimiaoshu=moments.get(position).wupingid;
                    chuandiid=moments.get(position).ID;
                    chuandiurl=(String []) moments.get(position).photos.toArray(new String[moments.get(position).photos.size()]);
                    Intent i=new Intent(getActivity(),wpxinxiActivity.class);
                    startActivity(i);
                }
            });
        getdata();
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
//
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
    //获取数据
    public void getdata()
    {
        Thread r=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DButil dButil=new DButil();
                    conn2=dButil.GetConnection();
                    String upsql="select * from wuping order by wupingid desc";
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
                }catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        r.start();
//        for (int i=0;i<jxsj.size();i++)
//        {
//            ArrayList<String> ls1=new ArrayList<>();
//            String s=jxsj.get(i).getwpurl();
//            String[] ls=s.split("images/");
//            String[] sp=new String[ls.length];
//            for(int j=1;j<ls.length;j++)
//            {
//                sp[j]="http://47.106.83.240/images/"+ls[j];
//                ls1.add(sp[j]);
//            }
//            Moment m=new Moment(jxsj.get(i).getwpname(),jxsj.get(i).getwpjiage(),jxsj.get(i).getWpid(),jxsj.get(i).getWpfabuzhe(),jxsj.get(i).getwpms(),ls1);
//            moments.add(new Moment("测试：","888","qwe","测试","asd",new ArrayList<>(Arrays.asList("https://ss0.bdstatic.com/k4oZeXSm1A5BphGlnYG/skin/543.jpg?2"))));
//            moments.add(m);
//            Message message=new Message();
//            Bundle bundle=new Bundle();
//            bundle.putParcelableArrayList("moment", (ArrayList<? extends Parcelable>) moments);
//            message.setData(bundle);
//            handler.sendMessage(message);
//        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
//        Toast.makeText(getActivity(),"awadwdsad",Toast.LENGTH_LONG);

    }

    @Override
    public boolean onRVItemLongClick(ViewGroup parent, View itemView, int position) {
        return false;
    }



    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {

    }


    public class HomeAdapter extends BGARecyclerViewAdapter<Moment> {

        public HomeAdapter(RecyclerView recyclerView) {
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

//    //预览图片
//    @AfterPermissionGranted(PRC_PHOTO_PREVIEW)
//    private void photoPreviewWrapper() {
//        if (mCurrentClickNp1 == null) {
//            Log.i("等于控制","阿达大厦的1111111111111") ;
//            return;
//        }
//
//        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        if (EasyPermissions.hasPermissions(mainActivity, perms)) {
//            File downloadDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerDownload");
//            BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(mainActivity)
//                    .saveImgDir(downloadDir); // 保存图片的目录，如果传 null，则没有保存图片功能
//
//            if (mCurrentClickNp1.getItemCount() == 1) {
//                // 预览单张图片
//                photoPreviewIntentBuilder.previewPhoto(mCurrentClickNp1.getCurrentClickItem());
//            } else if (mCurrentClickNp1.getItemCount() > 1) {
//                // 预览多张图片
//                photoPreviewIntentBuilder.previewPhotos(mCurrentClickNp1.getData())
//                        .currentPosition(mCurrentClickNp1.getCurrentClickItemPosition()); // 当前预览图片的索引
//            }
//            startActivity(photoPreviewIntentBuilder.build());
//        } else {
//            EasyPermissions.requestPermissions(this, "图片预览需要以下权限:\n\n1.访问设备上的照片", 1, perms);
//        }
//    }




//    @Override
//    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
//        mCurrentClickNp1 = ninePhotoLayout;
//        Log.i("123","12321312313123") ;
//        photoPreviewWrapper();
//    }
@AfterPermissionGranted(PRC_PHOTO_PREVIEW)
private void photoPreviewWrapper() {
    if (mCurrentClickNp1 == null) {
        return;
    }

    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    if (EasyPermissions.hasPermissions(getActivity(), perms)) {
        File downloadDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerDownload");
        BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(getActivity())
                .saveImgDir(downloadDir); // 保存图片的目录，如果传 null，则没有保存图片功能

        if (mCurrentClickNp1.getItemCount() == 1) {
            // 预览单张图片
            photoPreviewIntentBuilder.previewPhoto(mCurrentClickNp1.getCurrentClickItem());
        } else if (mCurrentClickNp1.getItemCount() > 1) {
            // 预览多张图片
            photoPreviewIntentBuilder.previewPhotos(mCurrentClickNp1.getData())
                    .currentPosition(mCurrentClickNp1.getCurrentClickItemPosition()); // 当前预览图片的索引
        }
        startActivity(photoPreviewIntentBuilder.build());
    } else {
        EasyPermissions.requestPermissions(this, "图片预览需要以下权限:\n\n1.访问设备上的照片", PRC_PHOTO_PREVIEW, perms);
    }
}
}
