package cn.bingoogolapple.photopicker.demo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.nanchen.compresshelper.CompressHelper;
import com.nanchen.compresshelper.StringUtil;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.photopicker.activity.BGAPPToolbarActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.demo.Config.DButil;
import cn.bingoogolapple.photopicker.demo.R;
import cn.bingoogolapple.photopicker.demo.model.Moment;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
//*
// yyk create date2018.05.01
// *
public class MomentAddActivity extends BGAPPToolbarActivity implements EasyPermissions.PermissionCallbacks, BGASortableNinePhotoLayout.Delegate {
    private static final int PRC_PHOTO_PICKER = 1;

    private static final int RC_CHOOSE_PHOTO = 1;
    private static final int RC_PHOTO_PREVIEW = 2;
    private static final String EXTRA_MOMENT = "EXTRA_MOMENT";

    private CheckBox mSingleChoiceCb;
    private EditText title;
    private String price;
    private EditText editprice;
    private List<String> photosurl;
    private List<String> photosurladd;
    public Connection conn2;

    private CheckBox mTakePhotoCb;
    private CheckBox mEditableCb;
    public String TUPIANurl;
    private CheckBox mPlusCb;
    private CheckBox mSortableCb;
    private BGASortableNinePhotoLayout mPhotosSnpl;
    private EditText miaoshu;
    final String[] StringUrl = {null};
    public static Moment getMoment(Intent intent) {
        return intent.getParcelableExtra(EXTRA_MOMENT);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_moment_add);
        mSingleChoiceCb = findViewById(R.id.cb_moment_add_single_choice);
        mTakePhotoCb = findViewById(R.id.cb_moment_add_take_photo);
        title = findViewById(R.id.title);
        mEditableCb = findViewById(R.id.cb_moment_add_editable);
        mPlusCb = findViewById(R.id.cb_moment_add_plus);
        mSortableCb = findViewById(R.id.cb_moment_add_sortable);
        editprice = findViewById(R.id.editprice);
        miaoshu = findViewById(R.id.miaoshu);
        mPhotosSnpl = findViewById(R.id.snpl_moment_add_photos);
    }

    @Override
    protected void setListener() {
        mSingleChoiceCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    mPhotosSnpl.setData(null);
                    mPhotosSnpl.setMaxItemCount(1);
                } else {
                    mPhotosSnpl.setMaxItemCount(9);
                }
            }
        });
        mEditableCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                mPhotosSnpl.setEditable(checked);
            }
        });
        mPlusCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                mPhotosSnpl.setPlusEnable(checked);
            }
        });
        mSortableCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                mPhotosSnpl.setSortable(checked);
            }
        });

        mPhotosSnpl.setDelegate(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        setTitle("发布物品：");

        mEditableCb.setChecked(mPhotosSnpl.isEditable());
        mPlusCb.setChecked(mPhotosSnpl.isPlusEnable());
        mSortableCb.setChecked(mPhotosSnpl.isSortable());
        mTakePhotoCb.setChecked(true);
    }

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    new AlertDialog.Builder(MomentAddActivity.this).setTitle("提示:")
                            .setMessage("发布成功！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).show();
                    break;
                case 0:
                        new AlertDialog.Builder(MomentAddActivity.this).setTitle("提示:")
                                .setMessage("发布失败！请重新尝试！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                        break;
            }
        }
    };
    public void onClick(View v) {


        if (v.getId() == R.id.tv_moment_add_publish) {
            String content = title.getText().toString().trim();
            String content2 = miaoshu.getText().toString().trim();
            String content3 = editprice.getText().toString().trim();
            if (content.length() == 0) {
                Toast.makeText(this, "必须添加标题！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mPhotosSnpl.getItemCount() == 0) {
                Toast.makeText(this, "必须添加图片！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (content2.length() == 0) {
                Toast.makeText(this, "必须添加物品表述！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (content3.length() == 0) {
                Toast.makeText(this, "必须设置价格！", Toast.LENGTH_SHORT).show();
                return;
            }
//            Intent intent = new Intent(MomentAddActivity.this,MomentListActivity.class);
//            intent.putExtra(EXTRA_MOMENT, new Moment(editprice.getText().toString().trim(),title.getText().toString().trim(), mPhotosSnpl.getData()));
//            setResult(RESULT_OK, intent);
//            startActivity(intent);
            sendMultipart();
            indata();
//            if(!StringUtil.isEmpty(TUPIANurl))
//            {
//                indata();
//                new AlertDialog.Builder(MomentAddActivity.this).setTitle("提示:")
//                        .setMessage("发布成功！")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//                            }
//                        }).show();
//            }
//            else
//                {
//                    new AlertDialog.Builder(MomentAddActivity.this).setTitle("提示:")
//                            .setMessage("发布失败！请重新尝试！")
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                }
//                            }).show();
//                }

        }
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
//        if(photosurl!=null)
//        {
//        for(int i=0;i<photosurl.size();i++)
//        {
//            photosurladd.add(photosurl.get(i));
//        }
//        }
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(this)
                .previewPhotos(models) // 当前预览的图片路径集合
                .selectedPhotos(models) // 当前已选中的图片路径集合
                .maxChooseCount(mPhotosSnpl.getMaxItemCount()) // 图片选择张数的最大值
                .currentPosition(position) // 当前预览图片的索引
                .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                .build();
        startActivityForResult(photoPickerPreviewIntent, RC_PHOTO_PREVIEW);
    }

    @Override
    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {
        Toast.makeText(this, "排序发生变化", Toast.LENGTH_SHORT).show();
    }

    @AfterPermissionGranted(PRC_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");

            Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                    .cameraFileDir(mTakePhotoCb.isChecked() ? takePhotoDir : null) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                    .maxChooseCount(mPhotosSnpl.getMaxItemCount() - mPhotosSnpl.getItemCount()) // 图片选择张数的最大值
                    .selectedPhotos(null) // 当前已选中的图片路径集合
                    .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                    .build();
            startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", PRC_PHOTO_PICKER, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == PRC_PHOTO_PICKER) {
            Toast.makeText(this, "您拒绝了「图片选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {
            photosurl = BGAPhotoPickerActivity.getSelectedPhotos(data);
        }
        if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {
            if (mSingleChoiceCb.isChecked()) {
                mPhotosSnpl.setData(BGAPhotoPickerActivity.getSelectedPhotos(data));
            } else {
                mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data));
            }
        } else if (requestCode == RC_PHOTO_PREVIEW) {
            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
        }
    }

    //准备发送
    private void sendMultipart() {
        String panduan="failure";
        String content1 = title.getText().toString().trim();
        String content2 = miaoshu.getText().toString().trim();
        String content3 = editprice.getText().toString().trim();
        final String url = "http://localhost/uploadimage.php";
        File sdcache = getExternalCacheDir();
        int cacheSize = 100 * 1024 * 1024;
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("application/octet-stream");
        //设置超时时间及缓存
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        OkHttpClient mOkHttpClient = builder.build();
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        List<File> fileList = new ArrayList<File>();
        for (int j = 0; j <mPhotosSnpl.getItemCount(); j++) {
            //遍历进行图片压缩
            File oldfile=new File(mPhotosSnpl.getData().get(j));
            File newfile= CompressHelper.getDefault(this).compressToFile(oldfile);
            fileList.add(newfile);
        }
        int i = 0;
        for (File file : fileList) {
            if (file.exists()) {
                Log.i("imageName:", file.getName());//经过测试，此处的名称不能相同，如果相同，只能保存最后一个图片.
                mbody.addFormDataPart("image" + i, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
                i++;
            }
        }
        RequestBody requestBody = mbody.build();
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...")
                .url("http://192.168.1.100/uploadimage.php")
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("www", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                TUPIANurl=response.body().string();
                Log.i("qweqw",jx(TUPIANurl));
            }
        });
    }
    public void indata()
    {
//        final String home_IP = "47.106.83.240:3306";
//        final String sqlurl = "jdbc:mysql://" + home_IP + "/youershou";
//        final String user = "root";
//        final String password1 = "960528";
        mPhotosSnpl.getData();
        SimpleDateFormat name=new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String wupingid=name.format(new Date());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DButil dButil=new DButil();
                    conn2=dButil.GetConnection();
                    String upsql="insert into wuping values (?,?,?,?,?,?)";
                    PreparedStatement ps=conn2.prepareStatement(upsql);
                    ps.setString(1,title.getText().toString());
                    ps.setString(2,editprice.getText().toString());
                    ps.setString(3,miaoshu.getText().toString());
                    ps.setString(4,jx(TUPIANurl));
                    ps.setString(5,wupingid);
                    ps.setString(6, RegActivity.username);
                    ps.execute();
                    Message message=handler.obtainMessage();
                    message.what=1;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message1=handler.obtainMessage();
                    message1.what=0;
                    handler.sendMessage(message1);
                }
            }
        }).start();
    }
    public String  jx(String data)
    {
        String a=data.replaceAll("\\./img","/img");
        String b=a.replaceAll("\\s*","");
        return  b;
    }
//    private void uploadMultiFile() {//将图片发送到服务器
//        final String url = "http://47.106.83.240/upload.php";
//        File sdcache = getExternalCacheDir();
//        int cacheSize = 10 * 1024 * 1024;
//        OkHttpClient.Builder builder2 = new OkHttpClient.Builder()
//                .connectTimeout(15, TimeUnit.SECONDS)
//                .writeTimeout(20, TimeUnit.SECONDS)
//                .readTimeout(20, TimeUnit.SECONDS)
//                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
//        final MediaType MEDIA_TYPE_PNG = MediaType.parse("application/octet-stream");
//        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        File[]oldfiles;
//        File[]newfiles;
//        SimpleDateFormat name=new SimpleDateFormat("yyyyMMddHHmmssSSS");
//        String name2=name.format(new Date());
//        for(int i=0;i<photosurl.size();i++)
//        {
//            builder.addFormDataPart("image"+i,name2,RequestBody.create(MEDIA_TYPE_PNG,photosurl.get(i)));
//        }
//        for(int i=0;i<photosurl.size();i++)
//        {
//
//
//            File oldfile=new File(photosurl.get(i));
//            File newfile= CompressHelper.getDefault(this).compressToFile(oldfile);
//            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), newfile);
//            RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("image", name2+".jpg", fileBody)
//                    .build();
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(requestBody)
//                    .build();
//            final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
//            OkHttpClient okHttpClient  = httpBuilder
//                    //设置超时
//                    .connectTimeout(10000, TimeUnit.SECONDS)
//                    .writeTimeout(15000, TimeUnit.SECONDS)
//                    .build();
//            okHttpClient.newCall(request).enqueue(new Callback() {
//
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    Log.e("aa", "uploadMultiFile() e=" + e);
//                }
//
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    String url=response.body().string();
//                    Log.i("url",url);
//                }
//            });
//            s.append(photosurl.get(i));
//        }


//        File file2 = new File( "/sdcard/1451607699964.jpg");
//        RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), file2);

//                .addFormDataPart("image2", "1451607699964.jpg", fileBody2)



    }
