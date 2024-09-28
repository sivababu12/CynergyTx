package com.cilpl.clusters.Activites;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.cilpl.clusters.Activites.Interfaces.ApiSucessResponse;
import com.cilpl.clusters.Activites.Interfaces.CarteResponse;
import com.cilpl.clusters.Adapters.ChairAdapter;
import com.cilpl.clusters.Model.ChairSlotModel;
import com.cilpl.clusters.Model.TimeSlotsModel;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.NetworkCalling.NetworkServiceCall;
import com.cilpl.clusters.NetworkCalling.OnServiceCallCompleteListener;
import com.cilpl.clusters.NetworkCalling.ServerURL;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;
import com.cilpl.clusters.databinding.ActivityBookingsBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Openpopup {
    SQLiteDatabase database;
    String DB_PATH, dbpath, DB_NAME, authcode, time = "", fcmid, uid, sdate = "", edate = "", authtoken, chairlist = "0";
    Context context;
    SharedPreferences userdata;
    Calendar c;
    HelperClass helperClass;
    private int mYear, mMonth, mDay;
    private ArrayList<TimeSlotsModel> timeSlotsModels;
    private ArrayList<ChairSlotModel> chairSlotModels;
    ApiDataSync dataSync;
    String stmonth = "", etmonth = "";
    ApiSucessResponse apiSucessResponse;
    CarteResponse carteResponse;
    private String[] users, pkgarray;
    ArrayAdapter<String> packageadapter;
    List<String> nextMonths, newMonths;
    String filepath, uname = "", pkgoption = "", emailid = "",seates = "", bookiningtype = "",phonenum = "", compynyname = "", gstNo = "";
    ArrayAdapter<String> startDateadapter, endMonthadapter;

    public Openpopup(Context context) {
        this.context = context;
        if (android.os.Build.VERSION.SDK_INT >= 30) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = context.getFilesDir().getParentFile().getPath() + "/databases/";
        }
        dbpath = DB_PATH + DB_NAME;
        dataSync = new ApiDataSync(context);
        userdata = context.getSharedPreferences("Userdata", context.MODE_PRIVATE);
        authcode = userdata.getString("authToken", "");
        fcmid = userdata.getString("notificationId", "") + "";
        uid = userdata.getString("user_id", "") + "";
        authtoken = userdata.getString("authToken", "");
        helperClass = new HelperClass(context);
    }
    public void OpenPopup(String bookid, String cartid, String booktitle) {
        bookiningtype = "cluster";
        uname = userdata.getString("firstname", "");
        emailid = userdata.getString("email", "");
        phonenum = userdata.getString("phne", "");
        compynyname = userdata.getString("compy_name", "");
        gstNo = userdata.getString("gst_no", "");
        final Dialog dialog = new Dialog(context);
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
        pkgarray=context.getResources().getStringArray(R.array.packageoptions);
        packageadapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, pkgarray);
        packageadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayslist.setAdapter(packageadapter);
        dayslist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long l) {
                switch (arg0.getId()) {


                    case R.id.dayslist:
                        //Your Another Action Here.

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
                        break;


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
                DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        sdate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        //  Log.e("startdate", sdate);


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
                //  Log.e("pakcageoption", pkgoption);
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
                        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
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
                    Toast.makeText(context, "Please Select Package Options", Toast.LENGTH_SHORT).show();
                } else if (sdate.isEmpty() || edate.isEmpty()) {
                    Toast.makeText(context, "Select Start Date and End Date", Toast.LENGTH_SHORT).show();

                } else {

                    //  Log.e("bookslots", seates + "," + sdate + "," + edate);
                    if (helperClass.checkInternetConnection(context)) {
                        CarteResponse apiSucessResponse = (status, count) -> {
                            //insertEventDB(name, payload);
                            if (status.equals("1")) {
                                slotpage.setVisibility(View.GONE);
                                verifydetails.setVisibility(View.GONE);
                                resultpage.setVisibility(View.GONE);
//                                storeValues(cartid,booktitle,sdate,edate,seates);
                                HomeMainActivity.cartcount.setVisibility(View.VISIBLE);
                                HomeMainActivity.cartcount.setText(count);
                                dialog.dismiss();


                            } else {
                                Toast.makeText(context, "Please give Proper Details", Toast.LENGTH_SHORT).show();
                            }
                            // Log.e("printtest", status);
                        };

                        dataSync.AddCart(authtoken, bookiningtype, bookid, sdate, edate,"", seates, booktitle, apiSucessResponse);
                    } else {
                        Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
                    }


                }


            }
        });


        dialog.show();


    }
    public void Popup(String bookid, String booktype, String booktitle, CarteResponse carteResponse) {
        Log.e("openpopup", booktype+","+booktitle+","+bookid);
        this.carteResponse = carteResponse;
        final Dialog dialog = new Dialog(context);
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
        TextView bookingtitile = dialog.findViewById(R.id.bookingtitile);
        Button submit = dialog.findViewById(R.id.sumit_btn);
        Button submit2 = dialog.findViewById(R.id.sumit2_btn);
        Button submit3 = dialog.findViewById(R.id.sumit3_btn);

        ImageView cancel = dialog.findViewById(R.id.cancel_button);
        TextView compname = dialog.findViewById(R.id.compname);
        TextView gstno = dialog.findViewById(R.id.gstno);

        Spinner startmonth = dialog.findViewById(R.id.startmonth);
        Spinner endmonth = dialog.findViewById(R.id.endmonth);
        Button sumitmonth_btn = dialog.findViewById(R.id.sumitmonth_btn);

        bookingtitile.setText(booktitle);
        LinearLayout resultpage = dialog.findViewById(R.id.book_result);
        LinearLayout slotpage = dialog.findViewById(R.id.book_slot);
        LinearLayout seates_layout = dialog.findViewById(R.id.seates_layout);
        LinearLayout verifydetails = dialog.findViewById(R.id.verifydetails);
        Button daily_btn = dialog.findViewById(R.id.daily_btn);
        Button montly_btn = dialog.findViewById(R.id.montly_btn);
        seates_layout.setVisibility(View.GONE);
        ImageView cance2 = dialog.findViewById(R.id.cancel_button1);
        LinearLayout optionselection = dialog.findViewById(R.id.optionselection);
        RelativeLayout dailylypage = dialog.findViewById(R.id.dailylypage);
        RelativeLayout monthlypage = dialog.findViewById(R.id.monthlypage);
if(!booktype.equals("WS")){
    montly_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
         //   Log.e("selection", "monthly");
            optionselection.setVisibility(View.GONE);
            dailylypage.setVisibility(View.GONE);
            monthlypage.setVisibility(View.VISIBLE);
        }
    });

    daily_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          //  Log.e("selection", "daily");
//                Intent i = new Intent(context,PracticeActivity.class);
//                i.putExtra("book_model",booktype);
//                i.putExtra("book_id",bookid);
//                i.putExtra("book_title",booktitle);
//                context.startActivity(i);
//                dialog.dismiss();

            optionselection.setVisibility(View.GONE);
            dailylypage.setVisibility(View.VISIBLE);
            monthlypage.setVisibility(View.GONE);

        }
    });
}else {
    optionselection.setVisibility(View.GONE);
    dailylypage.setVisibility(View.GONE);
    monthlypage.setVisibility(View.VISIBLE);
}

        YearMonth currentMonth = YearMonth.now();


        nextMonths = new ArrayList<>();
        nextMonths.add("Select From Month");
        for (int i = 1; i <= 12 - currentMonth.getMonthValue(); i++) {
            nextMonths.add(YearMonth.of(currentMonth.getYear(), currentMonth.getMonthValue() + i).format(DateTimeFormatter.ofPattern("MMM-yyyy", Locale.ENGLISH)));
        }
       // Log.e("months", String.valueOf(nextMonths));
        System.out.println(nextMonths);
        startDateadapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, nextMonths);
        startDateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startmonth.setAdapter(startDateadapter);

        newMonths = new ArrayList<>();
        startmonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


                if (nextMonths.get(position).equals("Select From Month")) {
                    newMonths.clear();
                    stmonth = "";
//                    binding.errormessage.setVisibility(View.VISIBLE);
                } else {

                    stmonth = nextMonths.get(position);
                   // Log.e("positon", stmonth);
                    newMonths = new ArrayList<>();
                    newMonths.add("Select To Month");
                    String maxDate = stmonth;
                    SimpleDateFormat monthDate = new SimpleDateFormat("MMM-yyyy");
                    Calendar cal = Calendar.getInstance();
                    try {
                        cal.setTime(monthDate.parse(maxDate));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    for (int i = 1; i <= 12; i++) {
                        String month_name1 = monthDate.format(cal.getTime());
                        newMonths.add(month_name1);
                        cal.add(Calendar.MONTH, +1);
                      //  Log.e("dates", String.valueOf(i));

                    }

                    System.out.println(newMonths);


                    endMonthadapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, newMonths);
                    endMonthadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    endmonth.setAdapter(endMonthadapter);


//                    binding.errormessage.setVisibility(View.GONE);
                    // Toast.makeText(getContext(), "Selected User: " + users[position], Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        endmonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (newMonths.get(position).equals("Select To Month")) {
                    etmonth = "";
                }
                etmonth = newMonths.get(position);
               // Log.e("positon", etmonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sumitmonth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validations()) {

                //    Log.e("fetchdata", authtoken + "," + stmonth + "," + etmonth + "," + bookid + "," + booktitle + "," + booktype + ",");
                  //  Log.e("slectedmonts", stmonth + "," + etmonth);

                    if (booktype.equals("WS") || booktype.equals("ind")) {
                        SendDate(authtoken, stmonth, etmonth, bookid, booktitle, booktype, carteResponse);
                    } else {
                        if (helperClass.checkInternetConnection(context)) {
                            CarteResponse carteResponse = (status, count) -> {

                                if (status.equals("1")) {

                                 HomeMainActivity.cartcount.setText(count);
                                    dialog.dismiss();

                                } else {
                                    Toast.makeText(context, "Please give Proper Details", Toast.LENGTH_SHORT).show();
                                }
                               // Log.e("printtest", status);
                            };

                            dataSync.AddCart(authtoken, booktype, bookid, stmonth, etmonth, "0", "0", booktitle, carteResponse);
                        } else {
                            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
                        }
                    }
                    dialog.dismiss();

                }
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
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                Calendar etdate = Calendar.getInstance();

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(dialog.getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //  Display Selected date in textbox
                        sdate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                        etdate.set(year, monthOfYear, dayOfMonth);
//                        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                etdate.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                                etdate.set(Calendar.MINUTE, minute);
//                                // Log.v(TAG, "The choosen one " + date.getTime());
//                                // Toast.makeText(getContext(),"The choosen one " + date.getTime(),Toast.LENGTH_SHORT).show();
//                                sdate=  (new SimpleDateFormat("dd-MM-yyyy h:mm a").format(etdate.getTime()));
//                                startdate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(etdate.getTime()));
//                            }
//                        },c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();

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
                        edate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        enddate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

//                        etdate.set(year, monthOfYear, dayOfMonth);
//                        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                etdate.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                                etdate.set(Calendar.MINUTE, minute);
//                                // Log.v(TAG, "The choosen one " + date.getTime());
//                                // Toast.makeText(getContext(),"The choosen one " + date.getTime(),Toast.LENGTH_SHORT).show();
//                                edate=(new SimpleDateFormat("dd-MM-yyyy h:mm a").format(etdate.getTime()));
//                                enddate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(etdate.getTime()));
//                            }
//                        },c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE), false).show();

                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(c1.getTimeInMillis());
                //  dpd.getDatePicker().setMinDate(milliseconds(changeDate(sdate)) + 1);
                dpd.show();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sdate.isEmpty() || edate.isEmpty()) {
                    Toast.makeText(context, "Select Start Date and End Date", Toast.LENGTH_SHORT).show();

//                } else if (seates.equals("0") || seates.isEmpty() || seates.equals("")) {
//                    Toast.makeText(context, "Please Select No of Seats", Toast.LENGTH_SHORT).show();
//
                } else {

                   // Log.e("bookslots", sdate + "," + edate);
                    if (helperClass.checkInternetConnection(context)) {

                        slotpage.setVisibility(View.GONE);
                        verifydetails.setVisibility(View.GONE);
                        resultpage.setVisibility(View.GONE);

                        if (booktype.equals("WS") || booktype.equals("ind")) {
                            SendDate(authtoken, sdate, edate, bookid, booktitle, booktype, carteResponse);
                        } else {
                            if (helperClass.checkInternetConnection(context)) {
                                CarteResponse carteResponse = (status, count) -> {

                                    if (status.equals("1")) {
                                       HomeMainActivity.cartcount.setText(count);
                                        dialog.dismiss();

                                    } else {
                                        Toast.makeText(context, "Please give Proper Details", Toast.LENGTH_SHORT).show();
                                    }
                                  //  Log.e("printtest", status);
                                };

                                dataSync.AddCart(authtoken, booktype, bookid, sdate, edate, "0", "0", booktitle, carteResponse);
                            } else {
                                Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
                            }
                        }


                        dialog.dismiss();


                    } else {
                        Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
                    }


                }


            }
        });
        dialog.show();
    }

    private boolean Validations() {
        if (stmonth.equals("") || stmonth.equals("Select From Month")) {
            Toast.makeText(context, "Please Select Start Month", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etmonth.equals("") || etmonth.equals("Select To Month")) {
            Toast.makeText(context, "Please Select To Month", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void SendDate(String authtoken, String startdate, String endate, String bookid, String booktitle, String booktype, CarteResponse apiSucessResponse) {
        try {
           // Log.e("sadsadsaa", "eeee");
            String dateapi = ServerURL.getSlots();
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("authToken", authtoken);
            hashMap.put("date", startdate);
            hashMap.put("enddate", endate);
            hashMap.put("product_id", bookid);
          //  Log.e("bookingslot", dateapi + "," + authtoken + "," + startdate + "," + endate + "," + bookid);
            NetworkServiceCall serviceCall = new NetworkServiceCall(context, true);
            serviceCall.setOnServiceCallCompleteListener(new slotbookslotCompleteListener(bookid, booktype, booktitle, startdate, endate, apiSucessResponse));
            serviceCall.makeJSONObjectPostStringRequest(dateapi, hashMap, Request.Priority.HIGH);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class slotbookslotCompleteListener implements OnServiceCallCompleteListener {
        String bookid, booktype, booktitle, startdate, endate;
        CarteResponse carteResponse;

        public slotbookslotCompleteListener(String bookid, String booktype, String booktitle, String startdate, String endate, CarteResponse sucessresponse) {
            this.bookid = bookid;
            this.booktype = booktype;
            this.booktitle = booktitle;
            this.startdate = startdate;
            this.endate = endate;
            this.carteResponse = sucessresponse;

        }

        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            timeSlotsModels = new ArrayList<>();
            chairSlotModels = new ArrayList<>();
            ChairAdapter.chairSlots = new ArrayList<>();
            try {
               // Log.e("bookingslot", jsonObject.toString());
                String status = jsonObject.getString("status");
                String errorcode = jsonObject.getString("errorCode");
                String message = jsonObject.getString("message");
                JSONObject ob = jsonObject.getJSONObject("data");
                JSONArray array = ob.getJSONArray("slots");
                for (int i = 0; i < array.length(); i++) {
                  //  Log.e("bookingslot", array.toString());
                    JSONObject object = array.getJSONObject(i);
                    TimeSlotsModel model = new TimeSlotsModel();
                    model.setTime(object.getString("time"));
                    model.setStatus(object.getString("status"));
                    timeSlotsModels.add(model);

                }
                JSONArray array1 = ob.getJSONArray("chairs");
                for (int i = 0; i < array1.length(); i++) {
                  //  Log.e("bookingslot", array.toString());
                    JSONObject object = array1.getJSONObject(i);
                    ChairSlotModel model = new ChairSlotModel();
                    model.setChair(object.getString("chair"));
                    model.setStatus(object.getString("status"));
                    chairSlotModels.add(model);

                }
                popUP(chairSlotModels, bookid, booktype, booktitle, startdate, endate, (CarteResponse) carteResponse);
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

    private void popUP(ArrayList<ChairSlotModel> chairSlotModels, String bookid, String booktype, String booktitle, String startdate, String endate, CarteResponse sucessResponse) {
        this.carteResponse = sucessResponse;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.chairlist);
        dialog.setCanceledOnTouchOutside(false);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        RecyclerView chair_recycle = dialog.findViewById(R.id.chair_recycle);
        Button submit_time = dialog.findViewById(R.id.submit_time);
        ImageView cancel_btn = dialog.findViewById(R.id.cancelbtn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        submit_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Log.e("praticeactivity", ChairAdapter.chairSlots.toString() + "," + startdate + ",size=" + ChairAdapter.chairSlots.size());

                // dataSync.SendSlotData(sdate, "0", 0, "1", ChairAdapter.chairSlots.toString(), ChairAdapter.chairSlots.size(), "0");
//                binding.addBtn.setVisibility(View.VISIBLE);
//                binding.slots.setVisibility(View.VISIBLE);

                if (helperClass.checkInternetConnection(context)) {
                    CarteResponse apiSucessResponse = (status, count) -> {
                        //insertEventDB(name, payload);
                        if (status.equals("1")) {


                            sucessResponse.apiStatus("1", count);
                            HomeMainActivity.cartcount.setText(count);

                            dialog.dismiss();


                        } else {
                            Toast.makeText(context, "Please give Proper Details", Toast.LENGTH_SHORT).show();
                        }
                      //  Log.e("printtest", status);
                    };

                    dataSync.AddCart(authtoken, booktype, bookid, startdate, endate, ChairAdapter.chairSlots.toString(), String.valueOf(ChairAdapter.chairSlots.size()), booktitle, apiSucessResponse);
                } else {
                    Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
                }


               // Log.e("workstationslots", String.valueOf(ChairAdapter.chairSlots.size()));
//                binding.slots.setText("(" + TimeAdapter.timeslots.size() + " Slots, " + ChairAdapter.chairSlots.size() + " Chairs)");

            }
        });

        LinearLayoutManager layoutManager = new GridLayoutManager(context, 4);
        ChairAdapter adapter = new ChairAdapter(context, sdate, chairlist, chairSlotModels);
        chair_recycle.setLayoutManager(layoutManager);
        chair_recycle.setAdapter(adapter);
        dialog.show();
    }
}
