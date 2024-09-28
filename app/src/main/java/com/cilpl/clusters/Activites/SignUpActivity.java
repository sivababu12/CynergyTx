package com.cilpl.clusters.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.cilpl.clusters.Activites.Interfaces.CarteResponse;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.NetworkCalling.OnServiceCallCompleteListener;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    Button signinbtn;
    Bitmap bitmap, bitmap2;
    long f_length;
    CheckBox checkBox;
    TextView filename_1, filename_2, terms_conditions, signinPage, lstname, email_id, moblie_id, pass1_id, pass2_id, cname, gstnum, fstname, dob_txt, gender_txt;
    boolean cheked;
    ImageView aadharimg, panimg;
    ApiDataSync dataSync;
    Uri imageurl;
    HelperClass helperClass;
    SharedPreferences userdata;
    private String[] genderarray, cameraOptions;
    AlertDialog alertDialog1;
    DatePickerDialog datePickerDialog;
    String aadhar_stats = "0", aadhar_img = "", pan_States = "0", pan_img = "", adhardoc = "", pandoc = "";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String fname, lname, email, mob, pass1, pass2, compnyname = "", gstNo = "", deviceid = "", device_type = "android", ipaddress = "", gender = "", dataofbirth = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        InitViews();
        onClicks();
    }

    private void onClicks() {
        signinPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);


            }
        });
        terms_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this, TermsandConditionsActivity.class);
                i.putExtra("url", "https://cynergytx.com/terms-and-conditons.php");
                i.putExtra("p_name", "Terms & Conditions");
                startActivity(i);
            }
        });
        aadharimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    selectimage(0, 1, 111);
                }
            }
        });
        panimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    selectimage(2, 3, 222);
                }
            }
        });
        dob_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        Date current = newDate.getTime();
                        int diff1 = new Date().compareTo(current);
                        if (diff1 < 0) {
                            // acc_dob.setError("Select Valid Date");
                            Toast.makeText(SignUpActivity.this, "Please Select a valid date", Toast.LENGTH_LONG).show();

                        } else {
                            // set day of month , month and year value in the edit text
                            dob_txt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            dataofbirth = dob_txt.getText().toString();
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });
        gender_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenderRadioButtonGroup();
            }
        });
        signinbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (Validations()) {
                    {
                        compnyname = cname.getText().toString();
                        gstNo = gstnum.getText().toString();

                        Log.e("signupparams", fname + "," + lname + "," + email + "," + mob + "," + compnyname + "," + gstNo + "," + pass1 + "," + pass2 + "," + deviceid + "," + device_type + "," + ipaddress);
                        // signinbtn.setClickable(false);
                        if (helperClass.checkInternetConnection(getApplicationContext())) {
                            CarteResponse apiSucessResponse = (status, messge) -> {
                                //insertEventDB(name, payload);
                                if (status.equals("1")) {
                                    Intent i = new Intent(SignUpActivity.this, OTPActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Toast toast = Toast.makeText(SignUpActivity.this, Html.fromHtml("<font color='#F44336' ><b>" + messge + "</b></font>"), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.TOP, 0, 0);
                                    toast.show();
//                                    Toast.makeText(SignUpActivity.this, messge, Toast.LENGTH_SHORT).show();
                                }
                                Log.e("printtest", status);
                            };
                            dataSync.SendStatus(fname, lname, email, mob, compnyname, gstNo, pass1, pass2, deviceid, device_type, ipaddress, apiSucessResponse, gender, dataofbirth, bitmap, bitmap2, aadhar_stats, pan_States, adhardoc, pandoc);
//                            try {
//                                Log.e("sadsadsaa", "eeee");
//
//                                String regdetails = ServerURL.userregsiration();
//                                HashMap<String, String> hashMap = new HashMap<>();
//                                hashMap.put("first_name", fname);
//                                hashMap.put("last_name", lname);
//                                hashMap.put("email", email);
//                                hashMap.put("phone", mob);
//                                hashMap.put("company_name", compnyname);
//                                hashMap.put("gst_number", gstNo);
//                                hashMap.put("password", pass1);
//                                hashMap.put("confirm_password", pass2);
//                                hashMap.put("deviceId", deviceid);
//                                hashMap.put("deviceType", device_type);
//                                hashMap.put("ip", ipaddress);
//                                hashMap.put("terms_check", "1");
//                                // hashMap.put("file_type", fileformat);
//                                Log.e("signupresponse",fname+","+lname+","+email+","+mob+","+compnyname+","+gstNo+","+pass1+","+pass2+","+deviceid+","+device_type+","+ipaddress);
//
//                                //    Log.e("Notificationststus", authcode + "," + fcmid + "," + status+","+uid);
//                                NetworkServiceCall serviceCall = new NetworkServiceCall(SignUpActivity.this, true);
//                                serviceCall.setOnServiceCallCompleteListener(new SignUpActivity.neServiceCallCompleteListener());
//                                serviceCall.makeJSONObjectPostStringRequest(regdetails, hashMap, Request.Priority.HIGH);
//                                // delete total files in folder
//                                //dletefiles();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();
                        }


                    }
                }


            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBox.isChecked()) {
                    signinbtn.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                    cheked = true;
                    //checked
                } else {
                    cheked = false;
                    signinbtn.setBackground(getResources().getDrawable(R.drawable.btn0_bg));
                    //not checked
                }

            }
        });

    }

    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.CAMERA);
        int wtite = ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
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
            ActivityCompat.requestPermissions(SignUpActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            //  Log.e("granted","No");


        }
        //  Log.e("granted","Yes");


        return true;
    }

    private void selectimage(int s, int s1, int s2) {


        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle(R.string.choicedoc_txt);


        builder.setItems(cameraOptions, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (cameraOptions[item].equals(cameraOptions[0])) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, s);

                } else if (cameraOptions[item].equals(cameraOptions[1])) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, s1);

                } else if (cameraOptions[item].equals("Delete Photo")) {

                    Glide.with(SignUpActivity.this).load(R.drawable.user_24).fitCenter().error(R.drawable.user_24).into(aadharimg);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_24);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                    byte[] imageBytes = baos.toByteArray();

                    aadhar_stats = "1";
                    pan_States = "1";
                    //  ProfileImageDbase(authcode, base64String, aadhar_stats);
                    dialog.dismiss();
                } else if (cameraOptions[item].equals(cameraOptions[2])) {
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("*/*");
                    chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                    if (s2 == 111) {
                        startActivityForResult(chooseFile, 111);
                    } else {
                        startActivityForResult(chooseFile, 222);
                    }

                } else if (cameraOptions[item].equals(cameraOptions[3])) {
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


                            // base64String = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                            aadhar_stats = "1";

                            Glide.with(this).load(bitmap).into(aadharimg);
                            aadhar_img = String.valueOf(bitmap);


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
                            // Log.e("sizeofimage", String.valueOf(f_length));
                            if (f_length > 15728640) {
                                // Toast.makeText(this, R.string.imageflesize_txt, Toast.LENGTH_SHORT).show();
                            } else {


                                // base64String = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                aadhar_stats = "1";
                                //  ProfileImageDbase(authocode, base64String, aadhar_stats);

                                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                                if (selectedImage != null) {
                                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                                    if (cursor != null) {
                                        cursor.moveToFirst();

                                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                        String picturePath = cursor.getString(columnIndex);
                                        //    profile_img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                        Glide.with(this).load(picturePath).into(aadharimg);


                                        aadhar_img = String.valueOf(picturePath);

                                        cursor.close();
                                    }
                                }

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 2:
                    if (resultCode == RESULT_OK && data != null) {


                        bitmap2 = (Bitmap) data.getExtras().get("data");
//                        profile_img.setImageBitmap(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                        byte[] imageBytes = baos.toByteArray();
                        f_length = imageBytes.length;
                        //  Log.e("sizeofimage", String.valueOf(f_length));
                        if (f_length > 15728640) {
                            // Toast.makeText(this, R.string.imageflesize_txt, Toast.LENGTH_SHORT).show();
                        } else {


                            // base64String = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                            pan_States = "1";

                            Glide.with(this).load(bitmap2).into(panimg);
                            pan_img = String.valueOf(bitmap);


                        }
                    }
                    break;
                case 3:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        try {
                            bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap2.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                            byte[] imageBytes = baos.toByteArray();
                            f_length = imageBytes.length;
                            // Log.e("sizeofimage", String.valueOf(f_length));
                            if (f_length > 15728640) {
                                // Toast.makeText(this, R.string.imageflesize_txt, Toast.LENGTH_SHORT).show();
                            } else {


                                // base64String = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                pan_States = "1";
                                //  ProfileImageDbase(authocode, base64String, aadhar_stats);

                                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                                if (selectedImage != null) {
                                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                                    if (cursor != null) {
                                        cursor.moveToFirst();

                                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                        String picturePath = cursor.getString(columnIndex);
                                        //    profile_img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                        Glide.with(this).load(picturePath).into(panimg);


                                        pan_img = String.valueOf(picturePath);

                                        cursor.close();
                                    }
                                }

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                case 111:
                    if (resultCode == RESULT_OK && null != data) {
//                        // Get the Image from data
//                        Log.e("filepath", String.valueOf(requestCode));
//                        Uri content_describer = data.getData();
//                        String src = content_describer.getPath();
//
//
//
//                        File source = new File(src);
//                        Log.d("src is ", source.toString());
//                        String filename1 = content_describer.getLastPathSegment();
//                        Log.e("filename", filename1);
//                        //    text.setText(filename);
//                        Log.d("FileName is ", filename1);
//                        File destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename1);
//                        Log.d("Destination is ", destination.toString());
//
//                        String path = new File(data.getData().getPath()).getAbsolutePath();
//
//                        if(path != null){
//                            Uri uri = data.getData();
//
//                            String filename;
//                            Cursor cursor = getContentResolver().query(uri,null,null,null,null);
//
//                            if(cursor == null) filename=uri.getPath();
//                            else{
//                                cursor.moveToFirst();
//                                int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
//                                filename = cursor.getString(idx);
//                                cursor.close();
//                            }
//
//                            String name = filename.substring(0,filename.lastIndexOf("."));
//                            String extension = filename.substring(filename.lastIndexOf(".")+1);
//                            Log.d("Destination is ", name+","+extension);


                        //etToFolder.setEnabled(true);

                        Uri pdfUri = data.getData();
                        String frontImagePath = getPDFPath(pdfUri);
                        File file = new File(frontImagePath);
                        float fileSize = file.length();
                        //   Log.e("namammama", frontImagePath + "," + fileSize+","+file.getName());
                        aadharimg.setVisibility(View.GONE);
                        filename_1.setVisibility(View.VISIBLE);
                        filename_1.setText("FileName : " + file.getName());
                        aadhar_stats = "2";
                        adhardoc = frontImagePath;
                        break;
                    }
                case 222:
                    if (resultCode == RESULT_OK && null != data) {
//                        // Get the Image from data
//                        Log.e("filepath", String.valueOf(requestCode));
//                        Uri content_describer = data.getData();
//                        String src = content_describer.getPath();
//
//
//
//                        File source = new File(src);
//                        Log.d("src is ", source.toString());
//                        String filename1 = content_describer.getLastPathSegment();
//                        Log.e("filename", filename1);
//                        //    text.setText(filename);
//                        Log.d("FileName is ", filename1);
//                        File destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename1);
//                        Log.d("Destination is ", destination.toString());
//
//                        String path = new File(data.getData().getPath()).getAbsolutePath();
//
//                        if(path != null){
//                            Uri uri = data.getData();
//
//                            String filename;
//                            Cursor cursor = getContentResolver().query(uri,null,null,null,null);
//
//                            if(cursor == null) filename=uri.getPath();
//                            else{
//                                cursor.moveToFirst();
//                                int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
//                                filename = cursor.getString(idx);
//                                cursor.close();
//                            }
//
//                            String name = filename.substring(0,filename.lastIndexOf("."));
//                            String extension = filename.substring(filename.lastIndexOf(".")+1);
//                            Log.d("Destination is ", name+","+extension);


                        //etToFolder.setEnabled(true);

                        Uri pdfUri = data.getData();
                        String frontImagePath = getPDFPath(pdfUri);
                        File file = new File(frontImagePath);
                        float fileSize = file.length();
                        //   Log.e("namammama", frontImagePath + "," + fileSize+","+file.getName());
                        panimg.setVisibility(View.GONE);
                        filename_2.setVisibility(View.VISIBLE);
                        filename_2.setText("FileName : " + file.getName());
                        pan_States = "2";
                        pandoc = frontImagePath;
                        break;
                    }
            }


        }
    }


    private String getPDFPath(Uri uri) {
        String absolutePath = "";
        try {
            InputStream inputStream = this.getContentResolver().openInputStream(uri);
            byte[] pdfInBytes = new byte[inputStream.available()];
            inputStream.read(pdfInBytes);
            String encodePdf = Base64.encodeToString(pdfInBytes, Base64.DEFAULT);
            // Toast.makeText(this, ""+encodePdf, Toast.LENGTH_SHORT).show();
            int offset = 0;
            int numRead = 0;
            while (offset < pdfInBytes.length && (numRead = inputStream.read(pdfInBytes, offset, pdfInBytes.length - offset)) >= 0) {
                offset += numRead;
            }

            String mPath = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                mPath = this.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + "CY_" + System.currentTimeMillis() + ".pdf";
            } else {
                mPath = Environment.getExternalStorageDirectory().toString() + "/" + "Cy_" + System.currentTimeMillis() + ".pdf";
            }

            File pdfFile = new File(mPath);
            OutputStream op = new FileOutputStream(pdfFile);
            op.write(pdfInBytes);

            absolutePath = pdfFile.getPath();
            //   Log.e("filepath", absolutePath);

        } catch (Exception ae) {
            ae.printStackTrace();
        }
        return absolutePath;
    }

    private void GenderRadioButtonGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.selectchoice_txt);
        builder.setSingleChoiceItems(genderarray, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        Log.e("genderselected", "male");
                        // Toast.makeText(SignUpActivity.this, R.string.male_txt, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Log.e("genderselected", "Female");
                        //  Toast.makeText(SignUpActivity.this, R.string.female_txt, Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        Log.e("genderselected", "Female");
                        // Toast.makeText(SignUpActivity.this, R.string.female_txt, Toast.LENGTH_SHORT).show();
                        break;
                }
                alertDialog1.dismiss();
//                SharedPreferences.Editor editor = userdata.edit();
//                gender = String.valueOf(genderarray[item]);
//                editor.putString("gender", gender);
//                editor.apply();
                gender = genderarray[item];
                gender_txt.setText(genderarray[item]);

            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();


    }

    private boolean Validations() {
        fname = fstname.getText().toString();
        lname = lstname.getText().toString();
        email = email_id.getText().toString();
        mob = moblie_id.getText().toString();
        pass1 = pass1_id.getText().toString();
        pass2 = pass2_id.getText().toString();
        if (fname.isEmpty() || fname.equals(" ")) {
            fstname.requestFocus();
            fstname.setError("Enter Your First Name");
            return false;
        } else {
            fstname.setError(null);
        }
        if (lname.isEmpty() || lname.equals(" ")) {
            lstname.requestFocus();
            lstname.setError("Enter Your Last Name");
            return false;
        } else {
            lstname.setError(null);
        }
        if (email.isEmpty() || email.equals(" ")) {

            email_id.requestFocus();
            email_id.setError("Enter Your Email");
            return false;
        } else {
            email_id.setError(null);
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email_id.getText().toString()).matches()) {
            // Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            //  _email.setError("Invalid email");
            email_id.setError("Invalid Email");
            return false;
        } else {
            email_id.setError(null);
        }
        if (mob.isEmpty() || mob.equals(" ") || mob.length() != 10) {
            moblie_id.requestFocus();
            moblie_id.setError("Enter Your Mobile Number");
            return false;
        } else {
            moblie_id.setError(null);
        }
        if (gender.isEmpty() || gender.equals("")) {
            gender_txt.requestFocus();
            gender_txt.setError("Please select Your Gender");
            return false;
        } else {
            gender_txt.setError(null);
        }
        if (dataofbirth.isEmpty() || dataofbirth.equals("")) {
            dob_txt.requestFocus();
            dob_txt.setError("Please select Your Date Birth");
            return false;
        } else {
            dob_txt.setError(null);
        }
        if (aadhar_stats.equals("0") || aadhar_stats.equals("")) {
            Toast.makeText(this, "Please Upload your Aadhar Card", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pan_States.equals("0") || pan_States.equals("")) {
            Toast.makeText(this, "Please Upload your Pan Card", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pass1.isEmpty() || pass1.equals(" ")) {
            pass1_id.setError("Enter Your Password");
            //Toast.makeText(this, "Enter your Password", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            pass1_id.setError(null);
        }
        if (pass2.isEmpty() || pass2.equals(" ")) {
            pass2_id.setError("Enter Confirm Password");
            //Toast.makeText(this, "Enter your Password", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            pass2_id.setError(null);
        }
        if (!pass1.equals(pass2)) {
            Toast.makeText(this, "Password and Confirm Password should be same", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cheked == false) {
            Toast.makeText(this, "Please check the Checkbox", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void InitViews() {
        userdata = getSharedPreferences("Userdata", MODE_PRIVATE);
        genderarray = SignUpActivity.this.getResources().getStringArray(R.array.genderitems);
        cameraOptions = SignUpActivity.this.getResources().getStringArray(R.array.proofOptions);
        dataSync = new ApiDataSync(this);
        signinbtn = findViewById(R.id.signinbtn);
        checkBox = findViewById(R.id.checkBox);
        signinPage = findViewById(R.id.signinPage);
        lstname = findViewById(R.id.lname);
        email_id = findViewById(R.id.email_id);
        moblie_id = findViewById(R.id.moblie_id);
        pass1_id = findViewById(R.id.pass1_id);
        pass2_id = findViewById(R.id.pass2_id);
        aadharimg = findViewById(R.id.aadharimg);
        filename_2 = findViewById(R.id.filename_2);
        filename_1 = findViewById(R.id.filename_1);
        terms_conditions = findViewById(R.id.terms_conditions);
        panimg = findViewById(R.id.panimg);
        cname = findViewById(R.id.cname);
        gstnum = findViewById(R.id.gstnum);
        fstname = findViewById(R.id.fstname);
        dob_txt = findViewById(R.id.dob_txt);
        gender_txt = findViewById(R.id.gender_txt);
        helperClass = new HelperClass(this);

        GetIpAddress();
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

    private void GetIpAddress() {
        ArrayList<String> urls = new ArrayList<String>(); //to read each line

        new Thread(new Runnable() {
            public void run() {
                //TextView t; //to show the result, please declare and find it inside onCreate()

                try {
                    // Create a URL for the desired page
                    URL url = new URL("https://api.ipify.org/"); //My text file location
                    //First open the connection
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(60000); // timing out in a minute

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    //t=(TextView)findViewById(R.id.TextView1); // ideally do this in onCreate()
                    String str;
                    while ((str = in.readLine()) != null) {
                        urls.add(str);
                    }
                    in.close();

                } catch (Exception e) {
                    Log.d("MyTag", e.toString());
                }

                //since we are in background thread, to post results we have to go back to ui thread. do the following for that

                SignUpActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            ipaddress = urls.get(0);
                            //  Log.e("ipaddress",ipaddress);
                            // Toast.makeText(SignUpActivity.this, "Public IP:"+urls.get(0), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            //  Toast.makeText(SignUpActivity.this, "TurnOn wiffi to get public ip", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }).start();
    }

    public class neServiceCallCompleteListener implements OnServiceCallCompleteListener {
        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            try {
                // Log.e("signupresponse",jsonObject.toString());
                String status = jsonObject.getString("status");
                String errorcode = jsonObject.getString("errorCode");
                String message = jsonObject.getString("message");
                String otp = jsonObject.getString("OTP");
                String authtokenmessage = jsonObject.getString("authToken");
                String uid = jsonObject.getString("id");
                if (status.equals("1")) {
                    SharedPreferences.Editor editor = userdata.edit();
                    editor.putString("authToken", authtokenmessage);
//                    editor.putString("userId", uid);
                    editor.putString("otp", otp);
                    editor.apply();
                    Intent i = new Intent(SignUpActivity.this, OTPActivity.class);
                    i.putExtra("pass", "reg");
                    startActivity(i);
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