package com.cilpl.clusters.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cilpl.clusters.Activites.Interfaces.ApiSucessResponse;
import com.cilpl.clusters.Activites.Interfaces.CarteResponse;
import com.cilpl.clusters.Adapters.AminitiesAdapter;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;
import com.cilpl.clusters.databinding.ActivityBookingsBinding;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingsinfoActivity extends AppCompatActivity {
    private ActivityBookingsBinding binding;
    Vibrator vibrator;
    private String[] pkgarray;

    String cartpopup = "", btype = "", bseats = "", bprice = "", bwrk_Stations = "", bconf_cabins = "", bmd_Cabins = "", sdate = "", count = "", aminities = "", edate = "", seates = "", uname = "", compynyname = "", imgpath, gstNo = "", emailid = "", phonenum = "", authtoken, bookId = "", bookimg = "", booki_features = "", book_title = "";
    private int mYear, mMonth, mDay;
    Calendar c;
    HelperClass helperClass;
    SharedPreferences userdata;
    int counter = 1;
    String pkgoption = "";
    ArrayList<String> Amnitiesarray;
    Openpopup openpopup;
    ApiDataSync dataSync;
    ApiSucessResponse apiSucessResponse;
    CarteResponse cuteResponse;
    ArrayAdapter<String> packageadapter;
    private String[] Aminities = {"Parking", "WIFI", " Power Socket", "Printer/Scanner", "Tea/Coffee ", " Meeting Room", " Commen Area", " Reception", "Logout"};

    List<SlideModel> slideModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        InitViews();
        onClicks();
    }

    private void onClicks() {
        binding.showmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.showmore.setVisibility(View.GONE);
                binding.l1.setVisibility(View.VISIBLE);
                binding.showless.setVisibility(View.VISIBLE);
            }
        });
        binding.showless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.l1.setVisibility(View.GONE);
                binding.showless.setVisibility(View.GONE);
                binding.showmore.setVisibility(View.VISIBLE);
            }
        });

        String data = aminities;
        String[] items = data.split(",");
        for (String item : items) {
            String[] temp = item.replaceAll("[\\[\\]]", "").split("-@");
            // String test = item.replaceAll("[^a-zA-Z]", "");
            for (String tem : temp) {

                String input = tem.replaceAll("\\s+", "");
                //  Log.e("printlisttt", input);
                Amnitiesarray.add(input);

            }
        }

        AminitiesAdapter adapter = new AminitiesAdapter(this, Amnitiesarray);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.aminitiesRecycle.setLayoutManager(layoutManager);
        binding.aminitiesRecycle.setAdapter(adapter);


//        binding.showmore1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                binding.showmore1.setVisibility(View.GONE);
//                binding.l2.setVisibility(View.VISIBLE);
//                binding.showless1.setVisibility(View.VISIBLE);
//            }
//        });
//        binding.showless1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                binding.l2.setVisibility(View.GONE);
//                binding.showless1.setVisibility(View.GONE);
//                binding.showmore1.setVisibility(View.VISIBLE);
//            }
//        });
        binding.backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.bookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                cartpopup = "1";
//                OpenPopup(cartpopup);

                if (btype.equals("cluster")) {
                    AddcartPopup(bookId, book_title);

                } else if (btype.equals("WS") || btype.equals("MC")) {

                    CarteResponse apiSucessResponse = (status, count) -> {
                        //insertEventDB(name, payload);
                        if (status.equals("1")) {
                            binding.cartcount1.setVisibility(View.VISIBLE);
                            binding.cartcount1.setText(count);
                            HomeMainActivity.cartcount.setText(count);

                        } else {
                            Toast.makeText(BookingsinfoActivity.this, "Please give Proper Details", Toast.LENGTH_SHORT).show();
                        }
                        //   Log.e("printtest", status);
                    };


                    openpopup.Popup(bookId, btype, book_title, apiSucessResponse);
                } else {
                    Intent i = new Intent(BookingsinfoActivity.this, PracticeActivity.class);
                    i.putExtra("book_model", btype);
                    i.putExtra("book_id", bookId);
                    i.putExtra("book_title", book_title);
                    i.putExtra("page", "home");
                    startActivity(i);
                }
            }
        });
        binding.fabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BookingsinfoActivity.this, CartActivity.class);
                startActivity(i);
            }
        });

    }

    private void AddcartPopup(String bookId, String book_title) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.clusterbookslot);
        dialog.setCanceledOnTouchOutside(false);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView startdate = dialog.findViewById(R.id.startdate1);
        Spinner dayslist = dialog.findViewById(R.id.dayslist);
        TextView enddate = dialog.findViewById(R.id.enddate1);
        Button submit = dialog.findViewById(R.id.sumit_btn);

        LinearLayout resultpage = dialog.findViewById(R.id.book_result);
        LinearLayout slotpage = dialog.findViewById(R.id.book_slot);
        LinearLayout verifydetails = dialog.findViewById(R.id.verifydetails);
        ImageView cancel = dialog.findViewById(R.id.cancel_button);
        TextView compname = dialog.findViewById(R.id.compname);
        TextView gstno = dialog.findViewById(R.id.gstno);
        ImageView cance2 = dialog.findViewById(R.id.cancel_button1);
        pkgarray = getResources().getStringArray(R.array.packageoptions);
        packageadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pkgarray);
        packageadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayslist.setAdapter(packageadapter);
        dayslist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long l) {
                if (arg0.getId() == R.id.dayslist) {//Your Another Action Here.

                    if (pkgarray[position].equals("Select Item")) {
                        startdate.setText("");
                        enddate.setText("");
                        startdate.setHint("Start Date");
                        enddate.setHint("End Date");
                    } else {
                        pkgoption = pkgarray[position];
                        // Toast.makeText(getContext(), "Selected Package Option: "+pkgarray[position] ,Toast.LENGTH_SHORT).show();
                        startdate.setText("");
                        enddate.setText("");
                        startdate.setHint("Start Date");
                        enddate.setHint("End Date");
//                enddate.setText("");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        cance2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(BookingsinfoActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        sdate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        //   Log.e("startdate", sdate);


                        if (pkgoption.equals("Monthly")) {
                            String dateInString = sdate;  // Start date
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                            Calendar c1 = Calendar.getInstance(); // Get Calendar Instance
                            try {
                                c1.setTime(sdf.parse(dateInString));
                            } catch (java.text.ParseException e) {
                                throw new RuntimeException(e);
                            }

                            c1.add(Calendar.DATE, 30);  // add 45 days
                            sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Date resultdate = new Date(c1.getTimeInMillis());   // Get new time
                            dateInString = sdf.format(resultdate);
                            System.out.println("String date:" + dateInString);
                            enddate.setText(dateInString);
                            edate = dateInString;
                        } else if (pkgoption.equals("Quaterly")) {
                            String dateInString = sdate;  // Start date
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Calendar c1 = Calendar.getInstance(); // Get Calendar Instance
                            try {
                                c1.setTime(sdf.parse(dateInString));
                            } catch (java.text.ParseException e) {
                                throw new RuntimeException(e);
                            }
                            c1.add(Calendar.DATE, 90);  // add 45 days
                            sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Date resultdate = new Date(c1.getTimeInMillis());   // Get new time
                            dateInString = sdf.format(resultdate);
                            System.out.println("String date:" + dateInString);
                            enddate.setText(dateInString);
                            edate = dateInString;
                        } else if (pkgoption.equals("Half-Yearly")) {
                            String dateInString = sdate;  // Start date
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                            Calendar c1 = Calendar.getInstance(); // Get Calendar Instance
                            try {
                                c1.setTime(sdf.parse(dateInString));
                            } catch (java.text.ParseException e) {
                                throw new RuntimeException(e);
                            }
                            c1.add(Calendar.DATE, 180);  // add 45 days
                            sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Date resultdate = new Date(c1.getTimeInMillis());   // Get new time
                            dateInString = sdf.format(resultdate);
                            System.out.println("String date:" + dateInString);
                            enddate.setText(dateInString);
                            edate = dateInString;
                        } else if (pkgoption.equals("Anually")) {
                            String dateInString = sdate;  // Start date
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Calendar c1 = Calendar.getInstance(); // Get Calendar Instance
                            try {
                                c1.setTime(sdf.parse(dateInString));
                            } catch (java.text.ParseException e) {
                                throw new RuntimeException(e);
                            }

                            c1.add(Calendar.DATE, 360);  // add 45 days
                            sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Date resultdate = new Date(c1.getTimeInMillis());   // Get new time
                            dateInString = sdf.format(resultdate);
                            System.out.println("String date:" + dateInString);
                            edate = dateInString;
                            enddate.setText(dateInString);
                            edate = dateInString;
                        }


                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(c.getTimeInMillis());
                dpd.show();
                //Log.e("pakcageoption", pkgoption);
            }
        });
        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c1 = Calendar.getInstance();
                mYear = c1.get(Calendar.YEAR);
                mMonth = c1.get(Calendar.MONTH);
                mDay = c1.get(Calendar.DAY_OF_MONTH);
                Calendar etdate = Calendar.getInstance();

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(dialog.getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        //  edate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        // enddate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        etdate.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(BookingsinfoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                etdate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                etdate.set(Calendar.MINUTE, minute);
                                // Log.v(TAG, "The choosen one " + date.getTime());
                                // Toast.makeText(getContext(),"The choosen one " + date.getTime(),Toast.LENGTH_SHORT).show();
                                edate = (new SimpleDateFormat("dd-MM-yyyy h:mm a").format(etdate.getTime()));
                                enddate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(etdate.getTime()));
                            }
                        }, c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE), false).show();

                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(c1.getTimeInMillis());
                // dpd.getDatePicker().setMinDate(milliseconds(changeDate(sdate)) + 1);
                dpd.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pkgoption.equals("") || pkgoption.isEmpty() || pkgoption.equals("Select Item")) {
                    Toast.makeText(BookingsinfoActivity.this, "Please Select Package Options", Toast.LENGTH_SHORT).show();
                } else if (sdate.isEmpty() || edate.isEmpty()) {
                    Toast.makeText(BookingsinfoActivity.this, "Select Start Date and End Date", Toast.LENGTH_SHORT).show();

                } else {

                    // Log.e("bookslots", seates + "," + sdate + "," + edate);
                    if (helperClass.checkInternetConnection(BookingsinfoActivity.this)) {
                        CarteResponse apiSucessResponse = (status, count) -> {
                            //insertEventDB(name, payload);
                            if (status.equals("1")) {
                                slotpage.setVisibility(View.GONE);
                                verifydetails.setVisibility(View.GONE);
                                resultpage.setVisibility(View.GONE);
//                                storeValues(cartid,booktitle,sdate,edate,seates);
                                HomeMainActivity.cartcount.setText(count);
                                dialog.dismiss();


                            } else {
                                Toast.makeText(BookingsinfoActivity.this, "Please give Proper Details", Toast.LENGTH_SHORT).show();
                            }
                            // Log.e("printtest", status);
                        };

                        dataSync.AddCart(authtoken, btype, bookId, sdate, edate, "", seates, book_title, apiSucessResponse);
                    } else {
                        Toast.makeText(BookingsinfoActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();
                    }


                }


            }
        });


        dialog.show();


    }

    private void OpenPopup(String type) {
        uname = userdata.getString("firstname", "");
        emailid = userdata.getString("email", "");
        phonenum = userdata.getString("phne", "");
        compynyname = userdata.getString("compy_name", "");
        gstNo = userdata.getString("gst_no", "");
        final Dialog dialog = new Dialog(BookingsinfoActivity.this);
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
        TextView name_seates = dialog.findViewById(R.id.name_seates);
        TextView email_id = dialog.findViewById(R.id.email_id);
        TextView moblie_id = dialog.findViewById(R.id.moblie_id);
        TextView compname = dialog.findViewById(R.id.compname);
        TextView gstno = dialog.findViewById(R.id.gstno);
        TextView startdate = dialog.findViewById(R.id.startdate1);
        TextView enddate = dialog.findViewById(R.id.enddate1);
        Button submit = dialog.findViewById(R.id.sumit_btn);
        Button submit2 = dialog.findViewById(R.id.sumit2_btn);
        Button submit3 = dialog.findViewById(R.id.sumit3_btn);
        LinearLayout resultpage = dialog.findViewById(R.id.book_result);
        LinearLayout slotpage = dialog.findViewById(R.id.book_slot);
        LinearLayout verifydetails = dialog.findViewById(R.id.verifydetails);
        LinearLayout seates_layout = dialog.findViewById(R.id.seates_layout);
        ImageView cancel = dialog.findViewById(R.id.cancel_button);
        ImageView cance2 = dialog.findViewById(R.id.cancel_button1);
        fstname.setText(uname);
        email_id.setText(emailid);
        moblie_id.setText(phonenum);

        if (btype.equals("MC")) {
            name_seates.setText("No Of Manger Cabins");
        } else if (btype.equals("WS")) {
            name_seates.setText("No of Work Stations");
        } else if (btype.equals("MCR")) {
            name_seates.setText("No of Mini-Conferance Room");
        } else if (btype.equals("CR")) {
            name_seates.setText("No of Conferance Room");
        }
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
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        cance2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                Calendar etdate = Calendar.getInstance();
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(dialog.getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
//                            sdate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
//                            startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        etdate.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(BookingsinfoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                etdate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                etdate.set(Calendar.MINUTE, minute);
                                // Log.v(TAG, "The choosen one " + date.getTime());
                                // Toast.makeText(getContext(),"The choosen one " + date.getTime(),Toast.LENGTH_SHORT).show();
                                sdate = (new SimpleDateFormat("dd-MM-yyyy h:mm a").format(etdate.getTime()));
                                startdate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(etdate.getTime()));
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();


                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(c.getTimeInMillis());

                dpd.show();

            }
        });
        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c1 = Calendar.getInstance();
                mYear = c1.get(Calendar.YEAR);
                mMonth = c1.get(Calendar.MONTH);
                mDay = c1.get(Calendar.DAY_OF_MONTH);
                Calendar etdate = Calendar.getInstance();
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(dialog.getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
//                            edate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
//                            enddate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        etdate.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(BookingsinfoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                etdate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                etdate.set(Calendar.MINUTE, minute);
                                // Log.v(TAG, "The choosen one " + date.getTime());
                                // Toast.makeText(getContext(),"The choosen one " + date.getTime(),Toast.LENGTH_SHORT).show();
                                edate = (new SimpleDateFormat("dd-MM-yyyy h:mm a").format(etdate.getTime()));
                                enddate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(etdate.getTime()));
                            }
                        }, c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE), false).show();

                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(c1.getTimeInMillis());
//                    dpd.getDatePicker().setMinDate(milliseconds(changeDate(sdate)) + 1);
                dpd.show();
            }
        });

        addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(200);
                counter = counter + 1;
                addtxt.setText("" + counter);
            }
        });

        minusimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (counter < 0) {
                    counter = 0;
                    addtxt.setText(counter + "");
                }
                if (counter > 0) {
                    vibrator.vibrate(200);
                    counter = counter - 1;
                    addtxt.setText(counter + "");
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seates = addtxt.getText().toString();

//                if(btype.equals("cluster")){
//                    if (sdate.isEmpty() || edate.isEmpty()) {
//                        Toast.makeText(BookingsActivity.this, "Select Start Date and End Date", Toast.LENGTH_SHORT).show();
//
//                    }  else {
//                        seates="0";
//                        Log.e("bookslots", seates + "," + sdate + "," + edate);
//                        if (helperClass.checkInternetConnection(BookingsActivity.this)) {
//
//                            if (type.equals("1")) {
//                                ApiSucessResponse apiSucessResponse = (status) -> {
//                                    //insertEventDB(name, payload);
//                                    if (status.equals("1")) {
//
//
//                                        slotpage.setVisibility(View.GONE);
//                                        verifydetails.setVisibility(View.VISIBLE);
//                                        resultpage.setVisibility(View.GONE);
//
//
//                                    } else {
//                                        Toast.makeText(BookingsActivity.this, "Slots are not Available on these Date", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                };
//                                dataSync.Bookingslotdates(authtoken, bookId, sdate, edate, seates, btype, apiSucessResponse);
//
//                            } else {
//                                CarteResponse cuteResponse = (status, count) -> {
//                                    //insertEventDB(name, payload);
//                                    if (status.equals("1")) {
//
//
//                                        slotpage.setVisibility(View.GONE);
//                                        verifydetails.setVisibility(View.GONE);
//                                        resultpage.setVisibility(View.GONE);
//                                        dialog.dismiss();
//
//                                        binding.cartcount1.setText(count);
//                                    }
//
//
//                                };
//
//                                dataSync.AddCart(authtoken, btype, bookId, sdate, edate, seates, book_title, cuteResponse);
//
//
//                            }
//
//
//                        } else {
//                            Toast.makeText(BookingsActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();
//                        }
//
//
//                    }
//
//                }else {
                if (sdate.isEmpty() || edate.isEmpty()) {
                    Toast.makeText(BookingsinfoActivity.this, "Select Start Date and End Date", Toast.LENGTH_SHORT).show();

                } else if (seates.equals("0") || seates.isEmpty() || seates.equals("")) {
                    Toast.makeText(BookingsinfoActivity.this, "Please Select No of Seats", Toast.LENGTH_SHORT).show();

                } else {

                    //  Log.e("bookslots", seates + "," + sdate + "," + edate);
                    if (helperClass.checkInternetConnection(BookingsinfoActivity.this)) {

                        if (type.equals("1")) {
                            ApiSucessResponse apiSucessResponse = (status) -> {
                                //insertEventDB(name, payload);
                                if (status.equals("1")) {


                                    slotpage.setVisibility(View.GONE);
                                    verifydetails.setVisibility(View.VISIBLE);
                                    resultpage.setVisibility(View.GONE);


                                } else {
                                    Toast.makeText(BookingsinfoActivity.this, "Slots are not Available on these Date", Toast.LENGTH_SHORT).show();
                                }

                            };
                            dataSync.Bookingslotdates(authtoken, bookId, sdate, edate, seates, btype, apiSucessResponse);

                        } else {
                            CarteResponse cuteResponse = (status, count) -> {
                                //insertEventDB(name, payload);
                                if (status.equals("1")) {


                                    slotpage.setVisibility(View.GONE);
                                    verifydetails.setVisibility(View.GONE);
                                    resultpage.setVisibility(View.GONE);
                                    dialog.dismiss();

                                    binding.cartcount1.setText(count);
                                }


                            };

                            dataSync.AddCart(authtoken, btype, bookId, sdate, edate, "", seates, book_title, cuteResponse);


                        }


                    } else {
                        Toast.makeText(BookingsinfoActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();
                    }


                }

            }


        });
        submit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


                    if (helperClass.checkInternetConnection(BookingsinfoActivity.this)) {
                        ApiSucessResponse apiSucessResponse = (status) -> {
                            //insertEventDB(name, payload);
                            if (status.equals("1")) {
                                slotpage.setVisibility(View.GONE);
                                verifydetails.setVisibility(View.GONE);
                                resultpage.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(BookingsinfoActivity.this, "Please give Proper Details", Toast.LENGTH_SHORT).show();
                            }
                            // Log.e("printtest", status);
                        };

                        dataSync.BookingConfirmdetails(authtoken, bookId, sdate, edate, seates, btype, uname, emailid, phonenum, compynyname, gstNo, apiSucessResponse);
                    } else {
                        Toast.makeText(BookingsinfoActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });


        dialog.show();


    }

    private long milliseconds(Object changeDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        System.out.println("Date in milli :: " + changeDate + 1);
        try {
            Date mDate = null;
            try {
                mDate = sdf.parse(String.valueOf(changeDate));
            } catch (java.text.ParseException e) {
                throw new RuntimeException(e);
            }
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milliq :: " + timeInMilliseconds);
            return timeInMilliseconds;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }

    private Object changeDate(String sdate) {
        SimpleDateFormat readFormat = null, writeFormat = null;
        Date startDate = null, endDate = null;
        //  String strStartDate = "2014-12-09";

        readFormat = new SimpleDateFormat("dd-MM-yyyy");
        writeFormat = new SimpleDateFormat("MM/dd/yyyy");

        try {
            try {
                startDate = readFormat.parse(sdate);
            } catch (java.text.ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return writeFormat.format(startDate);
    }

    private boolean validation() {
        return true;
    }

    private void InitViews() {
        vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        userdata = getSharedPreferences("Userdata", MODE_PRIVATE);
        authtoken = userdata.getString("authToken", "");
        uname = userdata.getString("firstname", "");
        emailid = userdata.getString("email", "");
        phonenum = userdata.getString("phne", "");
        count = userdata.getString("c_count", "");
        dataSync = new ApiDataSync(this);
        helperClass = new HelperClass(this);
        slideModel = new ArrayList<>();
        Amnitiesarray = new ArrayList<>();
        openpopup = new Openpopup(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bookId = bundle.getString("bookid");
            bookimg = bundle.getString("b_img");
            booki_features = bundle.getString("b_features");
            book_title = bundle.getString("b_name");
            btype = bundle.getString("b_type");
            bseats = bundle.getString("b_seats");
            bprice = bundle.getString("b_price");
            bwrk_Stations = bundle.getString("b_wrkststions");
            bconf_cabins = bundle.getString("b_conf_cabins");
            bmd_Cabins = bundle.getString("b_mdcabins");
            imgpath = bundle.getString("b_imgpath");
            aminities = bundle.getString("b_aminities");
        }
        String data = bookimg;
        String[] items = data.split(",");
        for (String item : items) {
            String[] temp = item.replaceAll("[\\[\\]]", "").split("-@");
            // String test = item.replaceAll("[^a-zA-Z]", "");
            for (String tem : temp) {

                String input = tem.replaceAll("\\s+", "");
                //   Log.e("printlisttt", input);
                slideModel.add(new SlideModel(imgpath + input, ScaleTypes.CENTER_CROP));
            }
        }

        binding.infoSlider.setImageList(slideModel, ScaleTypes.CENTER_CROP);
        if (btype.equals("cluster")) {
            binding.confernaceLay.setVisibility(View.VISIBLE);
            binding.workstationLay.setVisibility(View.VISIBLE);
            binding.mangerLay.setVisibility(View.VISIBLE);
        } else {
            binding.confernaceLay.setVisibility(View.GONE);
            binding.workstationLay.setVisibility(View.GONE);
            binding.mangerLay.setVisibility(View.GONE);
        }
        //  Log.e("bookslot", bookId+","+count);
        binding.texname.setText(book_title);
        binding.hedaerName.setText(book_title);
        binding.address.setText(booki_features);
        if (count.equals("0")) {
            binding.cartcount1.setVisibility(View.GONE);
        } else {
            binding.cartcount1.setVisibility(View.VISIBLE);
            binding.cartcount1.setText(count);
        }

        binding.priceTxt.setText(" Rs. " + bprice + "/-");
        binding.seatsSize.setText(" " + bseats);
        binding.confRoom.setText(" " + bconf_cabins);
        binding.mangerCabins.setText(" " + bmd_Cabins);
        binding.workstationSize.setText(" " + bwrk_Stations);
        // Glide.with(this).load(bookimg).error(R.drawable.screenshare).into(binding.bookicon);

    }
}