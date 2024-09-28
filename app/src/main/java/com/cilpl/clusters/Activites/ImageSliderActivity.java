package com.cilpl.clusters.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cilpl.clusters.R;
import com.cilpl.clusters.databinding.ActivityImageSliderBinding;

public class ImageSliderActivity extends AppCompatActivity {
     private ActivityImageSliderBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityImageSliderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }

    private void initViews() {
    }
}