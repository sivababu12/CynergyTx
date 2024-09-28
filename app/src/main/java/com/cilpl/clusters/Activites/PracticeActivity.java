package com.cilpl.clusters.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.cilpl.clusters.Activites.Interfaces.ApiSucessResponse;
import com.cilpl.clusters.Activites.Interfaces.CarteResponse;
import com.cilpl.clusters.Adapters.ChairAdapter;
import com.cilpl.clusters.Adapters.NameAdapter;
import com.cilpl.clusters.Adapters.TimeAdapter;
import com.cilpl.clusters.Database.SqlLiteDb;
import com.cilpl.clusters.Model.BookModel;
import com.cilpl.clusters.Model.ChairSlotModel;
import com.cilpl.clusters.Model.NameDetails;
import com.cilpl.clusters.Model.TimeSlotsModel;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.NetworkCalling.NetworkServiceCall;
import com.cilpl.clusters.NetworkCalling.OnServiceCallCompleteListener;
import com.cilpl.clusters.NetworkCalling.ServerURL;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;
import com.cilpl.clusters.databinding.ActivityPracticeBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PracticeActivity extends AppCompatActivity {
    private ActivityPracticeBinding binding;
    public static NameAdapter nameAdapter;
    List<NameDetails> list;
    private ArrayList<TimeSlotsModel> timeSlotsModels;
    private ArrayList<ChairSlotModel> chairSlotModels;
    private int mYear, mMonth, mDay;
    TimeAdapter adapter;
    SQLiteDatabase database;
    SharedPreferences userdata;
    HelperClass helperClass;
    ApiDataSync dataSync;
    NestedScrollView scrollview;

    String DB_PATH, dbpath, DB_NAME = SqlLiteDb.DATABASE_NAME, bookmodel = "", book_Id, page, book_titile;
    LinearLayoutManager layoutManager;
    private ArrayList<BookModel> bookModels;
    String authtoken = "", startdate = "", timelist = "0", chairlist = "0", date = "", time_list = "", chair_list = "", slotcount = "", chair_count = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPracticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//
        list = new ArrayList<NameDetails>();
        TimeAdapter.timeslots = new ArrayList<>();
        ChairAdapter.chairSlots = new ArrayList<>();

//        Date d = new Date();
//        CharSequence s  = DateFormat.format("dd-MM-yyyy ", d.getTime());
//
//        String name = (String) s;
//
//        NameDetails nameDetails = new NameDetails(name);
//        list.add(nameDetails);
        initViews();
        onClicks();
    }

    private void onClicks() {
        binding.backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.slots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Log.e("testing", binding.slots.getText().toString());
            }
        });
        binding.calanderInt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeAdapter.timeslots.clear();
                ChairAdapter.chairSlots.clear();
              //  Log.e("positionname", binding.startDate.getText().toString());
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(PracticeActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        startdate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        binding.startDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        if (helperClass.checkInternetConnection(PracticeActivity.this)) {

                            SendDate(authtoken, startdate, book_Id);


                        } else {
                            Toast.makeText(PracticeActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();
                        }


                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(c.getTimeInMillis());
                dpd.show();
            }
        });

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                if (!binding.startDate.getText().toString().equals("Start Date")) {
                    String name = "11-04-2024";
                    binding.startDate.setText("Start Date");
                    binding.slots.setVisibility(View.GONE);
                    binding.slots.setText("");
                    NameDetails nameDetails = new NameDetails(startdate);
                    list.add(nameDetails);
                    TimeAdapter.timeslots.clear();
                    ChairAdapter.chairSlots.clear();
                    nameAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(PracticeActivity.this, "Please Select Date", Toast.LENGTH_SHORT).show();
                }


            }
        });
        binding.slotSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (helperClass.checkInternetConnection(PracticeActivity.this)) {
                    CarteResponse carteResponse = (status,count) -> {
                        //insertEventDB(name, payload);
                        if (status.equals("1")) {
                            //Log.e("cartcount",status+","+count);
                            HomeMainActivity.cartcount.setText(count);
                            if(page.equals("home")){
                                Intent i = new Intent(PracticeActivity.this,HomeMainActivity.class);
                                startActivity(i);
                            }else {
                                Intent i = new Intent(PracticeActivity.this,CartActivity.class);
                                startActivity(i);
                            }

//                            finish();
                        } else {
                            Toast.makeText(PracticeActivity.this, "Please try Again", Toast.LENGTH_SHORT).show();

                        }
                    };
                    dataSync.OpenDB(book_Id, bookmodel, book_titile, carteResponse);
                } else {
                    Toast.makeText(PracticeActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void SendDate(String authtoken, String startdate, String book_Id) {
        try {
            try {
                String dateapi = ServerURL.getSlots();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("authToken", authtoken);
                hashMap.put("date", startdate);
                hashMap.put("product_id", book_Id);
             //   Log.e("bookingslot", dateapi + "," + authtoken + "," + startdate + "," + book_Id);
                NetworkServiceCall serviceCall = new NetworkServiceCall(this, true);
                serviceCall.setOnServiceCallCompleteListener(new bookslotCompleteListener());
                serviceCall.makeJSONObjectPostStringRequest(dateapi, hashMap, Request.Priority.HIGH);
                // delete total files in folder
                //dletefiles();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("Range")
    private void OpenDB(String startdate) {
        bookModels = new ArrayList<>();
        try {
            database = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
            String query = "SELECT * FROM bookingdates";
           // Log.e("opendatabase", query);
            database = new SqlLiteDb(this).getReadableDatabase();
            Cursor cursor = database.rawQuery(query, null);
            ArrayList<String> list1 = new ArrayList<>();
            ArrayList<String> chairlist_1 = new ArrayList<>();
            while (cursor.moveToNext()) {
                slotcount = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_no_count)));
                time_list = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_times)));
                chair_list = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_chairlist)));
                chair_count = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_chaircount)));
                date = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_date)));

                BookModel model = new BookModel();
                model.setNo_ofslots(slotcount);
                model.setBookdate(date);
                model.setChairslots(chair_list);
                model.setNo_ofchairs(chair_count);
                model.setTimeslots(time_list);
                bookModels.add(model);
              //  Log.e("opendatabase", slotcount + "," + timelist + "," + chairlist + "," + chair_count + "," + date);


            }
            SqlLiteDb db = new SqlLiteDb(this);
            db.deleteAll();
            database.close();


            try {
                String senddata = ServerURL.GetSlotData();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("authToken", authtoken);
                hashMap.put("product_id", book_Id);
                hashMap.put("booking_type", bookmodel);
                hashMap.put("name", book_titile);

                final JSONObject mainobject = new JSONObject();
                final JSONArray array = new JSONArray();

                for (int i = 0; i < bookModels.size(); i++) {
                    BookModel model = new BookModel();
                    final JSONObject object1 = new JSONObject();
                 //   Log.e("bookmodelsarray", bookModels.get(i).getBookdate());

//                   final JSONObject object2 = new JSONObject();
                    object1.put("date", bookModels.get(i).getBookdate());
                    object1.put("slotcount", bookModels.get(i).getNo_ofslots());
                    object1.put("chairlist", bookModels.get(i).getChairslots());
                    object1.put("timeslot", bookModels.get(i).getTimeslots());
                    array.put(object1);

                }
                mainobject.put("Data", array);
                hashMap.put("data", array.toString());
               // Log.e("PracticeAcitivty", senddata + "," + authtoken + "," + array.toString() + "," + book_titile + "," + book_Id + "," + bookmodel);
                NetworkServiceCall serviceCall = new NetworkServiceCall(this, true);
                serviceCall.setOnServiceCallCompleteListener(new SubmitData());
                serviceCall.makeJSONObjectPostStringRequest(senddata, hashMap, Request.Priority.HIGH);
            } catch (Exception e) {
                e.printStackTrace();

            }

            finish();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PracticeActivity.this, HomeMainActivity.class);
//        database.execSQL("DROP TABLE IF EXISTS bookingdates");
        startActivity(i);
    }

    private void popUP(ArrayList<TimeSlotsModel> timeSlotsModels, ArrayList<ChairSlotModel> chairSlotModels) {
        final Dialog dialog = new Dialog(PracticeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.timelist);
        dialog.setCanceledOnTouchOutside(false);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        RecyclerView languages_recycle = dialog.findViewById(R.id.time_recycle);
        Button submit_time = dialog.findViewById(R.id.submit_time);
        ImageView cancel_btn = dialog.findViewById(R.id.cancelbtn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeAdapter.timeslots.clear();
                ChairAdapter.chairSlots.clear();
                dialog.dismiss();
            }
        });


        submit_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Log.e("praticeactivity", TimeAdapter.timeslots.toString() + "," + startdate);
                if (!startdate.equals("") || !startdate.equals("Start Date")) {
                    // OpenDB(startdate,TimeAdapter.timeslots.toString(),TimeAdapter.timeslots.size(),"1","0",0);

                    // binding.slots.setText("("+TimeAdapter.timeslots.size()+" Slots)");
                    dialog.dismiss();
                    if (bookmodel.equals("WS")) {
                        ChairPopuop(chairSlotModels);
                    } else {

//confernceroom and mini confernace room code
                        // SendSlotData(startdate,TimeAdapter.timeslots.toString(),TimeAdapter.timeslots.size(),"1","0",0);
                        dataSync.SendSlotData(startdate, TimeAdapter.timeslots.toString(), TimeAdapter.timeslots.size(), "1", "0", 0, "0");


                        // InsertInotDB(startdate,TimeAdapter.timeslots.toString(),TimeAdapter.timeslots.size(),"1","0",0,"");
                        binding.slots.setVisibility(View.VISIBLE);
                        binding.slots.setText(  TimeAdapter.timeslots.size() + " Slots");
                    }

                } else {
                    binding.slots.setVisibility(View.GONE);
                    binding.slots.setText("");
                }

            }
        });

        layoutManager = new GridLayoutManager(this, 3);
        adapter = new TimeAdapter(this, timelist, startdate, timeSlotsModels);
        languages_recycle.setLayoutManager(layoutManager);
        languages_recycle.setAdapter(adapter);
        dialog.show();
    }

    private void ChairPopuop(ArrayList<ChairSlotModel> chairSlotModels) {
        final Dialog dialog = new Dialog(this);
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

                dataSync.SendSlotData(startdate, TimeAdapter.timeslots.toString(), TimeAdapter.timeslots.size(), "1", ChairAdapter.chairSlots.toString(), ChairAdapter.chairSlots.size(), "0");
//                binding.addBtn.setVisibility(View.VISIBLE);
                binding.slots.setVisibility(View.VISIBLE);
                binding.slots.setText("(" + ChairAdapter.chairSlots.size() + " Chairs)");
                dialog.dismiss();
            }
        });

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 4);
        ChairAdapter adapter = new ChairAdapter(this, startdate, chairlist, chairSlotModels);
        chair_recycle.setLayoutManager(layoutManager);
        chair_recycle.setAdapter(adapter);
        dialog.show();
    }

    private void SendSlotData(String startdate, String timeslots, int timeslotssize, String status, String chairslots, int chairslotssize) {
        try {
            String senddata = ServerURL.GetSlotData();
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("authToken", authtoken);


            final JSONObject mainobject = new JSONObject();
            final JSONArray array = new JSONArray();


            final JSONObject object1 = new JSONObject();


//                   final JSONObject object2 = new JSONObject();
            object1.put("date", startdate);
            object1.put("timeslot", timeslots);
            object1.put("slotcount", timeslotssize);
            object1.put("chairlist", chairslots);

            array.put(object1);


            mainobject.put("Data", array);
          //  Log.e("printlist", array.toString());
            hashMap.put("data", array.toString());
          //  Log.e("PracticeAcitivty", senddata + "," + authtoken + "," + array.toString());
            NetworkServiceCall serviceCall = new NetworkServiceCall(this, true);
            serviceCall.setOnServiceCallCompleteListener(new SendBookdataCompleteListener(startdate, timeslots, timeslotssize, status, chairslots, chairslotssize));
            serviceCall.makeJSONObjectPostStringRequest(senddata, hashMap, Request.Priority.HIGH);
            // delete total files in folder
            //dletefiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InsertInotDB(String startdate, String timelist, int size, String status, String chairlist, int chaircount, String recorded) {
        //Log.e("datesselected", "opened");

        database = new SqlLiteDb(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlLiteDb.ctx_date, startdate);
        values.put(SqlLiteDb.ctx_times, timelist);
        values.put(SqlLiteDb.ctx_no_count, size);
        values.put(SqlLiteDb.ctx_status, status);
        values.put(SqlLiteDb.ctx_chairlist, chairlist);
        values.put(SqlLiteDb.ctx_chaircount, chaircount);
        values.put(SqlLiteDb.ctx_recoredId, recorded);

        database.replace(SqlLiteDb.PERSON_TABLE_NAME, null, values);
       // Log.e("datesselected", startdate + "," + timelist + "," + size + "," + status + "," + chairlist + "," + chaircount + "," + recorded);

        database.close();

    }

    private void initViews() {
        if (android.os.Build.VERSION.SDK_INT >= 30) {
            DB_PATH = getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = getFilesDir().getParentFile().getPath() + "/databases/";
        }
        dbpath = DB_PATH + DB_NAME;
        userdata = getSharedPreferences("Userdata", MODE_PRIVATE);
        authtoken = userdata.getString("authToken", "");
        helperClass = new HelperClass(this);
        dataSync = new ApiDataSync(this);
        // LOCalDC();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bookmodel = bundle.getString("book_model");
            book_Id = bundle.getString("book_id");
            book_titile = bundle.getString("book_title");
            page = bundle.getString("page");
           // Log.e("bookingmodel", bookmodel + "," + book_Id + "," + book_titile);

        }
        binding.texname.setText(book_titile);
        nameAdapter = new NameAdapter(this, list, bookmodel, book_Id);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(nameAdapter);

    }
    @SuppressLint("Range")
    private void LOCalDC() {

        try {
            database = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
            String query = "SELECT * FROM bookingdates ";
           // Log.e("selectQuery", query);
            database = new SqlLiteDb(this).getReadableDatabase();
            Cursor cursor = database.rawQuery(query, null);
            while (cursor.moveToNext()) {

                String gid = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_no_count)));
                String timelist = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_times)));
                String datelist = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_date)));

                NameDetails nameDetails = new NameDetails(datelist);
                list.add(nameDetails);

               // Log.e("substatus", gid + "," + "status=" + gid);
                database.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        nameAdapter = new NameAdapter(this, list, bookmodel, book_Id);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setReverseLayout(true);
//        manager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(manager);

        binding.recyclerView.setAdapter(nameAdapter);
    }

    private class bookslotCompleteListener implements OnServiceCallCompleteListener {
        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            timeSlotsModels = new ArrayList<>();
            chairSlotModels = new ArrayList<>();
            try {
             //   Log.e("bookingslot", jsonObject.toString());
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
                   // Log.e("bookingslot", array.toString());
                    JSONObject object = array1.getJSONObject(i);
                    ChairSlotModel model = new ChairSlotModel();
                    model.setChair(object.getString("chair"));
                    model.setStatus(object.getString("status"));
                    chairSlotModels.add(model);

                }
                if(bookmodel.equals("WS")){
                    ChairPopuop(chairSlotModels);
                }else {
                    popUP(timeSlotsModels, chairSlotModels);
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

    private class SendBookdataCompleteListener implements OnServiceCallCompleteListener {
        private String date1, timeslots, chairslots, status;
        int timesloysize, chairslotcount;

        public SendBookdataCompleteListener(String startdate, String timeslots, int timeslotssize, String chairslots, String status, int chairslotssize) {

            this.date1 = startdate;
            this.timeslots = timeslots;
            this.chairslots = chairslots;
            this.status = status;
            this.chairslotcount = chairslotssize;
            this.timesloysize = timeslotssize;
        }

        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            try {
             //   Log.e("PracticeAcitivty", jsonObject.toString());
                String status = jsonObject.getString("status");
                String errorcode = jsonObject.getString("errorCode");
                String message = jsonObject.getString("message");
                String recordid = jsonObject.getString("recordId");

                if (status.equals("1")) {
                    InsertInotDB(date1, timeslots, timesloysize, status, chairslots, chairslotcount, recordid);

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

    private class SubmitData implements OnServiceCallCompleteListener {
        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            try {
               // Log.e("submittotallist", jsonObject.toString());
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