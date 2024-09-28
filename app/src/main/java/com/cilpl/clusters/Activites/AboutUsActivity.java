package com.cilpl.clusters.Activites;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.cilpl.clusters.databinding.ActivityAboutusBinding;
import com.cilpl.clusters.databinding.ActivityContactusBinding;

public class AboutUsActivity extends AppCompatActivity {
    private ActivityAboutusBinding binding;
    String pagename;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        InitViews();
        onClick();
    }

    private void onClick() {
        binding.backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void InitViews() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pagename = bundle.getString("pageName");
            url = bundle.getString("pageurl");
        }
        binding.texname.setText(pagename);
        WebSettings webSettings = binding.web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        binding.web.setWebViewClient(new Callback());  //HERE IS THE MAIN CHANGE
        binding.web.loadUrl(url);
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

    }

}