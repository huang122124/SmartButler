package com.example.surface.smartbutler.ui;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.utils.L;
import com.example.surface.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

public  class UpdateActivity extends BaseActivity{
    private TextView tv_size;
    private String url;
    private String path;
    private File dest;
    //正在下载
    public static final int HANDLE_LODING=10001;
    //下载完成
    public static final int HANDLE_OK=10002;
    //下载失败
    public static final int HANDLE_FAILED=10003;
    private Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case HANDLE_LODING:

                break;
            case HANDLE_OK:
                  startInstallApk();
                break;
            case HANDLE_FAILED:

                break;
        }
    }
};

    private void startInstallApk() {
        Intent i=new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setDataAndType(Uri.fromFile(dest),"application/vnd.android.package-archive");
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initView();
    }

    private long mTaskId;
    private DownloadManager downloadManager;

    private void initView() {
        tv_size=findViewById(R.id.tv_size);
        path=FileUtils.getSDCardPath()+ "/a.apk";
        String fileName="a.apk";
        //获取apk下载地址
        url=getIntent().getStringExtra("url");
        //开始下载
        download();

    }

    private void download() {
        final long startTime = System.currentTimeMillis();
        L.i("startTime="+startTime);
        Request request=new Request.Builder().url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(HANDLE_FAILED);
                    L.e("download failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Sink sink = null;
                BufferedSink bufferedSink = null;
                try {
                    String mSDCardPath= Environment.getExternalStorageDirectory().getAbsolutePath();
                    dest = new File(mSDCardPath,  url.substring(url.lastIndexOf("/") + 1));
                    sink = Okio.sink(dest);
                    bufferedSink = Okio.buffer(sink);
                    bufferedSink.writeAll(response.body().source());
                    bufferedSink.close();

                    handler.sendEmptyMessage(HANDLE_OK);
                    L.i("download success");
                    Toast.makeText(UpdateActivity.this, "download success", Toast.LENGTH_SHORT).show();
                    L.i("totalTime="+ (System.currentTimeMillis() - startTime));
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(HANDLE_FAILED);
                    L.e("download failed");
                } finally {
                    if(bufferedSink != null){
                        bufferedSink.close();
                    }
                }

            }
        });

    }
    //


}
