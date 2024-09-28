package com.cilpl.clusters.Activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cilpl.clusters.Activites.Interfaces.ApiSucessResponse;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;
import com.cilpl.clusters.databinding.ActivityChangePassordBinding;
import com.cilpl.clusters.databinding.ActivityConfirmPassordBinding;

public class ChangePassordActivity extends AppCompatActivity {
 private ActivityChangePassordBinding binding;
 String pass1,pass2,authtoken,oldpass;
 HelperClass helperClass;
 ApiDataSync dataSync;
 SharedPreferences userdata;
 ApiSucessResponse apiSucessResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePassordBinding.inflate(getLayoutInflater());
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
                                Intent i = new Intent(ChangePassordActivity.this,LoginActivity.class);
                                startActivity(i);
                            }else {
                                Toast.makeText(ChangePassordActivity.this, "Please give Proper Details", Toast.LENGTH_SHORT).show();
                            }
                            Log.e("printtest",status);
                        };
                        dataSync.changePasswordCreation(authtoken,oldpass,pass1,pass2,apiSucessResponse);
//
                    }else {
                        Toast.makeText(ChangePassordActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();
                    }





                }
            }
        });
    }

    private boolean Validations() {

        pass1=binding.pass1Id.getText().toString();
        pass2=binding.pass2Id.getText().toString();
        oldpass=binding.oldpassId.getText().toString();
        if (oldpass.isEmpty() || oldpass.equals(" ")) {
            binding.oldpassId.setError("Enter Your Old Password");
            //Toast.makeText(this, "Enter your Password", Toast.LENGTH_SHORT).show();
            return false;
        }
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