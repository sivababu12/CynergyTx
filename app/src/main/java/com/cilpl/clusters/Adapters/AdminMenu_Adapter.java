package com.cilpl.clusters.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cilpl.clusters.Activites.BooknowActivity;
import com.cilpl.clusters.Activites.HomeMainActivity;
import com.cilpl.clusters.Activites.LoginActivity;
import com.cilpl.clusters.Activites.UserManagementActivity;
import com.cilpl.clusters.R;


public class AdminMenu_Adapter extends RecyclerView.Adapter<AdminMenu_Adapter.ViewHolder> {
    Context context;

    SharedPreferences userdata;
    String selcted_lang;
    //private String[] sidemenulist = {"Invoices", "My Bookings", "About Us", "Contact Us", "Logout"};
    private String[] sidemenulist = {"Bookings", "User Management", "Support","Requests","Reports", "Logout"};
    private int[] tabicons = { R.drawable.my_bookings, R.drawable.tempimg, R.drawable.support_1,R.drawable.request_latest,R.drawable.reports, R.drawable.logout_};
//    R.drawable.invoices_icon,
    String authcode;
    private final int SINGLE_VIEW = 1;

    public AdminMenu_Adapter(HomeMainActivity context) {
        this.context = context;


    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.langauge_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        userdata = context.getSharedPreferences("Userdata", context.MODE_PRIVATE);
        holder.menuicon.setImageResource(tabicons[position]);
        holder.lname.setText(sidemenulist[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String position = String.valueOf(holder.getAdapterPosition());
                Log.e("homemodules", position);

                switch (position) {

                    case "0": {
                        Intent i = new Intent(context, BooknowActivity.class);
                        i.putExtra("pageName", "Book now");
                        i.putExtra("pageurl", "https://cynergytx.com//admin/products");
                        context.startActivity(i);
                        break;


                    }
                    case "1": {
                        Intent i = new Intent(context, UserManagementActivity.class);
                        i.putExtra("pageName", "User Management");
                        i.putExtra("pageurl", "https://cynergytx.com//admin/viewagents.html");
                        context.startActivity(i);
                        break;


                    }
                    case "2": {
                        Intent i = new Intent(context, UserManagementActivity.class);
                        i.putExtra("pageName", "Support");
                        i.putExtra("pageurl", "https://cynergytx.com//admin/view_tickets.html");
                        context.startActivity(i);
                        break;


                    }
                    case "3": {
                        Intent i = new Intent(context, UserManagementActivity.class);
                        i.putExtra("pageName", "Requests");
                        i.putExtra("pageurl", "https://cynergytx.com//admin/booking-requests.html");
                        context.startActivity(i);
                        break;


                    }
                    case "4": {
                        Intent i = new Intent(context, UserManagementActivity.class);
                        i.putExtra("pageName", "Reports");
                        i.putExtra("pageurl", "https://cynergytx.com//admin/booking-requests.html");
                        context.startActivity(i);
                        break;


                    }
                    case "5": {
                        SharedPreferences.Editor editor = userdata.edit();
                        editor.clear();
                        editor.apply();
                        Intent i = new Intent(context, LoginActivity.class);
                        context.startActivity(i);
                        System.exit(0);
                        break;


                    }
                }


            }
        });

    }


    @Override
    public int getItemCount() {
        return sidemenulist.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lname;
        ImageView menuicon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lname = itemView.findViewById(R.id.lname);
            menuicon = itemView.findViewById(R.id.menuicon);


        }
    }


}
