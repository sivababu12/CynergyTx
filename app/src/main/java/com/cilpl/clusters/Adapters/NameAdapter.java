package com.cilpl.clusters.Adapters;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.cilpl.clusters.Activites.PracticeActivity;
import com.cilpl.clusters.Database.SqlLiteDb;
import com.cilpl.clusters.Model.ChairSlotModel;
import com.cilpl.clusters.Model.NameDetails;
import com.cilpl.clusters.Model.TimeSlotsModel;
import com.cilpl.clusters.NetworkCalling.NetworkServiceCall;
import com.cilpl.clusters.NetworkCalling.OnServiceCallCompleteListener;
import com.cilpl.clusters.NetworkCalling.ServerURL;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class NameAdapter extends RecyclerView.Adapter<NameAdapter.NameHolder> {
    Context context;

    List<NameDetails> list;
    LinearLayoutManager layoutManager;
    TimeAdapter adapter;
    SQLiteDatabase database;
    SharedPreferences userdata;
    String DB_PATH, dbpath, DB_NAME = SqlLiteDb.DATABASE_NAME;
    private int mYear, mMonth, mDay;
    TextView slotstv;
    String startdate = "", bookingmodel = "", book_Id = "", authtoken, timelist = "0", chairlist = "0", recoredid = "";

    public ImageView add_btn, cancel_btn;
    ArrayList<ChairSlotModel> chairSlotModels;
    ApiDataSync apiDataSync;
    private ArrayList<TimeSlotsModel> timeSlotsModels = new ArrayList<>();


    public NameAdapter(PracticeActivity practiceActivity, List<NameDetails> list, String bookmodel, String book_Id) {
        this.context = practiceActivity;
        this.list = list;
        this.book_Id = book_Id;
        this.bookingmodel = bookmodel;
    }

    @NonNull
    @Override
    public NameAdapter.NameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.add_item, null);
        return new NameHolder(view);
    }

    @Override
    @SuppressLint("Range")
    public void onBindViewHolder(@NonNull NameAdapter.NameHolder holder, int position) {
        if (android.os.Build.VERSION.SDK_INT >= 30) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = context.getFilesDir().getParentFile().getPath() + "/databases/";
        }
        dbpath = DB_PATH + DB_NAME;
        apiDataSync = new ApiDataSync(context);
        userdata = context.getSharedPreferences("Userdata", context.MODE_PRIVATE);
        authtoken = userdata.getString("authToken", "");
        NameDetails nameDetails = list.get(position);
        //TimeAdapter.timeslots = new ArrayList<>();
        holder.textView.setText(nameDetails.getBooktitile());
        if (!holder.textView.getText().toString().equals("Start Date")) {
//            OpenDB(nameDetails.getBooktitile());

            try {
                database = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
                String query = "SELECT * FROM bookingdates WHERE _date='" + nameDetails.getBooktitile() + "'";
                // Log.e("selectssQuery", query);
                database = new SqlLiteDb(context).getReadableDatabase();
                Cursor cursor = database.rawQuery(query, null);
                ArrayList<String> list1 = new ArrayList<>();
                ArrayList<String> chairlist_1 = new ArrayList<>();
                while (cursor.moveToNext()) {
                    String gid = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_no_count)));
                    String timelist = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_times)));
                    String chairlist = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_chairlist)));
                    String chaircount = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_chaircount)));
                    startdate = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_date)));
                    TimeSlotsModel model = new TimeSlotsModel();
                    model.setTime(timelist);
                    model.setStatus("2");
                    // Log.e("listofselectedchairs", chairlist + "," + chaircount);
                    list1.add(timelist);
                    chairlist_1.add(chairlist);
                    if (bookingmodel.equals("WS")) {
                        slotstv.setText(chaircount + " Chairs");
                    } else {
                        slotstv.setText(gid + " Slots");
                    }


                    slotstv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //       apiDataSync.EditSlotDetails(nameDetails.getBooktitile(),book_Id,position,bookingmodel);

                            String query = "SELECT * FROM bookingdates WHERE _date='" + nameDetails.getBooktitile() + "'";
                            // Log.e("edistslots", query);
                            database = new SqlLiteDb(context).getReadableDatabase();
                            Cursor cursor = database.rawQuery(query, null);
                            ArrayList<String> list1 = new ArrayList<>();
                            ArrayList<String> chairlist_1 = new ArrayList<>();
                            while (cursor.moveToNext()) {
                                String timelist = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_times)));
                                String chairlist = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_chairlist)));
                                recoredid = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_recoredId)));
                                list1.add(timelist);
                                chairlist_1.add(chairlist);
                                TimeSlotsModel model = new TimeSlotsModel();
                                model.setTime(timelist);
                                model.setStatus("2");
                                //   Log.e("edistslots", chairlist + "," + timelist);
                                timeSlotsModels.add(model);
                                try {
                                    String dateapi = ServerURL.GetSlotinfo();
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("authToken", authtoken);
                                    hashMap.put("date", startdate);
                                    hashMap.put("product_id", book_Id);
                                    hashMap.put("recordId", recoredid);
                                    //  Log.e("bookingslot", dateapi+","+authtoken+","+startdate+","+book_Id+","+recoredid);
                                    NetworkServiceCall serviceCall = new NetworkServiceCall(context, true);
                                    serviceCall.setOnServiceCallCompleteListener(new bookslotCompleteListener(position));
                                    serviceCall.makeJSONObjectPostStringRequest(dateapi, hashMap, Request.Priority.HIGH);
                                    // delete total files in folder
                                    //dletefiles();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


//                                ChairAdapter.chairSlots.clear();
//                                // ChairAdapter.chairSlots.addAll(chairlist_1);
//                                TimeAdapter.timeslots.clear();
//                                TimeAdapter.timeslots.addAll(list1);
//                                popUP(timelist, nameDetails.getBooktitile(), position, "0",timeSlotsModels);
                            }

                        }
                    });

                    //  Log.e("substatus", gid + "," + "status=" + gid);
                    database.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        // Log.e("dataaa", nameDetails.getBooktitile());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log.e("positionname", holder.textView.getText().toString());
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        startdate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        holder.textView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        popUP("0", nameDetails.getBooktitile(), position, "0", timeSlotsModels);

                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(c.getTimeInMillis());
                dpd.show();


            }
        });


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               NameDetails nameDetails = list.get(position);
//
//               holder.textView.setText(nameDetails.getBooktitile());
//               Log.e("dataaa",nameDetails.getBooktitile());
//               NameDetails nameDetails = list.get(position);
//               add_btn.setVisibility(View.GONE);
//               cancel_btn.setVisibility(View.VISIBLE);
                NameDetails newValue = new NameDetails(holder.textView.getText().toString());
                newValue.setBooktitile(holder.textView.getText().toString());

                //    Log.e("position", String.valueOf(position));
                list.add(newValue);
                notifyDataSetChanged();


            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               add_btn.setVisibility(View.VISIBLE);
//               cancel_btn.setVisibility(View.GONE);
                NameDetails newValue = new NameDetails(list.get(position).getBooktitile());
//               newValue.setBooktitile(holder.textView.getText().toString());

                //  Log.e("position01", String.valueOf(holder.textView.getText().toString()));
                list.remove(list.get(position));
                dELETEDATE(holder.textView.getText().toString());
                notifyDataSetChanged();
            }
        });


    }

    private void dELETEDATE(String id) {

        database = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
        String query = "SELECT * FROM bookingdates";
        database = new SqlLiteDb(context).getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()) {

            @SuppressLint("Range") String messgegidid = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_date)));

            //  Log.e("pppppppp", messgegidid);
            if (id.equals(messgegidid)) {
                //   Log.e("remove", id + "," + messgegidid + ",,," + "yes");

                String query1 = "DELETE  FROM bookingdates WHERE _date ='" + messgegidid + "'";
                database.execSQL(query1);
                //  Log.e("deletecorporate", query1);
            } else {
                //  Log.e("remove", id + "," + messgegidid + ",,," + "Notmaching");
            }


        }
        database.close();


    }

    @SuppressLint("Range")
    private void OpenDB(String booktitile) {

    }

    private void popUP(String timelist, String startdate, int position, String chairlist, ArrayList<TimeSlotsModel> timeSlotsModels) {
        //  Log.e("chairlist", chairlist);


        final Dialog dialog = new Dialog(context);
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
                dialog.dismiss();
            }
        });


        submit_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log.e("slottimelist", TimeAdapter.timeslots.toString() + "," + startdate);
                List<String> flatList = new ArrayList<>();

// Iterate through the timeslots list
                for (String slot : TimeAdapter.timeslots) {
                    // Check if the slot is a single time slot or a list of time slots
                    if (slot.startsWith("[") && slot.endsWith("]")) {
                        // If it's a list, remove the brackets and split by comma to get individual slots
                        String[] slotArray = slot.substring(1, slot.length() - 1).split(", ");
                        // Add each slot from the array to the flat list
                        flatList.addAll(Arrays.asList(slotArray));
                    } else {
                        // If it's a single slot, just add it to the flat list
                        flatList.add(slot);
                    }
                }
                // Log.e("bookmodel", bookingmodel);
                dialog.dismiss();
                //for work station code
                if (bookingmodel.equals("WS")) {
                    ChairPopuop(startdate, flatList, flatList.size(), position, chairlist);
//                    dialog.dismiss();
                } else {
                    List<String> chairlist = new ArrayList<>();
                    UPdateList(startdate, flatList, flatList.size(), position, chairlist, 0);
                    dialog.dismiss();
                }


            }
        });

        layoutManager = new GridLayoutManager(context, 4);
        adapter = new TimeAdapter(context, timelist, startdate, timeSlotsModels);
        languages_recycle.setLayoutManager(layoutManager);
        languages_recycle.setAdapter(adapter);
        dialog.show();


    }

    private void ChairPopuop(String startdate, List<String> f1, int f1size, int position, String chairlist) {
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

                // Log.e("slottimelist_02", ChairAdapter.chairSlots.toString() + "," + startdate);
                List<String> flatList = new ArrayList<>();

// Iterate through the timeslots list
                for (String slot : ChairAdapter.chairSlots) {
                    // Check if the slot is a single time slot or a list of time slots
                    if (slot.startsWith("[") && slot.endsWith("]")) {
                        // If it's a list, remove the brackets and split by comma to get individual slots
                        String[] slotArray = slot.substring(1, slot.length() - 1).split(", ");
                        // Add each slot from the array to the flat list
                        flatList.addAll(Arrays.asList(slotArray));
                    } else {
                        // If it's a single slot, just add it to the flat list
                        flatList.add(slot);
                    }
                }

                UPdateList(startdate, f1, f1size, position, flatList, flatList.size());


                dialog.dismiss();
            }
        });

        LinearLayoutManager layoutManager = new GridLayoutManager(context, 4);
        ChairAdapter adapter = new ChairAdapter(context, startdate, chairlist, chairSlotModels);
        chair_recycle.setLayoutManager(layoutManager);
        chair_recycle.setAdapter(adapter);
        dialog.show();
    }

    private void UPdateList(String startdate, List<String> timeslots, int size, int position, List<String> chairlist, int chaircount) {
        //   Log.e("TAG", "UPdateList: " + timeslots.size());
        //  Log.e("chairlist", timeslots + "," + chairlist);
        if (bookingmodel.equals("WS")) {
            if (timeslots.size() == 0) {
                list.remove(list.get(position));
                dELETEDATE(startdate);
                TimeAdapter.timeslots.clear();
                notifyDataSetChanged();
            } else if (chairlist.size() == 0) {
                list.remove(list.get(position));
                dELETEDATE(startdate);
                ChairAdapter.chairSlots.clear();
                notifyDataSetChanged();
            }
        } else if (!bookingmodel.equals("WS")) {
            if (timeslots.size() == 0) {
                list.remove(list.get(position));
                dELETEDATE(startdate);
                TimeAdapter.timeslots.clear();
                notifyDataSetChanged();
            }
        }
        try {
            database = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
            String query = "UPDATE bookingdates set  _timelist= '" + timeslots + "',_slotscount = '" + size + "',_chairlist='" + chairlist + "',_chaircount='" + chaircount + "' WHERE _date='" + startdate + "'";
            database.execSQL(query);
            //    Log.e("updateQuery", query);
            notifyDataSetChanged();
            TimeAdapter.timeslots.clear();
            ChairAdapter.chairSlots.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NameHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RecyclerView timecycle;


        public NameHolder(@NonNull View itemView) {
            super(itemView);
            add_btn = (ImageView) itemView.findViewById(R.id.add_btn);
            cancel_btn = (ImageView) itemView.findViewById(R.id.cancel_btn);
            textView = itemView.findViewById(R.id.title1);
            slotstv = itemView.findViewById(R.id.slots);
            timecycle = (RecyclerView) itemView.findViewById(R.id.timecycle);

        }
    }

    private class bookslotCompleteListener implements OnServiceCallCompleteListener {
        int position;

        public bookslotCompleteListener(int position) {
            this.position = position;
        }

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
                ChairAdapter.chairSlots.clear();
                TimeAdapter.timeslots.clear();
                if (bookingmodel.equals("WS")) {
                    ChairPopuop2(chairSlotModels);
                } else {
                    popUP2(timeSlotsModels, chairSlotModels, position);
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

    private void popUP2(ArrayList<TimeSlotsModel> timeSlotsModels, ArrayList<ChairSlotModel> chairSlotModels, int position) {
        final Dialog dialog = new Dialog(context);
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
                //  Log.e("praticeactivity", TimeAdapter.timeslots.toString()+","+startdate);
                if (!startdate.equals("") || !startdate.equals("Start Date")) {

                    if (TimeAdapter.timeslots.size() == 0) {
                        list.remove(list.get(position));
                        dELETEDATE(startdate);
                        TimeAdapter.timeslots.clear();
                        notifyDataSetChanged();
                    } else {
                        if (bookingmodel.equals("WS")) {
                            ChairPopuop2(chairSlotModels);
                        } else {


                            apiDataSync.SendSlotData(startdate, TimeAdapter.timeslots.toString(), TimeAdapter.timeslots.size(), "1", "0", 0, recoredid);

                        }

                        //     Log.e("timeupdate",TimeAdapter.timeslots.size()+",");
                        slotstv.setText("(" + TimeAdapter.timeslots.size() + " Slots)");
                    }

                    dialog.dismiss();


                }

            }
        });

        layoutManager = new GridLayoutManager(context, 4);
        adapter = new TimeAdapter(context, timelist, startdate, timeSlotsModels);
        languages_recycle.setLayoutManager(layoutManager);
        languages_recycle.setAdapter(adapter);
        dialog.show();
    }

    private void ChairPopuop2(ArrayList<ChairSlotModel> chairSlotModels) {
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
                //    Log.e("praticeactivity", ChairAdapter.chairSlots.toString()+","+startdate+",size="+ChairAdapter.chairSlots.size());

                apiDataSync.SendSlotData(startdate, TimeAdapter.timeslots.toString(), TimeAdapter.timeslots.size(), "1", ChairAdapter.chairSlots.toString(), ChairAdapter.chairSlots.size(), recoredid);

//                    binding.slots.setVisibility(View.VISIBLE);


                slotstv.setText("(" + ChairAdapter.chairSlots.size() + " Chairs)");
                //  Log.e("timeupdate",TimeAdapter.timeslots.size()+","+ChairAdapter.chairSlots.size());


                dialog.dismiss();
            }
        });

        LinearLayoutManager layoutManager = new GridLayoutManager(context, 4);
        ChairAdapter adapter = new ChairAdapter(context, startdate, chairlist, chairSlotModels);
        chair_recycle.setLayoutManager(layoutManager);
        chair_recycle.setAdapter(adapter);
        dialog.show();
    }
}

