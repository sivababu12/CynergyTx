package com.cilpl.clusters.Adapters;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cilpl.clusters.Activites.BookingsinfoActivity;
import com.cilpl.clusters.Activites.HomeMainActivity;
import com.cilpl.clusters.Activites.Interfaces.CarteResponse;
import com.cilpl.clusters.Model.BookingModel;
import com.cilpl.clusters.Model.DaysModel;
import com.cilpl.clusters.Model.HomeModel;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.NetworkCalling.ShowDialog;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Cluster_Adapter extends RecyclerView.Adapter<Cluster_Adapter.ViewHolder> {
    Context context;

    SharedPreferences userdata;
    String filepath, uname = "", emailid = "", bookiningtype = "cluster", phonenum = "", sdate = "", compynyname = "", gstNo = "", edate = "", seates = "", authtoken, cartid = "", booktitle = "";
    private ArrayList<HomeModel> homeModels;
    String authcode;
    private final int SINGLE_VIEW = 1;
    private ArrayList<DaysModel> daysModels;
    Vibrator vibrator;
    Calendar c;
    private int mYear, mMonth, mDay;
    int counter = 1;
    HelperClass helperClass;
    ApiDataSync dataSync;
    ShowDialog sdialog;
    ArrayAdapter<String> packageadapter;
    private String[] users, pkgarray;
    List<SlideModel> slideModel;
    String pkgoption = "";
    private ArrayList<BookingModel> bookingModels;

    public Cluster_Adapter(FragmentActivity context, ArrayList<HomeModel> homeModels, ArrayList<DaysModel> daysModels) {
        this.context = context;
        this.homeModels = homeModels;
        this.daysModels = daysModels;


    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.cluster_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        dataSync = new ApiDataSync(context);
        sdialog = new ShowDialog(context);
        helperClass = new HelperClass(context);
        bookingModels = new ArrayList<>();
        slideModel=new ArrayList<>();
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        userdata = context.getSharedPreferences("Userdata", context.MODE_PRIVATE);
        filepath = userdata.getString("cluster_imagepath", "");
        authtoken = userdata.getString("authToken", "");
        final HomeModel model = homeModels.get(position);
        holder.headertxt.setText(model.getName());
        cartid = model.getId();
        booktitle = model.getName();
        holder.address.setText(model.getRoom_features());
        holder.title.setText("  : " + model.getSeats_capacity());

        String data=model.getImages();
        String[] items = data.split(",");
        for (String item : items) {
            String[] temp = item.replaceAll("[\\[\\]]", "").split("-@");
            // String test = item.replaceAll("[^a-zA-Z]", "");
            for (String tem : temp) {

                String input = tem.replaceAll("\\s+", "");
               // Log.e("printlisttt", input);
                slideModel.add(new SlideModel(filepath + input, ScaleTypes.CENTER_CROP));
            }
        }
    //    Log.e("imagesss",model.getImages());
        holder.image_slider.setImageList(slideModel, ScaleTypes.CENTER_CROP);

        //  holder.title2.setText("  : "+model.getAvailability());
//        Glide.with(context).load(filepath + model.getImages()).error(R.drawable.screenshare).into(holder.homeicon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent o = new Intent(context, BookingsinfoActivity.class);
                o.putExtra("bookid", model.getId());
                o.putExtra("b_name", model.getName());
                o.putExtra("b_img",   model.getImages());
                o.putExtra("b_features", model.getRoom_features());
                o.putExtra("b_type", "cluster");
                o.putExtra("b_aminities", model.getAminities());
                o.putExtra("b_seats",model.getSeats_capacity());
                o.putExtra("b_price",model.getPrice());
                o.putExtra("b_wrkststions",model.getWork_stations());
                o.putExtra("b_conf_cabins",model.getConf_room());
                o.putExtra("b_mdcabins",model.getManager_cabins());
                o.putExtra("b_imgpath",filepath);
                context.startActivity(o);
            }
        });
        holder.price_txt.setText(" Rs "+model.getPrice()+"/-");
        holder.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                holder.addbtn.setVisibility(View.GONE);


                OpenPopup(model.getId(), model.getId(), model.getName());

            }
        });
        final DaysModel daysModel = daysModels.get(position);
        String sunday = daysModel.getSun();
        String mon = daysModel.getMon();
        String tue = daysModel.getTue();
        String wed = daysModel.getWed();
        String thur = daysModel.getThur();
        String fri = daysModel.getFri();
        String sat = daysModel.getSat();
        if (sunday.equals("0")) {
            holder.sun_txt.setBackground(context.getDrawable(R.drawable.roundred_txt));
        } else {
            holder.sun_txt.setBackground(context.getDrawable(R.drawable.round_txt));
        }

        if (mon.equals("0")) {
            holder.mon_txt.setBackground(context.getDrawable(R.drawable.roundred_txt));
        } else {
            holder.mon_txt.setBackground(context.getDrawable(R.drawable.round_txt));
        }
        if (tue.equals("0")) {
            holder.tue_txt.setBackground(context.getDrawable(R.drawable.roundred_txt));
        } else {
            holder.tue_txt.setBackground(context.getDrawable(R.drawable.round_txt));
        }
        if (wed.equals("0")) {
            holder.wed_txt.setBackground(context.getDrawable(R.drawable.roundred_txt));
        } else {
            holder.wed_txt.setBackground(context.getDrawable(R.drawable.round_txt));
        }
        if (thur.equals("0")) {
            holder.thur_txt.setBackground(context.getDrawable(R.drawable.roundred_txt));
        } else {
            holder.thur_txt.setBackground(context.getDrawable(R.drawable.round_txt));
        }
        if (fri.equals("0")) {
            holder.fri_txt.setBackground(context.getDrawable(R.drawable.roundred_txt));
        } else {
            holder.fri_txt.setBackground(context.getDrawable(R.drawable.round_txt));
        }
        if (sat.equals("0")) {
            holder.sat_txt.setBackground(context.getDrawable(R.drawable.roundred_txt));
        } else {
            holder.sat_txt.setBackground(context.getDrawable(R.drawable.round_txt));
        }


    }

    private void OpenPopup(String bookid, String cartid, String booktitle) {

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

    private void storeValues(String cartid, String booktitle, String s_date, String e_date, String seates) {

       // Log.e("cartls", cartid + "," + booktitle);
        SharedPreferences.Editor editor = userdata.edit();
        Gson gson = new Gson();
        bookingModels.add(new BookingModel(cartid, booktitle, s_date, e_date, seates));
        String json = gson.toJson(bookingModels);
        editor.putString("bookdetails", json);
        editor.apply();
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

        readFormat = new SimpleDateFormat("dd-MMM-yyyy h:mm a");
        writeFormat = new SimpleDateFormat("MM/dd/yyyy h:mm a");

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

    @Override
    public int getItemCount() {
        return homeModels.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView headertxt, title, sun_txt, mon_txt, tue_txt, wed_txt, thur_txt, fri_txt, sat_txt, address,price_txt;
        ImageView homeicon;
        Button addbtn;
        ImageSlider image_slider;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headertxt = itemView.findViewById(R.id.hedaer_name);
            title = itemView.findViewById(R.id.title);
            sun_txt = itemView.findViewById(R.id.sun_txt);
            mon_txt = itemView.findViewById(R.id.mon_txt);
            tue_txt = itemView.findViewById(R.id.tue_txt);
            wed_txt = itemView.findViewById(R.id.wed_txt);
            thur_txt = itemView.findViewById(R.id.thr_txt);
            fri_txt = itemView.findViewById(R.id.fri_txt);
            sat_txt = itemView.findViewById(R.id.sat_txt);
            address = itemView.findViewById(R.id.address);
//            homeicon = itemView.findViewById(R.id.homeicon);
            addbtn = itemView.findViewById(R.id.addbtn);
            price_txt = itemView.findViewById(R.id.price_txt);
            image_slider = itemView.findViewById(R.id.image_clusterslider);


        }
    }


}
