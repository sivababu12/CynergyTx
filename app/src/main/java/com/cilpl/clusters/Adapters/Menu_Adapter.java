package com.cilpl.clusters.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.cilpl.clusters.Activites.AboutUsActivity;
import com.cilpl.clusters.Activites.ChangePassordActivity;
import com.cilpl.clusters.Activites.ContactUsActivity;
import com.cilpl.clusters.Activites.HomeMainActivity;
import com.cilpl.clusters.Activites.MyOrdersActivity;
import com.cilpl.clusters.NetworkCalling.ShowDialog;
import com.cilpl.clusters.R;


public class Menu_Adapter extends RecyclerView.Adapter<Menu_Adapter.ViewHolder> {
    Context context;

    SharedPreferences userdata;
    String selcted_lang;
    //private String[] sidemenulist = {"Invoices", "My Bookings", "About Us", "Contact Us", "Logout"};
    private final String[] sidemenulist = { "My Bookings", "About Us", "Contact Us","Privacy Policy","Change Password", "Logout"};
    private final int[] tabicons = { R.drawable.my_bookings, R.drawable.about_us, R.drawable.contact,R.drawable.privacy_policy,R.drawable.change_password, R.drawable.logout_};
//    R.drawable.invoices_icon,
    String authcode;
    private final int SINGLE_VIEW = 1;
    ShowDialog dialog;

    public Menu_Adapter(HomeMainActivity context) {
        this.context = context;


    }


    @NonNull
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
        dialog=new ShowDialog(context);
        holder.lname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String position = String.valueOf(holder.getAdapterPosition());
                Log.e("homemodules", position);

                switch (position) {

                    case "0": {
                        Intent i = new Intent(context, MyOrdersActivity.class);
                        i.putExtra("pageName", "My Bookings");
                        context.startActivity(i);
                        HomeMainActivity.menudialog.dismiss();
                        break;


                    }
                    case "1": {
                        Intent i = new Intent(context, AboutUsActivity.class);
                        i.putExtra("pageName", "About Us");
                        i.putExtra("pageurl", "https://cynergytx.com/aboutus_mob");
                        context.startActivity(i);
                        HomeMainActivity.menudialog.dismiss();
                        break;


                    }
                    case "2": {
                        Intent i = new Intent(context, ContactUsActivity.class);
                        i.putExtra("pageName", "Contact Us");
                        i.putExtra("pageurl", "https://cynergytx.com/contact_mob");
                        context.startActivity(i);
                        HomeMainActivity.menudialog.dismiss();
                        break;


                    }
                    case "3": {
                        Intent i = new Intent(context, AboutUsActivity.class);
                        i.putExtra("pageName", "Privacy Policy");
                        i.putExtra("pageurl", "https://cynergytx.com/privacy-policy_mob");
                        context.startActivity(i);
                        HomeMainActivity.menudialog.dismiss();
                        break;


                    }
                    case "4": {
                        Intent i = new Intent(context, ChangePassordActivity.class);
                        context.startActivity(i);
                        HomeMainActivity.menudialog.dismiss();
                        break;


                    }
                    case "5": {
                        dialog.ShowExitPopup("Are You Sure You want to Logout ?","1");
                        HomeMainActivity.menudialog.dismiss();
                        break;


                    }
                }


            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String position = String.valueOf(holder.getAdapterPosition());
                Log.e("homemodules", position);

                switch (position) {

                    case "0": {
                        Intent i = new Intent(context, MyOrdersActivity.class);
                        i.putExtra("pageName", "My Bookings");
                        context.startActivity(i);
                        HomeMainActivity.menudialog.dismiss();
                        break;


                    }
                    case "1": {
                        Intent i = new Intent(context, AboutUsActivity.class);
                        i.putExtra("pageName", "About Us");
                        i.putExtra("pageurl", "https://cynergytx.com/aboutus_mob");
                        context.startActivity(i);
                        HomeMainActivity.menudialog.dismiss();
                        break;


                    }
                    case "2": {
                        Intent i = new Intent(context, ContactUsActivity.class);
                        i.putExtra("pageName", "Contact Us");
                        i.putExtra("pageurl", "https://cynergytx.com/contact_mob");
                        context.startActivity(i);
                        HomeMainActivity.menudialog.dismiss();
                        break;


                    }
                    case "3": {
                        Intent i = new Intent(context, AboutUsActivity.class);
                        i.putExtra("pageName", "Privacy Policy");
                        i.putExtra("pageurl", "https://cynergytx.com/privacy-policy_mob");
                        context.startActivity(i);
                        HomeMainActivity.menudialog.dismiss();
                        break;


                    }
                    case "4": {

                        Intent i = new Intent(context, ChangePassordActivity.class);
                        context.startActivity(i);
                        HomeMainActivity.menudialog.dismiss();
                        break;


                    }
                    case "5": {
                        dialog.ShowExitPopup("Are You Sure You want to Logout ?","1");
                        HomeMainActivity.menudialog.dismiss();
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
        RelativeLayout menu_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lname = itemView.findViewById(R.id.lname);
            menuicon = itemView.findViewById(R.id.menuicon);
            menu_item = itemView.findViewById(R.id.menu_item);


        }
    }


}
