package com.cilpl.clusters.Activites;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.cilpl.clusters.Adapters.Cart_Adapter;
import com.cilpl.clusters.Model.CartModel;
import com.cilpl.clusters.Model.TimeSlotsModel;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.NetworkCalling.NetworkServiceCall;
import com.cilpl.clusters.NetworkCalling.OnServiceCallCompleteListener;
import com.cilpl.clusters.NetworkCalling.ServerURL;
import com.cilpl.clusters.R;
import com.cilpl.clusters.databinding.ActivityCartBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding biding;
    SharedPreferences userdata;
    HelperClass helperClass;
    String authtoken,cartType="0";

    private ArrayList<CartModel> cartModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        biding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(biding.getRoot());
        InitVIews();
        onClicks();
    }

    private void onClicks() {
        biding.backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent i = new Intent(CartActivity.this,HomeMainActivity.class);
                startActivity(i);
            }
        });
        biding.addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CartActivity.this,HomeMainActivity.class);
                startActivity(i);
            }
        });
        biding.checkoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(CartActivity.this,CheckOutActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)cartModels);
                i.putExtra("BUNDLE",args);
                startActivity(i);
                finish();
            }
        });


        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(CartActivity.this,HomeMainActivity.class);
                startActivity(i);
            }
        };
        CartActivity.this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void InitVIews() {
        userdata = getSharedPreferences("Userdata", MODE_PRIVATE);
        authtoken = userdata.getString("authToken", "");
        helperClass = new HelperClass(this);
        if (helperClass.checkInternetConnection(CartActivity.this)) {
            cartApicall(authtoken);
        }else {
            Toast.makeText(CartActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();
        }


    }

    private void cartApicall(String authtoken) {
        try {
            Log.e("sadsadsaa", "eeee");
            String slideimagesdetails = ServerURL.GetCartData();
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("authToken", authtoken);
            Log.e("cartActivity", slideimagesdetails+","+authtoken);
            NetworkServiceCall serviceCall = new NetworkServiceCall(CartActivity.this, true);
            serviceCall.setOnServiceCallCompleteListener(new CartActivity.CompleteListener());
            serviceCall.makeJSONObjectPostStringRequest(slideimagesdetails, hashMap, Request.Priority.HIGH);
            // delete total files in folder
            //dletefiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class CompleteListener implements OnServiceCallCompleteListener {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            try {
                cartModels = new ArrayList<>();
                Log.e("cartActivity", jsonObject.toString());
                String status = jsonObject.getString("status");
                String errorcode = jsonObject.getString("errorCode");
                String message = jsonObject.getString("message");
                if(status.equals("0")){
                    biding.errormessage.setVisibility(View.VISIBLE);
                    biding.btns.setVisibility(View.GONE);
                    biding.languagesRecycle.setVisibility(View.GONE);
                }else {
                    biding.errormessage.setVisibility(View.GONE);
                    biding.btns.setVisibility(View.VISIBLE);
                    biding.languagesRecycle.setVisibility(View.VISIBLE);
                    JSONArray array = jsonObject.getJSONArray("Data");
                    for (int i = 0; i < array.length(); i++) {
                        Log.e("HomeFragment", array.toString());
                        JSONObject object = array.getJSONObject(i);
                        CartModel model=new CartModel();
                        model.setCart_id(object.getString("cart_id"));
                        model.setProduct_id(object.getString("product_id"));
                        model.setBooking_type(object.getString("booking_type"));
                        model.setProduct_name(object.getString("product_name"));
                        model.setFrom_date(object.getString("from_date"));
                        model.setChairs(object.getString("chairs"));
                        model.setTo_date(object.getString("to_date"));
                        model.setSlots(object.getString("slots"));
                        model.setPrice(object.getString("amount"));
                        model.setS_flag("0");
                        cartModels.add(model);
                    }
                    Cart_Adapter cart_adapter=new Cart_Adapter(CartActivity.this,cartModels, cartType);
                    LinearLayoutManager layoutManager=new LinearLayoutManager(CartActivity.this);
                    biding.languagesRecycle.setAdapter(cart_adapter);
                    biding.languagesRecycle.setLayoutManager(layoutManager);
                    cart_adapter.notifyDataSetChanged();
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