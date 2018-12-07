package com.example.surface.smartbutler.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.utils.L;

public class WebViewActivity extends BaseActivity {
    private ProgressBar mProgressBar;
    private WebView mWebView;

    @TargetApi(Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void initView() {
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        final String url=intent.getStringExtra("url");
        L.i("url: "+url);
        //设置标题
        getSupportActionBar().setTitle(title);
        //加载进度条和WebView
        mProgressBar=findViewById(R.id.mProgressBar);
        mWebView=findViewById(R.id.mWebView);
        //加载网页逻辑
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        //接口回调
          mWebView.setWebChromeClient(new WebViewClient());
        //加载网页
        mWebView.loadUrl(url);
        //本地显示
        mWebView.setWebViewClient(new android.webkit.WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    public class WebViewClient extends WebChromeClient{
        //进度变化监听
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress==100){
                mProgressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}
