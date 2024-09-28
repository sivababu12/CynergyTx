package com.cilpl.clusters.Activites;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cilpl.clusters.Activites.ui.home.HomeFragment;
import com.cilpl.clusters.Adapters.AdminMenu_Adapter;
import com.cilpl.clusters.Adapters.Menu_Adapter;
import com.cilpl.clusters.Fragements.ClusterFragment;
import com.cilpl.clusters.Fragements.SupportFragment;
import com.cilpl.clusters.Fragements.ServicesFragment;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.NetworkCalling.ShowDialog;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;
import com.cilpl.clusters.databinding.ActivityHomemainBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeMainActivity extends AppCompatActivity {

    ActivityHomemainBinding binding;
    ImageView cancelButton;
    RecyclerView languages_recycle, admin_recycle;

    RelativeLayout profilepage;
    String authtoken, username, email, profileimage = "";
    TextView usernametxt, emailtxt;
    public static TextView cartcount;
    CircleImageView profie_image;
    ApiDataSync dataSync;
    HelperClass helperClass;
    ShowDialog dialog;
    SharedPreferences userdata;
    public static TextView hometitle;
    public static Dialog menudialog;

    //    private AppUpdateManager appUpdateManager;
    private static final int REQUEST_UPDATE = 123;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomemainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        helperClass = new HelperClass(this);
        dataSync = new ApiDataSync(this);
        if (helperClass.checkInternetConnection(HomeMainActivity.this)) {
            dataSync.GetListDetails();
        } else {

            Log.e("Homeacctivity", "No Internet");
        }
        AppUpdate();
//        listofmonth();
//        opendatepikcer();
        menudialog = new Dialog(HomeMainActivity.this);
        userdata = getSharedPreferences("Userdata", MODE_PRIVATE);
        authtoken = userdata.getString("authToken", "");
        username = userdata.getString("firstname", "");
        email = userdata.getString("email", "");
        profileimage = userdata.getString("profileimage", "");
        hometitle = findViewById(R.id.hometitle);
        dialog = new ShowDialog(this);
        cartcount = findViewById(R.id.cartcount1);
        Log.e("homeactivity", username + "," + authtoken + "," + email);
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.shorts:
                    replaceFragment(new ClusterFragment());
                    break;
                case R.id.subscriptions:
                    replaceFragment(new ServicesFragment());
                    break;
                case R.id.library:
                    replaceFragment(new SupportFragment());
                    break;
            }

            return true;
        });
        binding.notificationicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeMainActivity.this, R.string.no_notif, Toast.LENGTH_SHORT).show();
            }
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(HomeMainActivity.this, CartActivity.class);
                startActivity(i);
//                showBottomDialog();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                dialog.ShowExitPopup("Are you sure you want to Exit ?", "0");
            }
        };
        HomeMainActivity.this.getOnBackPressedDispatcher().addCallback(this, callback);

        menudialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding.menuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                menudialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Window window = menudialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;

                menudialog.setContentView(R.layout.menulist);
                menudialog.setCanceledOnTouchOutside(false);

                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                languages_recycle = menudialog.findViewById(R.id.languages_recycle);
                admin_recycle = menudialog.findViewById(R.id.admin_recycle);
                profilepage = menudialog.findViewById(R.id.profilepage);
                usernametxt = menudialog.findViewById(R.id.username);
                emailtxt = menudialog.findViewById(R.id.email);
                userdata = getSharedPreferences("Userdata", MODE_PRIVATE);
                authtoken = userdata.getString("authToken", null);
                username = userdata.getString("firstname", null);
                email = userdata.getString("email", null);
                profileimage = userdata.getString("profileimage", null);
                profie_image = menudialog.findViewById(R.id.acc_img);
                usernametxt.setText(username);
                emailtxt.setText(email);
                Glide.with(HomeMainActivity.this).load(profileimage).fitCenter().error(R.drawable.tempimg).into(profie_image);

                profilepage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(HomeMainActivity.this, ProfileActivity.class);
                        startActivity(i);
                        menudialog.dismiss();
                    }
                });
                cancelButton = menudialog.findViewById(R.id.cancelButton1);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        menudialog.dismiss();
                    }
                });

                // Changing side menu popup here

//                if(email.equals("siva.tavva@gmail.com")){
//                    admin_recycle.setVisibility(View.VISIBLE);
//                    languages_recycle.setVisibility(View.GONE);
//                    AdminMenu_Adapter adapter = new AdminMenu_Adapter(HomeMainActivity.this);
//                    LinearLayoutManager layoutManager = new LinearLayoutManager(HomeMainActivity.this);
//                    admin_recycle.setLayoutManager(layoutManager);
//                    admin_recycle.setAdapter(adapter);
//                }else {
//                    admin_recycle.setVisibility(View.GONE);
//                    languages_recycle.setVisibility(View.VISIBLE);
//                    Menu_Adapter adapter = new Menu_Adapter(HomeMainActivity.this);
//                    LinearLayoutManager layoutManager = new LinearLayoutManager(HomeMainActivity.this);
//                    languages_recycle.setLayoutManager(layoutManager);
//                    languages_recycle.setAdapter(adapter);
//                }

                Menu_Adapter adapter = new Menu_Adapter(HomeMainActivity.this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(HomeMainActivity.this);
                languages_recycle.setLayoutManager(layoutManager);
                languages_recycle.setAdapter(adapter);
                menudialog.show();
            }
        });

//        appUpdateManager = AppUpdateManagerFactory.create(this);
//
//        // Request update info
//        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//
//        // Check if update is available
//        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
//
//                // Request an immediate update
//                try {
//                    appUpdateManager.startUpdateFlowForResult(
//                            appUpdateInfo,
//                            AppUpdateType.IMMEDIATE,
//                            this,
//                            REQUEST_UPDATE);
//                } catch (IntentSender.SendIntentException e) {
//                    e.printStackTrace();
//                }
//            }
//        });


    }

    private void AppUpdate() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.

                ActivityResultLauncher<IntentSenderRequest> activityResultLauncher = null;
                appUpdateManager.startUpdateFlowForResult(
                        // Pass the intent that is returned by 'getAppUpdateInfo()'.
                        appUpdateInfo,
                        // an activity result launcher registered via registerForActivityResult
                        activityResultLauncher,
                        // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                        // flexible updates.
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build());
            }
        });
    }

    private void listofmonth() {
        YearMonth currentMonth = YearMonth.now();
        List<String> nextMonths = new ArrayList<>();
        for (int i = 1; i <= 12 - currentMonth.getMonthValue(); i++) {
            nextMonths.add(YearMonth.of(currentMonth.getYear(), currentMonth.getMonthValue() + i).format(DateTimeFormatter.ofPattern("MMM-yyyy", Locale.ENGLISH)));
        }
        Log.e("months", String.valueOf(nextMonths));
        System.out.println(nextMonths);


    }

    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_UPDATE) {
//            if (resultCode != RESULT_OK) {
//                // Update flow failed! Handle the error.
//                Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
    private void opendatepikcer() {
        List<String> allDates = new ArrayList<>();
        String maxDate = "May-2024";
        SimpleDateFormat monthDate = new SimpleDateFormat("MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(monthDate.parse(maxDate));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        for (int i = 1; i <= 12; i++) {
            String month_name1 = monthDate.format(cal.getTime());
            allDates.add(month_name1);
            cal.add(Calendar.MONTH, +1);
        }
        Log.e("dates", String.valueOf(allDates));
        System.out.println(allDates);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            dialogsbox();
            // finish();
        }
        return false;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


    private void dialogsbox() {
        Log.e("close", "app close");
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeMainActivity.this);
        builder.setMessage("Do you want to exit ?");
        builder.setTitle("Alert !");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {


            finishAffinity();
        });
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {

            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
        LinearLayout liveLayout = dialog.findViewById(R.id.layoutLive);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(HomeMainActivity.this, "Upload a Video is clicked", Toast.LENGTH_SHORT).show();

            }
        });

        shortsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(HomeMainActivity.this, "Create a short is Clicked", Toast.LENGTH_SHORT).show();

            }
        });

        liveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(HomeMainActivity.this, "Go live is Clicked", Toast.LENGTH_SHORT).show();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }


}