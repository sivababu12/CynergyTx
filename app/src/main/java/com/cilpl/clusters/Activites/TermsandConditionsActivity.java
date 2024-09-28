package com.cilpl.clusters.Activites;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cilpl.clusters.R;
import com.cilpl.clusters.databinding.ActivityForgetpasswordactivityBinding;
import com.cilpl.clusters.databinding.ActivityTermsandConditionsBinding;

public class TermsandConditionsActivity extends AppCompatActivity {
    private ActivityTermsandConditionsBinding binding;
    String url,pageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTermsandConditionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
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

    private void initViews() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
            pageName = bundle.getString("p_name");
        }
        binding.texname.setText(pageName);
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.loadUrl(url);
    }
}