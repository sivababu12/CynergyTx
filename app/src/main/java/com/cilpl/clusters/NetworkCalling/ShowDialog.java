package com.cilpl.clusters.NetworkCalling;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cilpl.clusters.Activites.CartActivity;
import com.cilpl.clusters.Activites.Interfaces.ApiSucessResponse;
import com.cilpl.clusters.Activites.LoginActivity;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;

public class ShowDialog {
    SQLiteDatabase database;
    String DB_PATH, dbpath, authcode, time = "", fcmid, uid;
    Context context;
    SharedPreferences userdata;
    ApiDataSync dataSync;

    public ShowDialog(Context context){
        this.context=context;
        userdata = context.getSharedPreferences("Userdata", context.MODE_PRIVATE);
        authcode = userdata.getString("authToken", "");
        fcmid = userdata.getString("notificationId", null) + "";
        uid = userdata.getString("user_id", null) + "";
        dataSync=new ApiDataSync(context);
    }

    public void ShowExitPopup(String s, String number) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER_HORIZONTAL;
        dialog.setContentView(R.layout.exit);
        dialog.setCanceledOnTouchOutside(true);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button alert_clncel = dialog.findViewById(R.id.alert_clncel);
        Button alert_Yes = dialog.findViewById(R.id.alertyes_btn);
        TextView title = dialog.findViewById(R.id.title);
        title.setText(s);


        alert_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(number.equals("1")){
                  SharedPreferences.Editor editor = userdata.edit();
                  editor.clear();
                  editor.apply();
                  Intent i = new Intent(context, LoginActivity.class);
                  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  context.startActivity(i);
              }else if(number.equals("0")){
                  quit();
                  dialog.dismiss();
              }else {
                  deleteRecord(number);
              }

            }
        });
        alert_clncel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    dialog.dismiss();
                }
                return true;
            }
        });
        dialog.show();

    }

    private void deleteRecord(String number) {
        Log.e("deleteCartId", number);

            ApiSucessResponse apiSucessResponse = (status) -> {
                //insertEventDB(name, payload);
                if (status.equals("1")) {

                    Intent i = new Intent(context, CartActivity.class);
                    context.startActivity(i);

                } else {
                    Toast.makeText(context, "Unable to Delete Please try Again.", Toast.LENGTH_SHORT).show();
                }
                Log.e("deleteCartId", status);
            };


            dataSync.deleteIDCart(authcode,number, apiSucessResponse);



    }

    private void quit() {
        Intent start = new Intent(Intent.ACTION_MAIN);
        start.addCategory(Intent.CATEGORY_HOME);
        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(start);

    }

    public void ShowAddPopup(String value1, String value2){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER_HORIZONTAL;
        dialog.setContentView(R.layout.addcart);
        dialog.setCanceledOnTouchOutside(true);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Button alert_clncel = dialog.findViewById(R.id.alert_clncel);
        Button alert_Yes = dialog.findViewById(R.id.alertyes_btn);
        TextView title = dialog.findViewById(R.id.title);


        alert_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                storeValues(value1,value2);
                dialog.dismiss();
            }
        });
        alert_clncel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    dialog.dismiss();
                }
                return true;
            }
        });
        dialog.show();

    }

    private void storeValues(String value1, String value2) {
        SharedPreferences.Editor editor = userdata.edit();
        editor.putString("bookid", value1);
        editor.putString("booktitle", value2);
        editor.apply();
    }
}
