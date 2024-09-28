package com.cilpl.clusters.Activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.android.volley.VolleyError;
import com.cilpl.clusters.Activites.Interfaces.ApiSucessResponse;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.NetworkCalling.OnServiceCallCompleteListener;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;
import com.cilpl.clusters.databinding.ActivityForgetpasswordactivityBinding;
import com.cilpl.clusters.databinding.ActivityOtpactivityBinding;

import org.json.JSONArray;
import org.json.JSONObject;

public class ForgetPasswordActivity extends AppCompatActivity {
private ActivityForgetpasswordactivityBinding binding;
String pinEntry,phonepin="",authdcode;
    SharedPreferences userdata;
    ApiDataSync apiDataSync;
    HelperClass helperClass;
    ApiSucessResponse apiSucessResponse;
    String email="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetpasswordactivityBinding.inflate(getLayoutInflater());
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
        binding.submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(Validations()){

                    if (helperClass.checkInternetConnection(ForgetPasswordActivity.this)) {
                        ApiSucessResponse apiSucessResponse = (status) -> {
                            //insertEventDB(name, payload);
                            if (status.equals("1")) {
                                Intent i = new Intent(ForgetPasswordActivity.this, OTPActivity.class);
                                i.putExtra("pass","forget");
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, "Please give Proper Details", Toast.LENGTH_SHORT).show();
                            }
                            Log.e("printtest", status);
                        };
//
                        apiDataSync.ForgetPasswordEmail(email,apiSucessResponse);

//                        Intent i = new Intent(ForgetPasswordActivity.this, OTPActivity.class);
//                        i.putExtra("pass","forget");
//                        startActivity(i);

                    }else {
                        Toast.makeText(ForgetPasswordActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();
                    }



                }
            }
        });
    }

    private boolean Validations() {

        email=binding.emailId.getText().toString();
        Log.e("emaiiil",email);
         if (email.isEmpty() || email.equals(" ")) {
            binding.emailId.setError("Enter Your Email");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailId.getText().toString()).matches()) {
            // Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            //  _email.setError("Invalid email");
            binding.emailId.setError("Invalid Email");
            return false;
        }
        return true;
    }

    private void InitVIews() {
        helperClass=new HelperClass(getApplicationContext());
        userdata = getSharedPreferences("Userdata", MODE_PRIVATE);
        apiDataSync=new ApiDataSync(this);



    }

    private class verifypneServiceCallCompleteListener implements OnServiceCallCompleteListener {
        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            Log.e("otpresponse",jsonObject.toString());
            Toast.makeText(ForgetPasswordActivity.this, "Registered Sucessfully.", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
            // Intent i = new Intent(ShortcutLocationPinActivity.this, LocationSelectActivity.class);
            startActivity(i);
            finish();
        }

        @Override
        public void onJSONArrayResponse(JSONArray jsonArray) {

        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }
    }
}