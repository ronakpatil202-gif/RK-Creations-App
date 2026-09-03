package com.rkcreations.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

    private WebView webView;
    private LinearLayout loadingLayout;

    private static final String WEBSITE =
            "https://papaya-empanada-75b97e.netlify.app/";

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showSplash();

        handler.postDelayed(() -> showMainInterface(), 1200);
    }

    private void showSplash() {

        LinearLayout splash = new LinearLayout(this);
        splash.setOrientation(LinearLayout.VERTICAL);
        splash.setGravity(Gravity.CENTER);
        splash.setBackgroundColor(Color.rgb(20, 20, 20));

        TextView logo = new TextView(this);
        logo.setText("RK");
        logo.setTextColor(Color.WHITE);
        logo.setTextSize(48);
        logo.setGravity(Gravity.CENTER);
        logo.setTypeface(null, Typeface.BOLD);

        GradientDrawable circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        circle.setColor(Color.rgb(225, 29, 72));
        logo.setBackground(circle);

        splash.addView(
                logo,
                new LinearLayout.LayoutParams(dp(130), dp(130))
        );

        TextView name = new TextView(this);
        name.setText("RK CREATIONS");
        name.setTextColor(Color.WHITE);
        name.setTextSize(24);
        name.setGravity(Gravity.CENTER);
        name.setTypeface(null, Typeface.BOLD);

        LinearLayout.LayoutParams nameParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        nameParams.topMargin = dp(20);
        splash.addView(name, nameParams);

        TextView tagline = new TextView(this);
        tagline.setText("Creative Design • Digital Services");
        tagline.setTextColor(Color.LTGRAY);
        tagline.setTextSize(14);
        tagline.setGravity(Gravity.CENTER);

        splash.addView(tagline);

        setContentView(splash);
    }

    private void showMainInterface() {

        if (!isInternetAvailable()) {
            showOffline();
            return;
        }

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.WHITE);

        loadingLayout = new LinearLayout(this);
        loadingLayout.setOrientation(LinearLayout.VERTICAL);
        loadingLayout.setGravity(Gravity.CENTER);
        loadingLayout.setBackgroundColor(Color.WHITE);

        ProgressBar progressBar = new ProgressBar(this);

        TextView loadingText = new TextView(this);
        loadingText.setText("Loading RK Creations...");
        loadingText.setTextSize(17);
        loadingText.setTextColor(Color.DKGRAY);
        loadingText.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams progressParams =
                new LinearLayout.LayoutParams(dp(50), dp(50));

        progressParams.bottomMargin = dp(15);

        loadingLayout.addView(progressBar, progressParams);
        loadingLayout.addView(loadingText);

        root.addView(
                loadingLayout,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );

        webView = new WebView(this);
        webView.setVisibility(View.INVISIBLE);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(false);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    WebResourceRequest request) {

                String url = request.getUrl().toString();

                if (url.startsWith("whatsapp://")
                        || url.contains("wa.me")
                        || url.contains("whatsapp.com")) {

                    try {
                        Intent intent = new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(url)
                        );

                        startActivity(intent);

                    } catch (Exception e) {
                        view.loadUrl(url);
                    }

                    return true;
                }

                if (url.startsWith("tel:")) {

                    Intent intent = new Intent(
                            Intent.ACTION_DIAL,
                            Uri.parse(url)
                    );

                    startActivity(intent);
                    return true;
                }

                if (url.startsWith("mailto:")) {

                    Intent intent = new Intent(
                            Intent.ACTION_SENDTO,
                            Uri.parse(url)
                    );

                    startActivity(intent);
                    return true;
                }

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(
                    WebView view,
                    String url) {

                loadingLayout.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(
                    WebView view,
                    WebResourceRequest request,
                    android.webkit.WebResourceError error) {

                if (request.isForMainFrame()) {
                    showOffline();
                }
            }
        });

        root.addView(
                webView,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );

        setContentView(root);

        webView.loadUrl(WEBSITE);
    }

    private void showOffline() {

        LinearLayout offline = new LinearLayout(this);
        offline.setOrientation(LinearLayout.VERTICAL);
        offline.setGravity(Gravity.CENTER);
        offline.setPadding(dp(30), dp(30), dp(30), dp(30));

        TextView icon = new TextView(this);
        icon.setText("📡");
        icon.setTextSize(55);
        icon.setGravity(Gravity.CENTER);
        offline.addView(icon);

        TextView title = new TextView(this);
        title.setText("You're Offline");
        title.setTextSize(26);
        title.setTextColor(Color.rgb(30, 30, 30));
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, Typeface.BOLD);
        offline.addView(title);

        TextView message = new TextView(this);
        message.setText(
                "RK Creations needs an internet connection.\n\n" +
                "Please connect to Wi-Fi or mobile data and try again."
        );
        message.setTextSize(16);
        message.setTextColor(Color.GRAY);
        message.setGravity(Gravity.CENTER);

        offline.addView(message);

        TextView retry = new TextView(this);
        retry.setText("  TRY AGAIN  ");
        retry.setTextSize(16);
        retry.setTextColor(Color.WHITE);
        retry.setGravity(Gravity.CENTER);
        retry.setTypeface(null, Typeface.BOLD);

        GradientDrawable button = new GradientDrawable();
        button.setColor(Color.rgb(225, 29, 72));
        button.setCornerRadius(dp(15));

        retry.setBackground(button);
        retry.setPadding(dp(25), dp(15), dp(25), dp(15));

        retry.setOnClickListener(v -> {
            if (isInternetAvailable())