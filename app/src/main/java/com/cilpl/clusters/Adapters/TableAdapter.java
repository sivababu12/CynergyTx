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
import com.cilpl.clusters.Model.NameDetails;
import com.cilpl.clusters.R;

import java.util.ArrayList;
import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.NameHolder>{
    Context context;

    List<NameDetails> list;
    SQLiteDatabase database;
    String DB_PATH, dbpath, DB_NAME = SqlLiteDb.DATABASE_NAME;

    private String[] timelist = {" ", "Standard", "Gold", "Platinum", "Business Address",
            "Yes", "yes", "Yes", "08-09","09-10","10-11","11-12","12-13",
            "13-14","14-15","15-16","16-17","17-18","19-20","20-21","21-22","22-23", "23-00"};

    LinearLayoutManager layoutManager;
    String timelists,startdate;
    public TableAdapter(Context context) {
        this.context = context;


    }

    @NonNull
    @Override
    public TableAdapter.NameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.table_item,null);
        return new NameHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableAdapter.NameHolder holder, int position) {
        if (android.os.Build.VERSION.SDK_INT >= 30) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = context.getFilesDir().getParentFile().getPath() + "/databases/";
        }
        dbpath = DB_PATH + DB_NAME;
        holder.time_txt.setText(timelist[position]);










    }

    @Override
    public int getItemCount() {
        return timelist.length;
    }

    public class NameHolder extends RecyclerView.ViewHolder{
        TextView time_txt;

        public NameHolder(@NonNull View itemView) {
            super(itemView);
            time_txt = (TextView) itemView.findViewById(R.id.time_txt);



        }
    }
}
