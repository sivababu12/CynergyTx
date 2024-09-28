package com.cilpl.clusters.Activites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.cilpl.clusters.Activites.ui.gallery.GalleryFragment;
import com.cilpl.clusters.Activites.ui.home.HomeFragment;
import com.cilpl.clusters.Activites.ui.slideshow.SlideshowFragment;
import com.cilpl.clusters.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.cilpl.clusters.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    HomeFragment homeFragment;
    GalleryFragment galleryFragment;
    SlideshowFragment slideshowFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);
//        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setItemIconTintList(null);
        IntitViews();
        getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment_content_home, homeFragment).commit();

    }

    private void IntitViews() {
        homeFragment = new HomeFragment();
        galleryFragment = new GalleryFragment();
        slideshowFragment = new SlideshowFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.btmnavigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, homeFragment).commit();
                return true;

            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, galleryFragment).commit();
                return true;

            case R.id.about:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, slideshowFragment).commit();
                return true;
            case R.id.clusternw:
                Intent i = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(i);
                return true;
        }
        return false;
    }
}