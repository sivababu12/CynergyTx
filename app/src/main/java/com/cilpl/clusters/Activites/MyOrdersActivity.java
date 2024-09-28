package com.cilpl.clusters.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.cilpl.clusters.Adapters.Mybookings_Adapter;
import com.cilpl.clusters.Model.MyBookingsModel;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.NetworkCalling.NetworkServiceCall;
import com.cilpl.clusters.NetworkCalling.OnServiceCallCompleteListener;
import com.cilpl.clusters.NetworkCalling.ServerURL;
import com.cilpl.clusters.R;
import com.cilpl.clusters.databinding.ActivityMyBookingsBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyOrdersActivity extends AppCompatActivity {
private ActivityMyBookingsBinding binding;
private Mybookings_Adapter adapter;
    SharedPreferences userdata;
    HelperClass helperClass;
    String authtoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyBookingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        oncick();
    }

    private void oncick() {
        binding.backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViews() {
        userdata = getSharedPreferences("Userdata", MODE_PRIVATE);
        authtoken = userdata.getString("authToken", "");
        helperClass = new HelperClass(this);
        if (helperClass.checkInternetConnection(MyOrdersActivity.this)) {

            myBookingsApi(authtoken);
        }else {
            Toast.makeText(MyOrdersActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();
        }



    }

    private void myBookingsApi(String authtoken) {
        try {
            String mybookings = ServerURL.getMyBookings();
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("authToken", authtoken);
        //    Log.e("mybookings",mybookings+","+ authtoken);
            NetworkServiceCall serviceCall = new NetworkServiceCall(MyOrdersActivity.this, true);
            serviceCall.setOnServiceCallCompleteListener(new CompleteListener());
            serviceCall.makeJSONObjectPostStringRequest(mybookings, hashMap, Request.Priority.HIGH);
            // delete total files in folder
            //dletefiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class CompleteListener implements OnServiceCallCompleteListener {
        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            try {
                ArrayList<MyBookingsModel> myBookingsModels = new ArrayList<>();

              //  Log.e("mybookings", jsonObject.toString());
                String status = jsonObject.getString("status");
                String errorcode = jsonObject.getString("errorCode");
                String message = jsonObject.getString("message");

                if(status.equals("0")){
                    binding.mybookingsRecycle.setVisibility(View.GONE);
                    binding.errormessage.setVisibility(View.VISIBLE);

                }else {
                    binding.mybookingsRecycle.setVisibility(View.VISIBLE);
                    binding.errormessage.setVisibility(View.GONE);
                    JSONArray array = jsonObject.getJSONArray("Data");
                    for (int i = 0; i < array.length(); i++) {
                      //  Log.e("mybookings", array.toString());
                        JSONObject object = array.getJSONObject(i);
                        MyBookingsModel model=new MyBookingsModel();
                        model.setName(object.getString("title"));
                        model.setProduct_id(object.getString("product_id"));
                        model.setFull_name(object.getString("full_name"));
                        model.setPhone(object.getString("phone"));
                        model.setEmail(object.getString("email"));
                        model.setFrom_date(object.getString("from_date"));
                        model.setTo_date(object.getString("to_date"));
                        model.setTimeslots(object.getString("slot_time"));
                        model.setChairs(object.getString("chairs"));
                        model.setBooking_id(object.getString("booking_id"));
                        model.setBooking_type(object.getString("booking_type"));
                        myBookingsModels.add(model);
                    }

                    adapter=new Mybookings_Adapter(MyOrdersActivity.this, myBookingsModels);
                    LinearLayoutManager layoutManager=new LinearLayoutManager(MyOrdersActivity.this);
                    binding.mybookingsRecycle.setAdapter(adapter);
                    binding.mybookingsRecycle.setLayoutManager(layoutManager);
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