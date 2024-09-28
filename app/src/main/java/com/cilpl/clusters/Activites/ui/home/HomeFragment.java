package com.cilpl.clusters.Activites.ui.home;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.cilpl.clusters.Activites.HomeMainActivity;
import com.cilpl.clusters.Adapters.Home_Adapter;
import com.cilpl.clusters.Model.DaysModel;
import com.cilpl.clusters.Model.HomeModel;
import com.cilpl.clusters.NetworkCalling.AppUtil;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.NetworkCalling.NetworkServiceCall;
import com.cilpl.clusters.NetworkCalling.OnServiceCallCompleteListener;
import com.cilpl.clusters.NetworkCalling.ServerURL;
import com.cilpl.clusters.R;
import com.cilpl.clusters.databinding.FragmentHomeBinding;
import com.codebyashish.autoimageslider.Interfaces.ItemsListener;
import com.codebyashish.autoimageslider.Models.ImageSlidesModel;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.moeidbannerlibrary.banner.BaseBannerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    ImageSlider imageSlider;
    ArrayList<SlideModel> slideModels;
    private int mYear, mMonth, mDay;
    String startdate = "", enddate = "", authtoken = "", c_startdate = "", c_enddate = "";
    private FragmentHomeBinding binding;
    HelperClass helperClass;
    AlertDialog alertDialog1;
    private ArrayList<HomeModel> homeModels;
    private ArrayList<DaysModel> daysModels;
    SharedPreferences userdata;
    private String[] bookingarray;
    private String[] users;
    List<String> urls;
    BaseBannerAdapter webBannerAdapter;
    String bookingoption = "", cluster = "";
    ArrayAdapter<String> clusteradapter;
    ArrayAdapter<String> bookingadapter;
    private ItemsListener listener;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        imageSlider = root.findViewById(R.id.image_slider);


        inItViews();
        return root;
    }

    private void inItViews() {
        userdata = getContext().getSharedPreferences("Userdata", getContext().MODE_PRIVATE);
        authtoken = userdata.getString("authToken", "");
        helperClass = new HelperClass(getContext());
        bookingarray = getActivity().getResources().getStringArray(R.array.bookingOptions);
        users = getActivity().getResources().getStringArray(R.array.clusters);
        HomeMainActivity.hometitle.setText(R.string.app_name);


        if (helperClass.checkInternetConnection(getContext())) {
            ImagSliderApicall();
        } else {
            Toast.makeText(getContext(), R.string.noInternet, Toast.LENGTH_SHORT).show();
        }
        if (helperClass.checkInternetConnection(getContext())) {

            HomeApicall(authtoken);
        } else {
            Toast.makeText(getContext(), R.string.noInternet, Toast.LENGTH_SHORT).show();
        }

        clusteradapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, users);
        clusteradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner1.setAdapter(clusteradapter);
        binding.spinner1.setOnItemSelectedListener(this);

        bookingadapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, bookingarray);
        bookingadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.bookingspinner.setAdapter(bookingadapter);
        binding.bookingspinner.setOnItemSelectedListener(this);

        binding.startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        startdate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        binding.startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(c.getTimeInMillis());
                dpd.show();
            }

        });

        binding.startdate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        c_startdate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        binding.startdate1.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(c.getTimeInMillis());
                dpd.show();

            }
        });


//        binding.bookingselection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//
//                builder.setTitle(R.string.selectchoice_txt);
//                builder.setSingleChoiceItems(bookingarray, -1, new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int item) {
//
//                        switch (item) {
//                            case 0:
//                                Log.e("genderselected","male");
//                               // Toast.makeText(SignUpActivity.this, R.string.male_txt, Toast.LENGTH_SHORT).show();
//                                break;
//                            case 1:
//                                Log.e("genderselected","Female");
//                               // Toast.makeText(SignUpActivity.this, R.string.female_txt, Toast.LENGTH_SHORT).show();
//                                break;
//                            case 2:
//                                Log.e("genderselected","Female");
//                                // Toast.makeText(SignUpActivity.this, R.string.female_txt, Toast.LENGTH_SHORT).show();
//                                break;
//                            case 3:
//                                Log.e("genderselected","Female");
//                                // Toast.makeText(SignUpActivity.this, R.string.female_txt, Toast.LENGTH_SHORT).show();
//                                break;
//
//
//
//
//                        }
//                        alertDialog1.dismiss();
////                SharedPreferences.Editor editor = userdata.edit();
////                gender = String.valueOf(genderarray[item]);
////                editor.putString("gender", gender);
////                editor.apply();
//                        bookingoption=bookingarray[item];
//                        Log.e("homefragment",bookingoption);
//                        binding.bookingselection.setText(bookingarray[item]);
//
//                    }
//                });
//                alertDialog1 = builder.create();
//                alertDialog1.show();
//
//            }
//        });
        binding.enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        enddate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        binding.enddate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(c.getTimeInMillis());
                dpd.show();
            }
        });

        binding.enddate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        c_enddate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        binding.enddate1.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(c.getTimeInMillis());
                dpd.show();
            }
        });
        binding.searchhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bookingoption.equals("") || bookingoption.isEmpty() || bookingoption.equals("Select Item")) {
                    Toast.makeText(getContext(), "Please Select Your Option", Toast.LENGTH_SHORT).show();
                } else if (!startdate.isEmpty() || !enddate.isEmpty()) {
                    if (helperClass.checkInternetConnection(getContext())) {
                       // Log.e("homefragment", startdate + "," + enddate);
                        HomeSearchcall(authtoken, startdate, enddate, bookingoption);
                    } else {
                        Toast.makeText(getContext(), R.string.noInternet, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Select Start Date and End Date", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.searchcluster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cluster.equals("") || cluster.isEmpty() || cluster.equals("Select Item")) {
                    Toast.makeText(getContext(), "Please Select Your Option", Toast.LENGTH_SHORT).show();
                } else if (!c_startdate.isEmpty() || !c_enddate.isEmpty()) {
                    if (helperClass.checkInternetConnection(getContext())) {
                      //  Log.e("homefragment", c_startdate + "," + c_enddate);
                        HomeSearchcall(authtoken, c_startdate, c_enddate, cluster);
                    } else {
                        Toast.makeText(getContext(), R.string.noInternet, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Select Start Date and End Date", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {


        switch (arg0.getId()) {
            case R.id.spinner1:

                if (users[position].equals("Select Item")) {

                } else {
                    cluster = users[position];
                   // Toast.makeText(getContext(), "Selected User: " + users[position], Toast.LENGTH_SHORT).show();
                }

                //Your Action Here.
                break;
            case R.id.bookingspinner:
                //Your Another Action Here.

                if (bookingarray[position].equals("Select Item")) {

                } else {
                    bookingoption = bookingarray[position];
                  //  Toast.makeText(getContext(), "Selected User: " + bookingarray[position], Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
    }

    private void HomeSearchcall(String authtoken, String startdate, String enddate, String bookingtype) {
        try {
            String slideimagesdetails = ServerURL.GetSearch();
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("authToken", authtoken);
            hashMap.put("from", startdate);
            hashMap.put("to", enddate);
            hashMap.put("booking_type", bookingtype);
          //  Log.e("HomeFragment", slideimagesdetails + "," + authtoken + "," + startdate + "," + enddate + "," + bookingtype);
            NetworkServiceCall serviceCall = new NetworkServiceCall(getContext(), true);
            serviceCall.setOnServiceCallCompleteListener(new HomeCompleteListener());
            serviceCall.makeJSONObjectPostStringRequest(slideimagesdetails, hashMap, Request.Priority.HIGH);
            // delete total files in folder
            //dletefiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void HomeApicall(String authtoken) {
        try {
            String slideimagesdetails = ServerURL.GetHome();
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("authToken", authtoken);
           // Log.e("HomeFragment", slideimagesdetails+","+authtoken);
            NetworkServiceCall serviceCall = new NetworkServiceCall(getContext(), true);
            serviceCall.setOnServiceCallCompleteListener(new HomeCompleteListener());
            serviceCall.makeJSONObjectPostStringRequest(slideimagesdetails, hashMap, Request.Priority.HIGH);
            // delete total files in folder
            //dletefiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ImagSliderApicall() {
        try {
            String slideimagesdetails = ServerURL.GetHomeSliders();
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("authToken", authtoken);
           // Log.e("HomeFragment", authtoken);
            NetworkServiceCall serviceCall = new NetworkServiceCall(getContext(), true);
            serviceCall.setOnServiceCallCompleteListener(new SliderCompleteListener());
            serviceCall.makeJSONObjectPostStringRequest(slideimagesdetails, hashMap, Request.Priority.HIGH);
            // delete total files in folder
            //dletefiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class SliderCompleteListener implements OnServiceCallCompleteListener {
        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            try {
                slideModels = new ArrayList<>();

                Log.e("HomeFragment", jsonObject.toString());
                String status = jsonObject.getString("status");
                String errorcode = jsonObject.getString("errorCode");
                String message = jsonObject.getString("message");
                if(status.equals("1")){
                    binding.homeslider.setVisibility(View.VISIBLE);
                    String groups_filePath = jsonObject.getString("filePath");
                    JSONArray array = jsonObject.getJSONArray("Data");
                    for (int i = 0; i < array.length(); i++) {
                      //  Log.e("HomeFragment", array.toString());
                        JSONObject object = array.getJSONObject(i);
                        String imagid = object.getString("id");
                        String image = object.getString("image");
                       // Log.e("homeimages", groups_filePath + image);
                        slideModels.add(new SlideModel(groups_filePath + image, ScaleTypes.FIT));
                    }

                    binding.imageSlider.setImageList(slideModels, ScaleTypes.FIT);
                }else {
                    binding.homeslider.setVisibility(View.GONE);
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

    private class HomeCompleteListener implements OnServiceCallCompleteListener {
        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            try {
                homeModels = new ArrayList<>();
                daysModels = new ArrayList<>();
                Log.e("HomeFragment", jsonObject.toString());
                String status = jsonObject.getString("status");
                String errorcode = jsonObject.getString("errorCode");
                String message = jsonObject.getString("message");





                if(status.equals("0")){
                    binding.errormessage.setVisibility(View.VISIBLE);
                    binding.homeRecycler.setVisibility(View.GONE);
                    HomeMainActivity.cartcount.setVisibility(View.GONE);
                }else {
                    binding.errormessage.setVisibility(View.GONE);
                    binding.homeRecycler.setVisibility(View.VISIBLE);

                    String groups_filePath = jsonObject.getString("filePath");
                    String count = jsonObject.getString("Count");
                    SharedPreferences.Editor editor = userdata.edit();
                    editor.putString("c_count", count);
                    editor.putString("homeimagepath", groups_filePath);
                    editor.apply();
                    if(count.equals("0")){
                        HomeMainActivity.cartcount.setVisibility(View.GONE);
                    }else {
                        HomeMainActivity.cartcount.setVisibility(View.VISIBLE);
                        HomeMainActivity.cartcount.setText(count);
                    }
                    JSONArray array = jsonObject.getJSONArray("Data");
                    for (int i = 0; i < array.length(); i++) {
                      //  Log.e("HomeFragment", array.toString());
                        JSONObject object = array.getJSONObject(i);
                        HomeModel model = new HomeModel();
                        model.setId(object.getString("id"));
                        model.setName(object.getString("name"));
                        model.setImages(object.getString("images"));
                        model.setRoom_features(object.getString("room_features"));
                        model.setSeats_capacity(object.getString("seats_capacity"));
                        model.setBookingtype(object.getString("provision_type"));
                        model.setPrice(object.getString("amount"));
                        model.setManager_cabins(object.getString("manager_cabins"));
                        model.setAminities(object.getString("aminities"));
                        model.setConf_room(object.getString("conf_room"));
                        model.setWork_stations(object.getString("work_stations"));
                        model.setSize(object.getString("size"));
                        homeModels.add(model);

                        JSONArray array1 = object.getJSONArray("availability");
                        for (int j = 0; j < array1.length(); j++) {
                           // Log.e("HomeFragment1", array1.toString());
                            JSONObject object1 = array1.getJSONObject(j);
                            DaysModel daysModel = new DaysModel();
                            daysModel.setSun(object1.getString("SU"));
                            daysModel.setMon(object1.getString("M"));
                            daysModel.setTue(object1.getString("T"));
                            daysModel.setWed(object1.getString("W"));
                            daysModel.setThur(object1.getString("TH"));
                            daysModel.setFri(object1.getString("F"));
                            daysModel.setSat(object1.getString("S"));
                            daysModels.add(daysModel);
                        }
                    }
                    Home_Adapter adapter = new Home_Adapter(getActivity(), homeModels, daysModels);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    binding.homeRecycler.setLayoutManager(layoutManager);
                    binding.homeRecycler.setAdapter(adapter);
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