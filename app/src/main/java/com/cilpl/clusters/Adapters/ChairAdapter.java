package com.cilpl.clusters.Adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cilpl.clusters.Database.SqlLiteDb;
import com.cilpl.clusters.Model.ChairSlotModel;
import com.cilpl.clusters.Model.NameDetails;
import com.cilpl.clusters.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChairAdapter extends RecyclerView.Adapter<ChairAdapter.NameHolder> {
    Context context;

    List<NameDetails> list;
    SQLiteDatabase database;
    String DB_PATH, dbpath, DB_NAME = SqlLiteDb.DATABASE_NAME;
    public static ArrayList<String> chairSlots = new ArrayList<>();
    ArrayList<ChairSlotModel> chairSlotModels;
//    private String[] chair_list = {"D1", "D2", "D3", "D4", "D5",
//            "D6", "D7", "D8", "D9", "D10","D11", "D12", "D13", "D14", "D15",
//            "D16", "D17", "D18", "D19", "D20",
//            "D21", "D22", "D23", "D24", "D25",
//            "D26", "D27", "D28", "D29", "D30"};

    LinearLayoutManager layoutManager;
    String chair_lists, startdate;

    public ChairAdapter(Context context, String startdate, String chair_lists, ArrayList<ChairSlotModel> chairSlotModels) {
        this.context = context;
        this.chair_lists = chair_lists;
        this.chairSlotModels = chairSlotModels;
        this.startdate = startdate;

    }

    @NonNull
    @Override
    public ChairAdapter.NameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chair_item, null);
        return new NameHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChairAdapter.NameHolder holder, int position) {
        if (android.os.Build.VERSION.SDK_INT >= 30) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = context.getFilesDir().getParentFile().getPath() + "/databases/";
        }
        dbpath = DB_PATH + DB_NAME;
        ChairSlotModel model = chairSlotModels.get(position);
        if (model.getStatus().equals("0")) {
            holder.chair_03.setVisibility(View.VISIBLE);
            holder.chair_01.setVisibility(View.GONE);
            holder.chair_02.setVisibility(View.GONE);
            holder.chair3_txt.setText(model.getChair());
        } else {
            holder.chair_03.setVisibility(View.GONE);
            holder.chair_01.setVisibility(View.VISIBLE);
            holder.chair_02.setVisibility(View.GONE);
            holder.chair1_txt.setText(model.getChair());


        }
//        holder.chair1_txt.setText(chair_list[position]);
//        holder.chair2_txt.setText(chair_list[position]);
//        Log.e("dataaa",chair_list[position]);


//        String data = chair_lists;
//        if(!data.equals("0")){
//            String[] items = data.split(",");
//            for (String item : items)
//            {
//                String[] temp = item.replaceAll("[\\[\\]]", "").split("-@");
//                // String test = item.replaceAll("[^a-zA-Z]", "");
//                for(String tem:temp){
//
//                    String  input = tem.replaceAll("\\s+", "");
//                    Log.e("printlisttt",input);
//                    if(chairSlotModels.get(position).equals(input)){
//                        Log.e("printlisttt1",input);
//                        holder.chair_02.setVisibility(View.VISIBLE);
//                        holder.chair_01.setVisibility(View.GONE);
//                    }else {
////                        holder.time2_txt.setVisibility(View.GONE);
////                        holder.time_txt.setVisibility(View.VISIBLE);
//                    }
//                }
//
//            }
//        }


        holder.chair_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.chair_02.setVisibility(View.VISIBLE);
                holder.chair_01.setVisibility(View.GONE);
                holder.chair2_txt.setText(model.getChair());
                chairSlots.add(holder.chair1_txt.getText().toString());
              //  Log.e("chairadapter", "add--" + chairSlots);
               // Log.e("chairadapter", holder.chair1_txt.getText().toString());
            }
        });

        holder.chair_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.chair_02.setVisibility(View.GONE);
                holder.chair_01.setVisibility(View.VISIBLE);
                List<String> flatList = new ArrayList<>();

// Iterate through the chairSlots list
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
                flatList.remove(holder.chair1_txt.getText().toString());
                chairSlots.clear();
                chairSlots.addAll(flatList);
             //   Log.e("chairadapter", "onClick_chairSlots: " + chairSlots);


              //  Log.e("chairadapter", String.valueOf(chairSlots));
              //  Log.e("chairadapter", "delete--" + String.valueOf(chairSlots.size()));
                //Log.e("chairadapter", holder.chair1_txt.getText().toString());
            }

        });
    }

    @Override
    public int getItemCount() {
        return chairSlotModels.size();
    }

    public class NameHolder extends RecyclerView.ViewHolder {
        LinearLayout chair_01, chair_02, chair_03;
        RecyclerView timecycle;
        TextView chair1_txt, chair2_txt, chair3_txt;

        public NameHolder(@NonNull View itemView) {
            super(itemView);
            chair_01 = itemView.findViewById(R.id.chair_01);
            chair_02 = itemView.findViewById(R.id.chair_02);
            chair_03 = itemView.findViewById(R.id.chair_03);
            chair1_txt = itemView.findViewById(R.id.chair1_txt);
            chair2_txt = itemView.findViewById(R.id.chair2_txt);
            chair3_txt = itemView.findViewById(R.id.chair3_txt);


        }
    }
}
