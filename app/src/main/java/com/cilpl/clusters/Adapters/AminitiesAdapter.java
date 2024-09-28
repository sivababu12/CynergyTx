package com.cilpl.clusters.Adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cilpl.clusters.Database.SqlLiteDb;
import com.cilpl.clusters.Model.NameDetails;
import com.cilpl.clusters.R;

import java.util.ArrayList;
import java.util.List;

public class AminitiesAdapter extends RecyclerView.Adapter<AminitiesAdapter.NameHolder>{
    Context context;

    List<NameDetails> list;
    SQLiteDatabase database;
    String DB_PATH, dbpath, DB_NAME = SqlLiteDb.DATABASE_NAME;
    String aminities;
    String[] items;
    List<String> Amnitiesarray;


    public AminitiesAdapter(Context context, ArrayList<String> aminities) {
        this.context = context;
        this.Amnitiesarray=aminities;


    }

    @NonNull
    @Override
    public AminitiesAdapter.NameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.aminities_item,null);
        return new NameHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AminitiesAdapter.NameHolder holder, int position) {
        if (android.os.Build.VERSION.SDK_INT >= 30) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = context.getFilesDir().getParentFile().getPath() + "/databases/";
        }
        dbpath = DB_PATH + DB_NAME;
        holder.aminities.setText(Amnitiesarray.get(position));













    }

    @Override
    public int getItemCount() {
        return Amnitiesarray.size();
    }

    public class NameHolder extends RecyclerView.ViewHolder{
        TextView aminities;

        public NameHolder(@NonNull View itemView) {
            super(itemView);
            aminities = (TextView) itemView.findViewById(R.id.aminities);



        }
    }
}
