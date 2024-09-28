package com.cilpl.clusters.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cilpl.clusters.R;
import com.cilpl.clusters.databinding.ActivityInvoiceViiewBinding;

public class InvoiceVIiewActivity extends AppCompatActivity {
 private ActivityInvoiceViiewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityInvoiceViiewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        inItViews();
        onClick();
    }

    private void onClick() {
    }

    private void inItViews() {

    }
}