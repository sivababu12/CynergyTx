package com.cilpl.clusters.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cilpl.clusters.Activites.Interfaces.ApiSucessResponse;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;
import com.cilpl.clusters.databinding.ActivityConfirmPassordBinding;
import com.cilpl.clusters.databinding.ActivityForgetpasswordactivityBinding;

import org.checkerframework.checker.units.qual.C;

public class ConfirmPassordActivity extends AppCompatActivity {
 private ActivityConfirmPassordBinding binding;
 String pass1,pass2,authtoken;
 HelperClass helperClass;
 ApiDataSync dataSync;
 SharedPreferences userdata;
 ApiSucessResponse apiSucessResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmPassordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        InitVIews();
        onClicks();
    }

    private void onClicks() {
        binding.backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validations()){
                    if (helperClass.checkInternetConnection(getApplicationContext())) {
                        ApiSucessResponse apiSucessResponse = (status) -> {
                            //insertEventDB(name, payload);
                            if(status.equals("1")){
                                Intent i = new Intent(ConfirmPassordActivity.this,LoginActivity.class);
                                startActivity(i);
                            }else {
                                Toast.makeText(ConfirmPassordActivity.this, "Please give Proper Details", Toast.LENGTH_SHORT).show();
                            }
                            Log.e("printtest",status);
                        };
                        dataSync.newPasswordCreation(authtoken,pass1,pass2,apiSucessResponse);
//
                    }else {
                        Toast.makeText(ConfirmPassordActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();
                    }





                }
            }
        });
    }

    private boolean Validations() {
        pass1=binding.pass1Id.getText().toString();
        pass2=binding.pass2Id.getText().toString();
        if (pass1.isEmpty() || pass1.equals(" ")) {
            binding.pass1Id.setError("Enter Your Password");
            //Toast.makeText(this, "Enter your Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pass2.isEmpty() || pass2.equals(" ")) {
            binding.pass2Id.setError("Enter Confirm Password");
            //Toast.makeText(this, "Enter your Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!pass1.equals(pass2)) {
            Toast.makeText(this, "Password and Confirm Password should be same", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void InitVIews() {
        userdata = getSharedPreferences("Userdata", MODE_PRIVATE);
        authtoken = userdata.getString("authToken", "");

        helperClass=new HelperClass(this);
        dataSync=new ApiDataSync(this);


    }
}