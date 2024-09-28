package com.cilpl.clusters.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cilpl.clusters.Adapters.InvouiceeAdapter;
import com.cilpl.clusters.Adapters.TableAdapter;
import com.cilpl.clusters.R;
import com.cilpl.clusters.databinding.ActivityEventsBinding;

public class EventsActivity extends AppCompatActivity {
private ActivityEventsBinding binding;
String pagename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEventsBinding.inflate(getLayoutInflater());
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
        }
        binding.texname.setText(pagename);


        InvouiceeAdapter adapter = new InvouiceeAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        binding.invoicelayout.setLayoutManager(layoutManager);
        binding.invoicelayout.setAdapter(adapter);
    }



}