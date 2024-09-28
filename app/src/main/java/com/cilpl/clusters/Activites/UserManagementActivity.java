package com.cilpl.clusters.Activites;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.cilpl.clusters.databinding.ActivityContactusBinding;

public class UserManagementActivity extends AppCompatActivity {
private ActivityContactusBinding binding;
String pagename,url="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityContactusBinding.inflate(getLayoutInflater());
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
        // this will enable the javascript.
        webSettings.setJavaScriptEnabled(true);

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.

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