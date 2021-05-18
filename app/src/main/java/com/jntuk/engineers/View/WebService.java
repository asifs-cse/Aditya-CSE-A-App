package com.jntuk.engineers.View;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.jntuk.engineers.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class WebService extends AppCompatActivity {

    private WebView webView;
    private String url;
    private ProgressBar progressBar;
    private TextView title_tv;
    private ImageView no_connection, web_back;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);

//        Window window = getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        url = getIntent().getStringExtra("url");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        webView = (WebView) findViewById(R.id.webView);
        mySwipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.web_refreshId);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        webView.reload();
                    }
                }
        );

        no_connection = findViewById(R.id.no_connection_imgId);
        web_back =findViewById(R.id.web_back);
        Glide.with(getApplicationContext()).load(R.drawable.back).into(web_back);
        title_tv = findViewById(R.id.web_titleId);
        title_tv.setSelected(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClientDemo());
        webView.setWebChromeClient(new WebChromeClientDemo());
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);

        checkConnection();
    }
    private void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            webView.setVisibility(View.VISIBLE);
            webView.loadUrl(url);
        } else {
            no_connection.setVisibility(View.VISIBLE);
            View parentLayout = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(parentLayout, "You are offline! Check Internet Connection...", Snackbar.LENGTH_LONG);
            snackbar.setDuration(5000);
            snackbar.show();
        }

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String s, final String s1, final String s2, final String s3, long l) {

                Dexter.withActivity(WebService.this)
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                try {
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(s));
                                    request.setMimeType(s3);
                                    String cookies = CookieManager.getInstance().getCookie(s);
                                    request.addRequestHeader("cookie",cookies);
                                    request.addRequestHeader("User-Agent",s1);
                                    request.setDescription("Downloading File.....");
                                    request.setTitle(URLUtil.guessFileName(s,s2,s3));
                                    request.allowScanningByMediaScanner();
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(
                                            Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                                                    s,s2,s3));
                                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                    downloadManager.enqueue(request);
                                    Toast.makeText(WebService.this, "Downloading File..", Toast.LENGTH_SHORT).show();
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(), "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            }
        });
        findViewById(R.id.web_back).setOnClickListener((View view)->{
            //startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        });
    }

    private class WebViewClientDemo extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(100);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            String title = view.getTitle();//getTitle
            super.doUpdateVisitedHistory(view, url, isReload);
            title_tv.setText(title);
        }
    }

    private class WebChromeClientDemo extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            progressBar.setProgress(progress);
        }
    }

    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public AssetManager getAssets() {
        return getResources().getAssets();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mySwipeRefreshLayout.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener =
                new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (webView.getScrollY() == 0)
                            mySwipeRefreshLayout.setEnabled(true);
                        else
                            mySwipeRefreshLayout.setEnabled(false);

                    }
                });
    }
}

