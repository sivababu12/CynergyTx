package com.cilpl.clusters.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cilpl.clusters.Activites.InvoiceVIiewActivity;
import com.cilpl.clusters.Database.SqlLiteDb;
import com.cilpl.clusters.Model.NameDetails;
import com.cilpl.clusters.R;

import java.util.List;

public class InvouiceeAdapter extends RecyclerView.Adapter<InvouiceeAdapter.NameHolder>{
    Context context;

    List<NameDetails> list;
    SQLiteDatabase database;
    String DB_PATH, dbpath, DB_NAME = SqlLiteDb.DATABASE_NAME;



    public InvouiceeAdapter(Context context) {
        this.context = context;


    }

    @NonNull
    @Override
    public InvouiceeAdapter.NameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.invoice_item,null);
        return new NameHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvouiceeAdapter.NameHolder holder, int position) {
        if (android.os.Build.VERSION.SDK_INT >= 30) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = context.getFilesDir().getParentFile().getPath() + "/databases/";
        }
        dbpath = DB_PATH + DB_NAME;
         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent i = new Intent(context, InvoiceVIiewActivity.class);
                 context.startActivity(i);
             }
         });










    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class NameHolder extends RecyclerView.ViewHolder{
        TextView time_txt;

        public NameHolder(@NonNull View itemView) {
            super(itemView);
            time_txt = (TextView) itemView.findViewById(R.id.time_txt);



        }
    }
}
