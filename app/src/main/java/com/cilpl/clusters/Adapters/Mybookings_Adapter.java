package com.cilpl.clusters.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cilpl.clusters.Model.BookingModel;
import com.cilpl.clusters.Model.MyBookingsModel;
import com.cilpl.clusters.R;

import java.util.ArrayList;


public class Mybookings_Adapter extends RecyclerView.Adapter<Mybookings_Adapter.ViewHolder> {
    Context context;

    SharedPreferences userdata;


    String authcode;

    ArrayList<MyBookingsModel> myBookingsModels;

    public Mybookings_Adapter(Context context, ArrayList<MyBookingsModel> myBookingsModels) {
        this.context = context;
        this.myBookingsModels = myBookingsModels;


    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.mybookings_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        userdata = context.getSharedPreferences("Userdata", context.MODE_PRIVATE);
        MyBookingsModel model = myBookingsModels.get(position);
        holder.lname.setText(model.getName());
        holder.s_date.setText( model.getFrom_date());
        holder.uname.setText( model.getFull_name());
        holder.phone.setText( model.getPhone());
        holder.email.setText( model.getEmail());


        switch (model.getBooking_type()) {
            case "cluster":
                holder.btype.setText("Cluster");
                holder.e_date.setVisibility(View.VISIBLE);
                holder.seates.setVisibility(View.GONE);
                holder.slotdates.setVisibility(View.GONE);
                holder.timeslotslay.setVisibility(View.GONE);
                holder.no_seatslay.setVisibility(View.GONE);
                holder.todatelotslay.setVisibility(View.VISIBLE);
                break;
            case "CR":
                holder.btype.setText("Conference Room");
                holder.timeslotslay.setVisibility(View.VISIBLE);
                holder.no_seatslay.setVisibility(View.GONE);
                holder.todatelotslay.setVisibility(View.GONE);
                break;
            case "MC":
                holder.btype.setText("Manger Cabin");
                holder.timeslotslay.setVisibility(View.GONE);
                holder.no_seatslay.setVisibility(View.GONE);
                holder.todatelotslay.setVisibility(View.VISIBLE);
                break;
            default:
                holder.btype.setText("Work Station");
                holder.todatelotslay.setVisibility(View.VISIBLE);
                holder.no_seatslay.setVisibility(View.VISIBLE);
                holder.timeslotslay.setVisibility(View.GONE);
                holder.e_date.setVisibility(View.VISIBLE);
                holder.seates.setVisibility(View.VISIBLE);
                holder.slotdates.setVisibility(View.VISIBLE);
                break;
        }
        holder.seates.setText( model.getChairs());
        holder.slotdates.setText( model.getTimeslots());
        holder.e_date.setText( model.getTo_date());
        holder.showmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.morelist.setVisibility(View.VISIBLE);
                holder.showmore.setVisibility(View.GONE);
                holder.showless.setVisibility(View.VISIBLE);
            }
        });
        holder.showless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.morelist.setVisibility(View.GONE);
                holder.showmore.setVisibility(View.VISIBLE);
                holder.showless.setVisibility(View.GONE);
            }
        });


    }


    @Override
    public int getItemCount() {
        return myBookingsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView lname, s_date, seates, e_date, phone, btype, email, uname, showmore, showless, slotdates;
        ImageButton imageButton;
        RelativeLayout cartrow;
        LinearLayout morelist,no_seatslay,timeslotslay,todatelotslay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lname = itemView.findViewById(R.id.title1);
            s_date = itemView.findViewById(R.id.sdate);
            e_date = itemView.findViewById(R.id.edate);
            seates = itemView.findViewById(R.id.sates);
            imageButton = itemView.findViewById(R.id.imageButton);
            cartrow = itemView.findViewById(R.id.cartrow1);
            phone = itemView.findViewById(R.id.phone);
            btype = itemView.findViewById(R.id.btype);
            email = itemView.findViewById(R.id.email);
            uname = itemView.findViewById(R.id.uname);
            morelist = itemView.findViewById(R.id.morelist);
            showmore = itemView.findViewById(R.id.showmore);
            showless = itemView.findViewById(R.id.showless);
            slotdates = itemView.findViewById(R.id.slotdates);
            no_seatslay = itemView.findViewById(R.id.no_seatslay);
            timeslotslay = itemView.findViewById(R.id.timeslotslay);
            todatelotslay = itemView.findViewById(R.id.todatelotslay);


        }
    }


}
