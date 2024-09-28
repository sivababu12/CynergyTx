package com.cilpl.clusters.Adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cilpl.clusters.Database.SqlLiteDb;
import com.cilpl.clusters.Model.ChairSlotModel;
import com.cilpl.clusters.Model.NameDetails;
import com.cilpl.clusters.Model.TimeSlotsModel;
import com.cilpl.clusters.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.NameHolder>{
    Context context;

    List<NameDetails> list;
    SQLiteDatabase database;
    String DB_PATH, dbpath, DB_NAME = SqlLiteDb.DATABASE_NAME;
    public static ArrayList<String> timeslots;
    private String[] timelist = {"00-01", "01-02", "02-03", "03-04", "04-05",
            "05-06", "06-07", "07-08", "08-09","09-10","10-11","11-12","12-13",
            "13-14","14-15","15-16","16-17","17-18","19-20","20-21","21-22","22-23", "23-00"};

    LinearLayoutManager layoutManager;
    String timelists,startdate;
    ArrayList<TimeSlotsModel> timeSlotsModels;
    ArrayList<ChairSlotModel> chairSlotModels;

    public TimeAdapter(Context context, String timelist, String startdate, ArrayList<TimeSlotsModel> timeSlotsModels) {
        this.context = context;
        this.timelists=timelist;
        this.startdate=startdate;
        this.timeSlotsModels=timeSlotsModels;


    }

    @NonNull
    @Override
    public TimeAdapter.NameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.add_time,null);
        return new NameHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeAdapter.NameHolder holder, int position) {
        if (android.os.Build.VERSION.SDK_INT >= 30) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = context.getFilesDir().getParentFile().getPath() + "/databases/";
        }
        dbpath = DB_PATH + DB_NAME;
        TimeSlotsModel nameDetails = timeSlotsModels.get(position);
     //  Log.e("listodata",nameDetails.getTime()+","+nameDetails.getStatus());

//        holder.time_txt.setText(timelist[position]);
//        holder.time2_txt.setText(timelist[position]);
//        holder.time3_txt.setText(timelist[position]);
//        Log.e("dataaa",timelist[position]);

        if(nameDetails.getStatus().equals("0")){
            holder.time2_txt.setVisibility(View.GONE);
            holder.time3_txt.setVisibility(View.VISIBLE);
            holder.time_txt.setVisibility(View.GONE);
            holder.time3_txt.setText(nameDetails.getTime());
        }else if(nameDetails.getStatus().equals("2")) {
            holder.time2_txt.setVisibility(View.VISIBLE);
            holder.time2_txt.setText(nameDetails.getTime());
            holder.time_txt.setVisibility(View.GONE);
            holder.time3_txt.setVisibility(View.GONE);
            timeslots.add(holder.time2_txt.getText().toString());

        }
        else {
            holder.time2_txt.setVisibility(View.GONE);
            holder.time_txt.setVisibility(View.VISIBLE);
            holder.time3_txt.setVisibility(View.GONE);
            holder.time_txt.setText(nameDetails.getTime());
        }

            String data = timelists;
        if(!data.equals("0")){
            String[] items = data.split(",");
            for (String item : items)
            {
                String[] temp = item.replaceAll("[\\[\\]]", "").split("-@");
                // String test = item.replaceAll("[^a-zA-Z]", "");
                for(String tem:temp){

                    String  input = tem.replaceAll("\\s+", "");
                 //   Log.e("printlisttt",input);
                    if(timeSlotsModels.get(position).equals(input)){
                     //   Log.e("printlisttt1",input);
                        holder.time2_txt.setVisibility(View.VISIBLE);
                        holder.time_txt.setVisibility(View.GONE);


                    }
                }

            }
        }

//        String data1 = nameDetails.getTime();
//        if(!data1.equals("0")){
//            String[] items = data1.split("-");
//            for (String item : items) {
//                String[] temp = item.replaceAll("[\\[\\]]", "").split("-@");
//                // String test = item.replaceAll("[^a-zA-Z]", "");
//                for (String tem : temp) {
//
//                    String input = tem.replaceAll("\\s+", "");
//                    Log.e("printlisttt", input+":00");
//                    if (timeSlotsModels.get(position).equals(input)) {
//                        Log.e("printlisttt1", input+":00");
//
//
//
//                    }
//                }
//
//            }
//        }



        holder.time_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.time2_txt.setText(nameDetails.getTime());
                holder.time2_txt.setVisibility(View.VISIBLE);
                holder.time_txt.setVisibility(View.GONE);
                timeslots.add(holder.time_txt.getText().toString());
              //  Log.e("timeadapter","add--"+timeslots);
               // Log.e("positionname",holder.time_txt.getText().toString());
            }
        });

        holder.time2_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.time2_txt.setVisibility(View.GONE);
                holder.time_txt.setVisibility(View.VISIBLE);
                holder.time_txt.setText(nameDetails.getTime());
                timeSlotsModels.remove(holder.time2_txt.getText().toString());
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
                flatList.remove(holder.time_txt.getText().toString());
                timeslots.clear();
                timeslots.addAll(flatList);
              //  Log.e("TAG", "onClick_timeslots: "+timeslots );


//                try {
//                    database = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
//                    String query = "SELECT * FROM bookingdates WHERE _date='" + startdate + "'";
//                    Log.e("timeadapterquery", query);
//                    database = new SqlLiteDb(context).getReadableDatabase();
//                    Cursor cursor = database.rawQuery(query, null);
//                    while (cursor.moveToNext()) {
//                        String gid = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_no_count)));
//                        String timelist = (cursor.getString(cursor.getColumnIndex(SqlLiteDb.ctx_times)));
//                        Log.e("timeadapterquery", timelist);
//
//                        Log.e("substatus", gid + "," + "status=" + gid);
//                        database.close();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if(timelist.equals(holder.time_txt.getText().toString())){
//                    timeslots.remove(holder.time_txt.getText().toString());
//                }

            //    Log.e("timeadapterquery", String.valueOf(timeslots));
              //  Log.e("timeadapter", "delete--" + String.valueOf(timeslots.size()));
              //  Log.e("positionname", holder.time_txt.getText().toString());
            }

        });
    }

    @Override
    public int getItemCount() {
        return timeSlotsModels.size();
    }

    public class NameHolder extends RecyclerView.ViewHolder{
        TextView time_txt,time2_txt,time3_txt;
        RecyclerView timecycle;

        public NameHolder(@NonNull View itemView) {
            super(itemView);
            time_txt = (TextView) itemView.findViewById(R.id.time_txt);
            time2_txt = (TextView) itemView.findViewById(R.id.time2_txt);
            time3_txt = (TextView) itemView.findViewById(R.id.time3_txt);


        }
    }
}
