package com.cilpl.clusters.Activites;

import static com.cilpl.clusters.Adapters.Cart_Adapter.cartlist;
import static com.cilpl.clusters.Adapters.Cart_Adapter.cartlist2;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.cilpl.clusters.Adapters.Cart_Adapter;
import com.cilpl.clusters.Adapters.Checkout_Adapter;
import com.cilpl.clusters.Model.CartModel;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.NetworkCalling.NetworkServiceCall;
import com.cilpl.clusters.NetworkCalling.OnServiceCallCompleteListener;
import com.cilpl.clusters.NetworkCalling.ServerURL;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;
import com.cilpl.clusters.databinding.ActivityCheckoutBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckOutActivity extends AppCompatActivity {
    private ActivityCheckoutBinding biding;
    SharedPreferences userdata;
    HelperClass helperClass;
    String authtoken, uname = "", emailid = "", phonenum = "", cartType = "1", compynyname = "", gstNo = "", sdate = "sdate", edate = "enddate", seates = "seates", btype = "clusters";
    ArrayList<Object> object;
    private ArrayList<CartModel> cartModels;
    LinearLayout slotpage, verifydetails, resultpage;
    ApiDataSync dataSync;
    public static ArrayList<String> cartlists;
    public static boolean isSelectedAll = true;
    public static boolean isUnSelectedAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        biding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(biding.getRoot());
        InitVIews();
        onClicks();
    }

    private void onClicks() {
        biding.backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        biding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PopUp();


                if (cartlists.isEmpty() ||cartlists.equals("")) {
                    Log.e("contacctlist", "empty");
                    Toast.makeText(CheckOutActivity.this, "Please Select Atleast 1 Item", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("contacctlist", cartlists.toString());
                    if (helperClass.checkInternetConnection(CheckOutActivity.this)) {
                        Log.e("CheckOutActivity", cartlists.toString());
                        PopUp(cartlists);
                    } else {
                        Toast.makeText(CheckOutActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    private void PopUp(ArrayList<String> cartlists) {
        {
            uname = userdata.getString("firstname", "");
            emailid = userdata.getString("email", "");
            phonenum = userdata.getString("phne", "");
            compynyname = userdata.getString("compy_name", "");
            gstNo = userdata.getString("gst_no", "");
            final Dialog dialog = new Dialog(CheckOutActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            dialog.setContentView(R.layout.bookslot);
            dialog.setCanceledOnTouchOutside(false);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            ImageView addimg = dialog.findViewById(R.id.add1_img);
            ImageView minusimg = dialog.findViewById(R.id.minus_img);
            TextView addtxt = dialog.findViewById(R.id.add_txt);
            TextView fstname = dialog.findViewById(R.id.fstname);
            TextView email_id = dialog.findViewById(R.id.email_id);
            TextView moblie_id = dialog.findViewById(R.id.moblie_id);
            TextView startdate = dialog.findViewById(R.id.startdate1);
            TextView enddate = dialog.findViewById(R.id.enddate1);
            Button submit = dialog.findViewById(R.id.sumit_btn);
            Button submit2 = dialog.findViewById(R.id.sumit2_btn);
            Button submit3 = dialog.findViewById(R.id.sumit3_btn);
            TextView compname = dialog.findViewById(R.id.compname);
            TextView gstno = dialog.findViewById(R.id.gstno);
            ImageView gif_image=dialog.findViewById(R.id.gif_image1);
            Glide.with(dialog.getContext())
                    .load(R.drawable.thumsup1)
                    .into(gif_image);
            resultpage = dialog.findViewById(R.id.book_result);
            slotpage = dialog.findViewById(R.id.book_slot);
            verifydetails = dialog.findViewById(R.id.verifydetails);
            ImageView cancel = dialog.findViewById(R.id.cancel_button);
            ImageView cance2 = dialog.findViewById(R.id.cancel_button1);
            slotpage.setVisibility(View.GONE);
            verifydetails.setVisibility(View.VISIBLE);
            fstname.setText(uname);
            email_id.setText(emailid);
            moblie_id.setText(phonenum);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            if (compynyname.isEmpty()) {
                compname.setText("");
            } else {
                compname.setText(compynyname);
            }

            if (gstNo.isEmpty()) {
                gstno.setText("");
            } else {
                gstno.setText(gstNo);
            }
            cance2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            submit3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                    Intent i = new Intent(CheckOutActivity.this, HomeMainActivity.class);
                    startActivity(i);
                }
            });
            submit2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartModels = new ArrayList<>();
                    uname = fstname.getText().toString();
                    emailid = email_id.getText().toString();
                    phonenum = moblie_id.getText().toString();
                    compynyname = compname.getText().toString();
                    gstNo = gstno.getText().toString();
                    if (uname.isEmpty() || uname.equals("")) {
                        fstname.setError("Please Enter Your Name");
                    } else if (emailid.isEmpty() || emailid.equals("")) {
                        email_id.setText("Please Enter Your Email");
                    } else if (phonenum.isEmpty() || phonenum.equals("")) {
                        moblie_id.setText("Please Enter Your Mobile Number");
                    } else {
                        if (helperClass.checkInternetConnection(CheckOutActivity.this)) {
                            Log.e("CheckOutActivity",    authtoken + "," + cartlists.toString() + "," + uname + "," + emailid + "," + phonenum + "," + compynyname + "," + gstNo);

                            try {
                                String checkouApi = ServerURL.BookNow();
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("authToken", authtoken);
                                hashMap.put("name", uname);
                                hashMap.put("email", emailid);
                                hashMap.put("phone", phonenum);
                                hashMap.put("c_name", compynyname);
                                hashMap.put("gst_number", gstNo);
                                hashMap.put("cart-id", cartlists.toString());

//                                final JSONObject mainobject = new JSONObject();
//                                final JSONArray array = new JSONArray();
//
//                                for (int i = 0; i < cartlist.size(); i++) {
//                                    CartModel model = (CartModel) object.get(i);
//                                    Log.e("chaekoutflag", model.getCart_id() + "," + model.getS_flag());
//                                    if (model.getS_flag().equals("0")) {
//                                        final JSONObject object1 = new JSONObject();
//                                        Log.e("prod_id", model.getProduct_id());
//                                        object1.put("prod_id", model.getProduct_id());
//                                        object1.put("chairlist", model.getChairs());
//                                        object1.put("name", model.getProduct_name());
//                                        object1.put("timeslot", model.getSlots());
//                                        object1.put("from_date", model.getFrom_date());
//                                        object1.put("to_date", model.getTo_date());
//                                        object1.put("booking_type", model.getBooking_type());
//                                        object1.put("cart_id", model.getCart_id());
//                                        array.put(object1);
//                                    }
//
//
//                                }
//                                mainobject.put("Data", array);
//                                hashMap.put("data", array.toString());
//                                Log.e("parrot", array.toString());
                                Log.e("CheckOutActivity", checkouApi + "," + authtoken + "," + cartlists.toString() + "," + uname + "," + emailid + "," + phonenum + "," + compynyname + "," + gstNo);
                                NetworkServiceCall serviceCall = new NetworkServiceCall(CheckOutActivity.this, true);
                                serviceCall.setOnServiceCallCompleteListener(new CheckOutActivity.SubmitData());
                                serviceCall.makeJSONObjectPostStringRequest(checkouApi, hashMap, Request.Priority.HIGH);
                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                        } else {
                            Toast.makeText(CheckOutActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();

                        }
//                        if (helperClass.checkInternetConnection(CheckOutActivity.this)) {
//                            ApiSucessResponse apiSucessResponse = (status) -> {
//                                //insertEventDB(name, payload);
//                                if (status.equals("1")) {
//                                    slotpage.setVisibility(View.GONE);
//                                    verifydetails.setVisibility(View.GONE);
//                                    resultpage.setVisibility(View.VISIBLE);
//                                } else {
//                                    Toast.makeText(CheckOutActivity.this, "Please give Proper Details", Toast.LENGTH_SHORT).show();
//                                }
//                                Log.e("printtest", status);
//                            };
//
//                           // dataSync.BookingConfirmdetails(authtoken,cartlist, sdate, edate, seates,btype, uname, emailid, phonenum,compynyname,gstNo, apiSucessResponse);
                        //                        dataSync.chekoutBookingConfirmdetails(authtoken,cartlist, sdate, edate, seates,btype, uname, emailid, phonenum,compynyname,gstNo, apiSucessResponse);
//                        } else {
//                            Toast.makeText(CheckOutActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();
//                        }

                    }

                }
            });


            dialog.show();


        }
    }

    private void InitVIews() {
        userdata = getSharedPreferences("Userdata", MODE_PRIVATE);
        authtoken = userdata.getString("authToken", "");
        helperClass = new HelperClass(this);
        dataSync = new ApiDataSync(this);
        cartlists=new ArrayList<>();
        if (helperClass.checkInternetConnection(CheckOutActivity.this)) {

            cartApicall(authtoken);
        } else {
            Toast.makeText(CheckOutActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();
        }

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        object = (ArrayList<Object>) args.getSerializable("ARRAYLIST");
        Log.e("bookingmodel", String.valueOf(object.size()));


    }

    private void cartApicall(String authtoken) {
        try {
            Log.e("sadsadsaa", "eeee");
            String slideimagesdetails = ServerURL.GetCartData();
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("authToken", authtoken);
            Log.e("CheckoutActivity", authtoken);
            NetworkServiceCall serviceCall = new NetworkServiceCall(CheckOutActivity.this, true);
            serviceCall.setOnServiceCallCompleteListener(new CheckOutActivity.CompleteListener());
            serviceCall.makeJSONObjectPostStringRequest(slideimagesdetails, hashMap, Request.Priority.HIGH);
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
                cartModels = new ArrayList<>();
                Log.e("CheckoutActivity", jsonObject.toString());
                String status = jsonObject.getString("status");
                String errorcode = jsonObject.getString("errorCode");
                String message = jsonObject.getString("message");
                JSONArray array = jsonObject.getJSONArray("Data");
                for (int i = 0; i < array.length(); i++) {

                    Log.e("HomeFragment", array.toString());
                    JSONObject object = array.getJSONObject(i);
                    CartModel model = new CartModel();
                    model.setCart_id(object.getString("cart_id"));
                    model.setProduct_id(object.getString("product_id"));
                    model.setBooking_type(object.getString("booking_type"));
                    model.setProduct_name(object.getString("product_name"));
                    model.setFrom_date(object.getString("from_date"));
                    model.setChairs(object.getString("chairs"));
                    model.setTo_date(object.getString("to_date"));
                    model.setSlots(object.getString("slots"));
                    model.setPrice(object.getString("amount"));
                    cartModels.add(model);

                }
                Checkout_Adapter cart_adapter = new Checkout_Adapter(CheckOutActivity.this, cartModels, cartType);
                LinearLayoutManager layoutManager = new LinearLayoutManager(CheckOutActivity.this);
                biding.languagesRecycle.setAdapter(cart_adapter);
                biding.languagesRecycle.setLayoutManager(layoutManager);

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

    private class SubmitData implements OnServiceCallCompleteListener {
        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            try {
                Log.e("CheckOutActivity", jsonObject.toString());
                String status = jsonObject.getString("status");
                String errorcode = jsonObject.getString("errorCode");
                String message = jsonObject.getString("message");
                if (status.equals("1")) {
                    slotpage.setVisibility(View.GONE);
                    verifydetails.setVisibility(View.GONE);
                    resultpage.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(CheckOutActivity.this, "Please give Proper Details", Toast.LENGTH_SHORT).show();
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