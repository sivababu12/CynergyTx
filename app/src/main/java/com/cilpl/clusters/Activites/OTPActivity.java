package com.cilpl.clusters.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.cilpl.clusters.Activites.Interfaces.ApiSucessResponse;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.NetworkCalling.NetworkServiceCall;
import com.cilpl.clusters.NetworkCalling.OnServiceCallCompleteListener;
import com.cilpl.clusters.NetworkCalling.ServerURL;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;
import com.cilpl.clusters.databinding.ActivityHomemainBinding;
import com.cilpl.clusters.databinding.ActivityOtpactivityBinding;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class OTPActivity extends AppCompatActivity {
private ActivityOtpactivityBinding otpactivityBinding;
String pinEntry,phonepin="",authdcode,pass="";
    SharedPreferences userdata;
    ApiDataSync apiDataSync;
    HelperClass helperClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        otpactivityBinding = ActivityOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(otpactivityBinding.getRoot());
        InitVIews();

    }

    private void InitVIews() {
        helperClass=new HelperClass(getApplicationContext());
        userdata = getSharedPreferences("Userdata", MODE_PRIVATE);
        apiDataSync=new ApiDataSync(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pass = bundle.getString("pass");
        }



        if (otpactivityBinding.txtPinEntry != null) {
            otpactivityBinding.txtPinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    if (!otpactivityBinding.txtPinEntry.getText().toString().isEmpty()) {
                        //Toast.makeText(PinActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                       // Log.e("pinentered", otpactivityBinding.txtPinEntry.getText().toString().trim());
                        SharedPreferences.Editor editor = userdata.edit();
                        phonepin = userdata.getString("otp", "");
                        authdcode = userdata.getString("authToken", "");
                        // editor.putString("device_id", deviceId);
                        editor.apply();
                    String pin = otpactivityBinding.txtPinEntry.getText().toString();
//                        if(appversion<=10) {

                        if (pin.equals(phonepin)) {
                            if (helperClass.checkInternetConnection(OTPActivity.this)) {
                                ApiSucessResponse apiSucessResponse = (status) -> {
                                    //insertEventDB(name, payload);
                                    if (status.equals("1")) {

                                        if(pass.equals("forget")){
                                            Intent i = new Intent(OTPActivity.this, ConfirmPassordActivity.class);
                                            startActivity(i);
                                            finish();
                                        }else {
                                            Toast.makeText(OTPActivity.this, "Registered Sucessfully.", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(OTPActivity.this, LoginActivity.class);
                                            startActivity(i);
                                            finish();
                                        }


                                    } else {
                                        otpactivityBinding.txtPinEntry.getText().clear();
                                        Toast.makeText(OTPActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                                    }
                               //     Log.e("printtest", status);
                                };
                                apiDataSync.OtpApicall(phonepin, authdcode,apiSucessResponse);
                            }else {
                                Toast.makeText(OTPActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();
                            }

                            //laxmanraju143.lr@gmail.com
//                            try {
//                                Log.e("sadsadsaa", "eeee");
//                                String regdetails = ServerURL.verifyOtp();
//                                HashMap<String, String> hashMap = new HashMap<>();
//                                hashMap.put("otp", phonepin);
//                                hashMap.put("authToken", authdcode);
//                                Log.e("otpresponse",phonepin+","+authdcode);
//                                NetworkServiceCall serviceCall = new NetworkServiceCall(OTPActivity.this, true);
//                                serviceCall.setOnServiceCallCompleteListener(new verifypneServiceCallCompleteListener());
//                                serviceCall.makeJSONObjectPostStringRequest(regdetails, hashMap, Request.Priority.HIGH);
//                                // delete total files in folder
//                                //dletefiles();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//
//                            otpactivityBinding.txtPinEntry.getText().clear();
//                        } else {
//
//
//                        }
//                        } Intent k = new Intent(ShortcutLocationPinActivity.this, LocationSelectActivity.class);
//                        startActivity(k);
//                        pinEntry.getText().clear();
                        }
                    }
                }
            });
        }
    }

    private class verifypneServiceCallCompleteListener implements OnServiceCallCompleteListener {
        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
       //     Log.e("otpresponse",jsonObject.toString());
            Toast.makeText(OTPActivity.this, "Registered Sucessfully.", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(OTPActivity.this, LoginActivity.class);
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