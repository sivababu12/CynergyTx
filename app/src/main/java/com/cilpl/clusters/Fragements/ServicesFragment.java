package com.cilpl.clusters.Fragements;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.cilpl.clusters.Activites.HomeMainActivity;
import com.cilpl.clusters.Activites.Interfaces.ApiSucessResponse;
import com.cilpl.clusters.Activites.LoginActivity;
import com.cilpl.clusters.Activites.SignUpActivity;
import com.cilpl.clusters.Activites.TermsandConditionsActivity;
import com.cilpl.clusters.Activites.ui.home.HomeFragment;
import com.cilpl.clusters.Adapters.TableAdapter;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;
import com.cilpl.clusters.databinding.FragmentSubscriptionBinding;

public class ServicesFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private FragmentSubscriptionBinding binding;
    ArrayAdapter<String> virualplansAdapter;
    private String[] virualplans;
    String v_plan = "", reg_plan = "BASIC", fname, lname, email, mob, n_business, compnyname = "", citys = "";
    ApiDataSync dataSync;
    SharedPreferences userdata;
    boolean cheked;
    HelperClass helperClass;
    EditText fstname, lstname, email_id, moblie_id, city, nature_of_bussiness, compname;
    String filepath, uname = "", emailid = "", authtoken = "", phonenum = "", sdate = "", edate = "", compynyname = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSubscriptionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        inItViews();
        onclick();
        return root;
    }

    private void onclick() {
        binding.startPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    Log.e("clicked","yes");
                uname = userdata.getString("firstname", "");
                emailid = userdata.getString("email", "");
                phonenum = userdata.getString("phne", "");
                compynyname = userdata.getString("compy_name", "");
                authtoken = userdata.getString("authToken", "");
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                dialog.setContentView(R.layout.virtual_popup);
                dialog.setCanceledOnTouchOutside(false);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                //Log.e("clicked","Inside");

                fstname = dialog.findViewById(R.id.ftname);
                TextView terms_conditions = dialog.findViewById(R.id.terms_conditions);
                lstname = dialog.findViewById(R.id.lsname);
                email_id = dialog.findViewById(R.id.email_);
                moblie_id = dialog.findViewById(R.id.phone_id);
                city = dialog.findViewById(R.id.city);
                nature_of_bussiness = dialog.findViewById(R.id.nature_bussiness);
                compname = dialog.findViewById(R.id.cmpyname);
                Button submit = dialog.findViewById(R.id.virtual_Submit);
                Button confirmsumit_btn = dialog.findViewById(R.id.sumit_btn);

                ImageView cancel = dialog.findViewById(R.id.cancel_button);
                CheckBox checkBox = dialog.findViewById(R.id.checkBox);
                LinearLayout virtualslot = dialog.findViewById(R.id.virtualslot);
                LinearLayout confirmslot = dialog.findViewById(R.id.confirmslot);
                ImageView gif_image=dialog.findViewById(R.id.gif_image);

                Glide.with(dialog.getContext())
                        .load(R.drawable.thumsup1)
                        .into(gif_image);
                Spinner virtualspinner = dialog.findViewById(R.id.virtualspinner);
                virualplans = getActivity().getResources().getStringArray(R.array.serviceplans);
                virualplansAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, virualplans);
                virualplansAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                virtualspinner.setAdapter(virualplansAdapter);
                virtualspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View view, int position, long l) {
                        switch (arg0.getId()) {
                            case R.id.virtualspinner:

                                if (virualplans[position].equals("Select Plans")) {

                                } else {
                                    reg_plan = virualplans[position];
                                    //     Log.e("selectedplan", reg_plan);


                                    break;

                                }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                terms_conditions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(), TermsandConditionsActivity.class);
                        i.putExtra("url", "https://cynergytx.com/terms-and-conditons.php");
                        i.putExtra("p_name", "Terms & Conditions");
                        startActivity(i);
                    }
                });
                fstname.setText(uname);
                email_id.setText(emailid);
                moblie_id.setText(phonenum);
                if (compynyname.isEmpty()) {
                    compname.setText("");
                } else {
                    compname.setText(compynyname);
                }

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (checkBox.isChecked()) {
                            submit.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                            cheked = true;
                            //checked
                        } else {
                            cheked = false;
                            submit.setBackground(getResources().getDrawable(R.drawable.btn0_bg));
                            //not checked
                        }

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                confirmsumit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent i = new Intent(getContext(), HomeMainActivity.class);
//                        startActivity(i);
                        dialog.dismiss();
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Validations()) {
                            fname = fstname.getText().toString();
                            lname = lstname.getText().toString();
                            email = email_id.getText().toString();
                            mob = moblie_id.getText().toString();
                            compynyname=compname.getText().toString();
                              Log.e("ServiceRequest",fname+","+lname+","+mob+","+email+","+citys+","+n_business+","+compynyname+","+reg_plan+","+cheked);
                            if (helperClass.checkInternetConnection(getContext())) {
                                ApiSucessResponse apiSucessResponse = (status) -> {
                                    //insertEventDB(name, payload);
                                    if (status.equals("1")) {
                                        virtualslot.setVisibility(View.GONE);
                                        confirmslot.setVisibility(View.VISIBLE);

                                    } else {
                                        Toast.makeText(getContext(), "Please give Proper Details", Toast.LENGTH_SHORT).show();
                                    }
                                    //  Log.e("printtest", status);
                                };

                                dataSync.ServiceRequest(authtoken, fname + lname, email, phonenum, compynyname, n_business, reg_plan, citys, apiSucessResponse);
                            } else {
                                Toast.makeText(getContext(), R.string.noInternet, Toast.LENGTH_SHORT).show();
                            }


                            //  dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }


        });

    }

    private boolean Validations() {
        fname = fstname.getText().toString();
        lname = lstname.getText().toString();
        email = email_id.getText().toString();
        mob = moblie_id.getText().toString();
        compnyname = compname.getText().toString();
        n_business = nature_of_bussiness.getText().toString();
        citys = city.getText().toString();

        if (fname.isEmpty() || fname.equals(" ")) {
            fstname.setError("Enter Your First Name");
            return false;
        }
        if (lname.isEmpty() || lname.equals(" ")) {
            lstname.setError("Enter Your Last Name");
            return false;
        }
        if (mob.isEmpty() || mob.equals(" ") || mob.length() != 10) {
            moblie_id.setError("Enter Your Mobile Number");
            return false;
        }
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
        if (citys.isEmpty() || citys.equals(" ")) {
            city.setError("Enter Current  City");
            return false;
        }
        if (n_business.isEmpty() || n_business.equals(" ")) {
            nature_of_bussiness.setError("Enter Your Nature of Business");
            return false;
        }
        if (compnyname.isEmpty() || compnyname.equals(" ")) {
            compname.setError("Enter Your Company Name");
            return false;
        }
        if (reg_plan.equals("") || reg_plan.equals("Select Plans") || reg_plan.isEmpty()) {
            Toast.makeText(getContext(), "Please Select Your  Plan", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cheked == false) {
            Toast.makeText(getContext(), "Please accept Terms and Conditions", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void inItViews() {
        userdata = getContext().getSharedPreferences("Userdata", getContext().MODE_PRIVATE);
        helperClass = new HelperClass(getContext());
        dataSync = new ApiDataSync(getContext());
        HomeMainActivity.hometitle.setText(R.string.services);
        virualplans = getActivity().getResources().getStringArray(R.array.virualplans);
        virualplansAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, virualplans);
        virualplansAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner1.setAdapter(virualplansAdapter);
        binding.spinner1.setOnItemSelectedListener(this);
        TableAdapter adapter = new TableAdapter(getActivity());
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 4, GridLayoutManager.HORIZONTAL, false);
        binding.tableRecycler.setLayoutManager(layoutManager);
        binding.tableRecycler.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View view, int position, long l) {
        switch (arg0.getId()) {
            case R.id.spinner1:

                if (virualplans[position].equals("Select Plans")) {

                } else {
                    v_plan = virualplans[position];
                    //   Log.e("selectedplan", v_plan);
                    switch (v_plan) {
                        case "Virtual Office Basic (INR 999)":
                            binding.basic.setVisibility(View.VISIBLE);
                            binding.gold.setVisibility(View.GONE);
                            binding.platinum.setVisibility(View.GONE);
                            break;
                        case "Virtual Office Gold (INR 2199)":
                            binding.basic.setVisibility(View.GONE);
                            binding.gold.setVisibility(View.VISIBLE);
                            binding.platinum.setVisibility(View.GONE);
                            break;
                        case "Virtual Office Platinum (INR 3999)":
                            binding.basic.setVisibility(View.GONE);
                            binding.gold.setVisibility(View.GONE);
                            binding.platinum.setVisibility(View.VISIBLE);
                            break;

                    }


                    break;

                }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}