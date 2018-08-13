package cn.bingoogolapple.photopicker.demo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.photopicker.demo.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class testActivity extends AppCompatActivity {

    private Button button1,button2;
    private ImageView imageView;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap)msg.obj;
            imageView.setImageBitmap(bitmap);
            //Toast.makeText(MainActivity.this, Environment.getExternalStorageDirectory()+"",Toast.LENGTH_LONG).show();
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        button1 = (Button)findViewById(R.id.test);
        button2 = (Button)findViewById(R.id.test2);
        imageView = (ImageView)findViewById(R.id.imageView2);
        button1.setOnClickListener(new View.OnClickListener() {//将服务器的图片读取到本地
            public void onClick(View view) {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://47.106.83.240/favicon.ico")
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream inputStream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        Message msg = new Message();
                        msg.obj = bitmap;
                        handler.sendMessage(msg);
                    }
                });
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMultiFile();
            }
        }) ;


    }
    private void uploadMultiFile() {//将图片发送到服务器
        final String url = "http://47.106.83.240/upload.php";
        File file = new File("/sdcard/1451607699899.jpg");
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        File file2 = new File( "/sdcard/1451607699964.jpg");
        RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), file2);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image1", "1451607699899.jpg", fileBody)
                .addFormDataPart("image2", "1451607699964.jpg", fileBody2)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();


        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient  = httpBuilder
                //设置超时
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("aa", "uploadMultiFile() e=" + e);
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String url=response.body().string();
                Log.i("url",url);
            }
        });
    }}