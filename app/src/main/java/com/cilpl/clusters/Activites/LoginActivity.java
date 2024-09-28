package com.cilpl.clusters.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.cilpl.clusters.Activites.Interfaces.ApiSucessResponse;
import com.cilpl.clusters.Activites.Interfaces.CarteResponse;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.NetworkCalling.NetworkServiceCall;
import com.cilpl.clusters.NetworkCalling.OnServiceCallCompleteListener;
import com.cilpl.clusters.NetworkCalling.ServerURL;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;
import com.cilpl.clusters.utils.ButtonEditTwCenMTFont;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    ButtonEditTwCenMTFont signbtn;
    EditText password_id, email_id;
    TextView signupPage,forgetpasssowrd;
    String deviceid = "", email, pass, authtoken = "";
    HelperClass helperClass;
    SharedPreferences userdata;
    ApiDataSync dataSync;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        InitViews();
        onClicks();
    }

    private void onClicks() {
        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Validation()) {
                    signbtn.setBackground(getResources().getDrawable(R.drawable.btn_bg));
//
                    if (helperClass.checkInternetConnection(LoginActivity.this)) {
                        CarteResponse apiSucessResponse = (status,messgae) -> {
                            //insertEventDB(name, payload);
                            if (status.equals("1")) {
                                Intent i = new Intent(LoginActivity.this, HomeMainActivity.class);
                                startActivity(i);
//                                finish();
                            } else {

                                Toast toast = Toast.makeText(LoginActivity.this, Html.fromHtml("<font color='#F44336' ><b>" + messgae + "</b></font>"), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP, 0, 0);
                                toast.show();

                               // Toast.makeText(LoginActivity.this, messgae, Toast.LENGTH_SHORT).show();
                            }
                       //     Log.e("printtest", status);
                        };

                        dataSync.LoginAPicall(email, pass, apiSucessResponse);
                    }else {
                        Toast.makeText(LoginActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();
                    }


                }


//
            }


        });
        signupPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
                //finish();
            }
        });
        forgetpasssowrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                i.putExtra("pass","forget");
                startActivity(i);
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private boolean Validation() {
        email = email_id.getText().toString();
        pass = password_id.getText().toString();

        if (email.isEmpty() || email.equals(" ")) {
            email_id.setError("Enter Your Email");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email_id.getText().toString()).matches()) {
            // Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            //  _email.setError("Invalid email");
            email_id.setError("Invalid Email");
            return false;
        }
        if (pass.isEmpty() || pass.equals(" ")) {
            password_id.setError("Enter Your Password");
            //Toast.makeText(this, "Enter your Password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void InitViews() {
        dataSync=new ApiDataSync(this);
        userdata = getSharedPreferences("Userdata", MODE_PRIVATE);
        authtoken = userdata.getString("authToken", "");
        signbtn = findViewById(R.id.signinbtn);
        signupPage = findViewById(R.id.signupPage);
        email_id = findViewById(R.id.email_id);
        password_id = findViewById(R.id.password_id);
        forgetpasssowrd = findViewById(R.id.forgetpass);
        helperClass = new HelperClass(this);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("Token", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                String token = task.getResult();

                // Log and toast
                String msg = token;
                deviceid = msg;
                Log.d("deviceid=", msg);
                //Toast.makeText(Login_Activity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class LoginActivityServiceCallCompleteListener implements OnServiceCallCompleteListener {
        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            try {
             //   Log.e("signupresponse", jsonObject.toString());
                String status = jsonObject.getString("status");
                String errorcode = jsonObject.getString("errorCode");
                String message = jsonObject.getString("message");
                if (status.equals("1")) {
                    String authtokenmessage = jsonObject.getString("authToken");
                    String uid = jsonObject.getString("id");
                    SharedPreferences.Editor editor = userdata.edit();
                    editor.putString("authToken", authtokenmessage);
                    editor.putString("userId", uid);
                    editor.putString("deviceId", deviceid);
                    editor.apply();
                    Intent i = new Intent(LoginActivity.this, HomeMainActivity.class);
                    startActivity(i);
                    finish();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onJSONArrayResponse(JSONArray jsonArray) {

        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }
    }
}