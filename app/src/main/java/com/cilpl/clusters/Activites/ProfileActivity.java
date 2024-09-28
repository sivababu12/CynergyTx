package com.cilpl.clusters.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cilpl.clusters.Activites.Interfaces.ApiSucessResponse;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.NetworkCalling.NetworkServiceCall;
import com.cilpl.clusters.NetworkCalling.OnServiceCallCompleteListener;
import com.cilpl.clusters.NetworkCalling.ServerURL;
import com.cilpl.clusters.NetworkCalling.VolleyMultipartRequest;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;
import com.cilpl.clusters.databinding.ActivityEventsBinding;
import com.cilpl.clusters.databinding.ActivityProfileBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
  private ActivityProfileBinding binding;
    private String[] genderarray,cameraOptions;
    AlertDialog alertDialog1;
    Bitmap bitmap;
    long f_length;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS= 1;
    String authtoken, dataofbirth,gender,f_name,l_name,email
            ,phone,com_name,gstno,city,state,country,pincode,base64String
            ,img_stats = "0",image123="";
    DatePickerDialog datePickerDialog;
    SharedPreferences userdata;
    HelperClass helperClass;
    ApiDataSync dataSync;
    ApiSucessResponse apiSucessResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        InitViews();
        onClicks();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ProfileActivity.this,HomeMainActivity.class);
        startActivity(i);
    }

    private void onClicks() {
        f_name=binding.fstname.getText().toString();

        binding.genderTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenderRadioButtonGroup();
            }
        });
        binding.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (helperClass.checkInternetConnection(ProfileActivity.this)) {
                    if(checkAndRequestPermissions()){
                        selectimage();
                    }
                }else {
                    Toast.makeText(ProfileActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();

                }

            }
        });
        binding.changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this,ChangePassordActivity.class);
                startActivity(i);
            }
        });

        binding.dobTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, monthOfYear, dayOfMonth);
                                Date current = newDate.getTime();
                                int diff1 = new Date().compareTo(current);
                                if (diff1 < 0) {
                                    // acc_dob.setError("Select Valid Date");
                                    Toast.makeText(ProfileActivity.this, "Please Select a valid date", Toast.LENGTH_LONG).show();

                                } else {
                                    // set day of month , month and year value in the edit text
                                    binding.dobTxt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    dataofbirth = binding.dobTxt.getText().toString();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        binding.signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (helperClass.checkInternetConnection(ProfileActivity.this)) {
                    SubmitDetails();
                }else {
                    Toast.makeText(ProfileActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();

                }

            }
        });
        binding.backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void SubmitDetails() {
        f_name=binding.fstname.getText().toString();
        l_name = binding.lname.getText().toString();
        email = binding.emailId.getText().toString();
        phone = binding.moblieId.getText().toString();
        com_name = binding.cname.getText().toString();
        gstno = binding.gstnum.getText().toString();
        city = binding.city.getText().toString();
        state = binding.state.getText().toString();
        dataofbirth = binding.dobTxt.getText().toString();
        ApiSucessResponse apiSucessResponse = (status) -> {
            //insertEventDB(name, payload);
            if(status.equals("1")){
                Intent i = new Intent(ProfileActivity.this,HomeMainActivity.class);
                startActivity(i);
            }else {
                Toast.makeText(ProfileActivity.this, "Please give Proper Details", Toast.LENGTH_SHORT).show();
            }
           // Log.e("printtest",status);
        };

        dataSync.UpdateProfile(authtoken,f_name,l_name,email,phone,gender,dataofbirth,com_name,gstno,city,state,apiSucessResponse);
     //  Log.e("profileupdate",f_name+","+l_name+","+email+","+gender+","+dataofbirth+","+com_name+","+gstno+","+city+","+state+",");

    }

    private void GenderRadioButtonGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.selectchoice_txt);
        builder.setSingleChoiceItems(genderarray, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                       // Log.e("genderselected","male");
                     //   Toast.makeText(ProfileActivity.this, R.string.male_txt, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                      //  Log.e("genderselected","Female");
                       // Toast.makeText(ProfileActivity.this, R.string.female_txt, Toast.LENGTH_SHORT).show();
                        break;


                }
                alertDialog1.dismiss();
//                SharedPreferences.Editor editor = userdata.edit();
//                gender = String.valueOf(genderarray[item]);
//                editor.putString("gender", gender);
//                editor.apply();
                gender=genderarray[item];
                binding.genderTxt.setText(genderarray[item]);

            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

        binding.backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this,HomeMainActivity.class);
                startActivity(i);
            }
        });
    }

    private void InitViews() {
        userdata = getSharedPreferences("Userdata", MODE_PRIVATE);
        authtoken = userdata.getString("authToken", "");
        helperClass=new HelperClass(this);
        dataSync=new ApiDataSync(this);
        genderarray=ProfileActivity.this.getResources().getStringArray(R.array.genderitems);
        cameraOptions = ProfileActivity.this.getResources().getStringArray(R.array.cameraOptions);

        if (helperClass.checkInternetConnection(ProfileActivity.this)) {
            GetListDetails();
        }else {
            Toast.makeText(ProfileActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();

        }
    }

    private void GetListDetails() {
        try {
            String confirmpass = ServerURL.GetProfile();
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("authToken", authtoken);

          //  Log.e("getprofile", confirmpass+","+authtoken);
            NetworkServiceCall serviceCall = new NetworkServiceCall(this, true);
            serviceCall.setOnServiceCallCompleteListener(new GetprofileCallCompleteListener());
            serviceCall.makeJSONObjectPostStringRequest(confirmpass, hashMap, Request.Priority.HIGH);
            // delete total files in folder
            //dletefiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(ProfileActivity.this,
                Manifest.permission.CAMERA);
        int wtite = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(ProfileActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);



        }


        return true;
    }
    private void selectimage() {

                final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle(R.string.choicepfimage_txt);


        builder.setItems(cameraOptions, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (cameraOptions[item].equals(cameraOptions[0])) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (cameraOptions[item].equals(cameraOptions[1])) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (cameraOptions[item].equals("Delete Photo")) {

                    Glide.with(ProfileActivity.this)
                            .load(R.drawable.tempimg)
                            .fitCenter()
                            .error(R.drawable.tempimg)
                            .into(binding.accImg);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tempimg);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                    byte[] imageBytes = baos.toByteArray();
                    base64String = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                   // Log.e("ssssssssss", base64String);
                    img_stats = "1";
                  //  ProfileImageDbase(authcode, base64String, img_stats);
                    dialog.dismiss();
                } else if (cameraOptions[item].equals(cameraOptions[2])) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {


                        bitmap = (Bitmap) data.getExtras().get("data");
//                        profile_img.setImageBitmap(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                        byte[] imageBytes = baos.toByteArray();
                        f_length = imageBytes.length;
                       // Log.e("sizeofimage", String.valueOf(f_length));
                        if (f_length > 15728640) {
                           // Toast.makeText(this, R.string.imageflesize_txt, Toast.LENGTH_SHORT).show();
                        } else {


                            base64String = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                            img_stats = "1";

                            Glide.with(this)
                                    .load(bitmap)
                                    .into(binding.accImg);
                            image123 = String.valueOf(bitmap);
                            imageUpload(image123,authtoken);
                            SharedPreferences.Editor editor = userdata.edit();
                            editor.putString("profileimage", image123);
                            editor.apply();
                        }
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                            byte[] imageBytes = baos.toByteArray();
                            f_length = imageBytes.length;
                         //   Log.e("sizeofimage", String.valueOf(f_length));
                            if (f_length > 15728640) {
                              // Toast.makeText(this, R.string.imageflesize_txt, Toast.LENGTH_SHORT).show();
                            } else {


                                base64String = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                img_stats = "1";
                              //  ProfileImageDbase(authocode, base64String, img_stats);

                                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                                if (selectedImage != null) {
                                    Cursor cursor = getContentResolver().query(selectedImage,
                                            filePathColumn, null, null, null);
                                    if (cursor != null) {
                                        cursor.moveToFirst();

                                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                        String picturePath = cursor.getString(columnIndex);
                                        //    profile_img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                        Glide.with(this)
                                                .load(picturePath)
                                                .into(binding.accImg);


                                        image123 = String.valueOf(picturePath);
                                        imageUpload(image123,authtoken);

                                        SharedPreferences.Editor editor = userdata.edit();
                                        editor.putString("profileimage", image123);
                                        editor.apply();
                                        cursor.close();
                                    }
                                }

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
            }
        }
    }
    private void imageUpload(String picturePath, String authcode) {
        try {

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ServerURL.AddImage(),
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            try {
                                JSONObject obj = new JSONObject(new String(response.data));
                                String status = obj.getString("status");
                                String errorCode = obj.getString("errorCode");
                                String message = obj.getString("message");
                             //   Log.e("changeimage", "" + status + "," + errorCode + "," + message);
                                if (status.equals("1")) {


//                                    Toast.makeText(AccountSettings_Activity.this, message, Toast.LENGTH_SHORT).show();
                                } else {
//                                    Toast.makeText(AccountSettings_Activity.this, message, Toast.LENGTH_SHORT).show();
                                }


                                //   Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        //    Log.e("GotError", "" + error.getMessage());
                            // multipartrequest(name, filesize, Outfilename);
                        }
                    }) {


                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    String imaname = ".jpeg";
                    params.put("image", new DataPart(imaname, getFileDataFromDrawable(bitmap)));

                    return params;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("authToken", authcode);



                    return params;


                }
            };
            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            //adding the request to volley
            Volley.newRequestQueue(this).add(volleyMultipartRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        long length = imageInByte.length;
      //  Log.e("sizeofimage", String.valueOf(length));
        return byteArrayOutputStream.toByteArray();
    }
    private class GetprofileCallCompleteListener implements OnServiceCallCompleteListener {
        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            try {
             //   Log.e("signupresponse", jsonObject.toString());
                String status = jsonObject.getString("status");
                String errorcode = jsonObject.getString("errorCode");
                String message = jsonObject.getString("message");
                String imagePath = jsonObject.getString("imagePath");
                if (status.equals("1")) {
                    JSONObject object=jsonObject.getJSONObject("data");
                   f_name = object.getString("first_name");
                    l_name = object.getString("last_name");
                    email = object.getString("email");
                    phone = object.getString("phone");
                    com_name = object.getString("company_name");
                    gstno = object.getString("gst_number");
                    city = object.getString("city");
                    country = object.getString("country");
                    state = object.getString("state");
                    pincode = object.getString("pin_code");
                    gender = object.getString("gender");
                    dataofbirth = object.getString("dob");
                   String photo = object.getString("photo");
                    SharedPreferences.Editor editor = userdata.edit();
                    editor.putString("firstname", f_name);
                    editor.putString("email", email);
                    editor.putString("phne", phone);
                    editor.putString("profileimage", imagePath+photo);
                    editor.apply();
                    Glide.with(ProfileActivity.this)
                            .load(imagePath+photo)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                                         binding.progress.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                                            binding.progress.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                            .into(binding.accImg);

                    binding.fstname.setText(f_name);
                    binding.lname.setText(l_name);
                    binding.dobTxt.setText(dataofbirth);
                    binding.emailId.setText(email);
                    if(city.equals("")||city.equals("0")){
                        binding.city.setText(" ");
                    }else {
                        binding.city.setText(city);
                    }
                    if(state.equals("")||state.equals("0")){
                        binding.state.setText(" ");
                    }else {
                        binding.state.setText(state);
                    }
                    binding.cname.setText(com_name);
                    binding.gstnum.setText(gstno);

                    binding.genderTxt.setText(gender);
                    binding.moblieId.setText(phone);



                }


            } catch (Exception e) {
                e.printStackTrace();
            }
           // Log.e("firebaseupload", "uploaded");

        }

        @Override
        public void onJSONArrayResponse(JSONArray jsonArray) {

        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }
    }
}