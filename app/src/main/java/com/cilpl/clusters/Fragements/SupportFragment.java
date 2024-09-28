package com.cilpl.clusters.Fragements;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cilpl.clusters.Activites.HomeMainActivity;
import com.cilpl.clusters.Activites.Interfaces.ApiSucessResponse;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;
import com.cilpl.clusters.databinding.FragmentLibraryBinding;


public class SupportFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private FragmentLibraryBinding binding;
    ArrayAdapter<String> supportadapter;
    String supportoption = "", uname, email, phone, message,  authtoken, username, e_mail,mobile_num;
    private String[] supportarray;
    ApiSucessResponse apiSucessResponse;
    ApiDataSync dataSync;
    HelperClass helperClass;
    SharedPreferences userdata;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        inItViews();
        onclick();
        return root;
    }

    private void onclick() {
        binding.supportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validations()) {
                  //  Log.e("supportfragment", uname + "," + email + "," + phone + "," + supportoption + "," + message);

                    if (helperClass.checkInternetConnection(getContext())) {
                        ApiSucessResponse apiSucessResponse = (status) -> {
                            //insertEventDB(name, payload);
                            if (status.equals("1")) {

                               // Toast.makeText(getContext(), "Request Added SucessFully!", Toast.LENGTH_SHORT).show();


                               popup();

                            } else {
                                Toast.makeText(getContext(), "Please try Again", Toast.LENGTH_SHORT).show();
                            }

                        };
                        dataSync.Supportdetails(uname, email, phone, supportoption, message, apiSucessResponse);


                    }
                }
            }
        });
    }

    private void refreshPage() {
        binding.fstname.setText("");
        binding.emailId.setText("");
        binding.moblieId.setText("");
        binding.messageEt.setText("");
        binding.supportspinner.setAdapter(supportadapter);
        binding.errormessage.setVisibility(View.GONE);

           

    }

    private void popup() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.virtual_popup);
        dialog.setCanceledOnTouchOutside(false);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
      //  Log.e("clicked","Inside");
        LinearLayout virtualslot=dialog.findViewById(R.id.virtualslot);
        LinearLayout confirmslot=dialog.findViewById(R.id.confirmslot);
        Button confirmsumit_btn=dialog.findViewById(R.id.sumit_btn);
        virtualslot.setVisibility(View.GONE);
        confirmslot.setVisibility(View.VISIBLE);
        confirmsumit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        Intent i = new Intent(getContext(), HomeMainActivity.class);
                        startActivity(i);
              //  refreshPage();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private boolean Validations() {
        uname = binding.fstname.getText().toString();
        email = binding.emailId.getText().toString();
        phone = binding.moblieId.getText().toString();
        message = binding.messageEt.getText().toString();

        if (uname.isEmpty() || uname.equals(" ")) {
            binding.fstname.setError("Enter Your Name");
            return false;
        }
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
        if (phone.isEmpty() || phone.equals(" ") || phone.length() != 10) {
            binding.moblieId.setError("Enter Your Mobile Number");
            return false;
        }
        if (supportoption.isEmpty() || supportoption.equals(" ") || supportoption.equals("Related To")) {
            binding.errormessage.setVisibility(View.VISIBLE);
//            Toast.makeText(getContext(), "Please Select Any Option", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (message.isEmpty() || message.equals(" ")) {
            binding.messageEt.setError("Enter Your Query");
            return false;
        }
        return true;
    }

    private void inItViews() {
        userdata = getContext().getSharedPreferences("Userdata", getContext().MODE_PRIVATE);
        authtoken = userdata.getString("authToken", "");
        username = userdata.getString("firstname", "");
        e_mail = userdata.getString("email", "");
        mobile_num = userdata.getString("phne", "");
        binding.fstname.setText(username);
        binding.emailId.setText(e_mail);
        binding.moblieId.setText(mobile_num);
        HomeMainActivity.hometitle.setText(R.string.support);
        dataSync = new ApiDataSync(getContext());
        helperClass = new HelperClass(getContext());
        supportarray = getActivity().getResources().getStringArray(R.array.supportoptions);
        supportadapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, supportarray);
        supportadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.supportspinner.setAdapter(supportadapter);
        binding.supportspinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View view, int position, long l) {
        switch (arg0.getId()) {
            case R.id.supportspinner:

                if (supportarray[position].equals("Related To")) {
//                    binding.errormessage.setVisibility(View.VISIBLE);
                } else {
                    supportoption = supportarray[position];
//                    binding.errormessage.setVisibility(View.GONE);
                    // Toast.makeText(getContext(), "Selected User: " + users[position], Toast.LENGTH_SHORT).show();
                }

                //Your Action Here.
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}