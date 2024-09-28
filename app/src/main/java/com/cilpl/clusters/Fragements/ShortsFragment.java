package com.cilpl.clusters.Fragements;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cilpl.clusters.R;
import com.cilpl.clusters.databinding.FragmentShortsBinding;
import com.google.android.material.tabs.TabLayout;


public class ShortsFragment extends Fragment {
private FragmentShortsBinding shortsBinding;
Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shortsBinding = FragmentShortsBinding.inflate(inflater, container, false);
        View root = shortsBinding.getRoot();
        // Inflate the layout for this fragment


        return root;
    }
}