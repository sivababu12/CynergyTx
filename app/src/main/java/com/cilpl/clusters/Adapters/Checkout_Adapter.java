package com.cilpl.clusters.Adapters;

import static com.cilpl.clusters.Activites.CheckOutActivity.cartlists;
import static com.cilpl.clusters.Activites.CheckOutActivity.isSelectedAll;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cilpl.clusters.Activites.CartActivity;
import com.cilpl.clusters.Activites.Interfaces.ApiSucessResponse;
import com.cilpl.clusters.Model.CartModel;
import com.cilpl.clusters.NetworkCalling.HelperClass;
import com.cilpl.clusters.NetworkCalling.ShowDialog;
import com.cilpl.clusters.R;
import com.cilpl.clusters.Services.ApiDataSync;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Checkout_Adapter extends RecyclerView.Adapter<Checkout_Adapter.ViewHolder> {
    Context context;
    String filepath, uname = "", emailid = "", phonenum = "", sdate = "", compynyname = "", gstNo = "", edate = "", deleteid = "", seates = "", authtoken, cartid = "", booktitle = "";
    HelperClass helperClass;
    SharedPreferences userdata;
    Calendar c;
    private int mYear, mMonth, mDay;
    private ArrayList<CartModel> cartModels;
    Vibrator vibrator;
    int counter = 1;
    ApiDataSync dataSync;

    public static ArrayList<String> cartlists2 = new ArrayList<>();

    String selcted_lang;
    private String[] sidemenulist = {"Wallet", "Meeting Room", "Invoices", "Polling", "Events", "My Devices", "Notification Settings", "Contact Us", "Logout"

    };
    private int[] tabicons = {R.drawable.wallet, R.drawable.meetingcard, R.drawable.invoice, R.drawable.poll, R.drawable.events, R.drawable.device, R.drawable.notifica, R.drawable.contactsus, R.drawable.logout_01};

    String authcode, cartType;
    Checkout_Adapter cart_adapter = this;
    private final int SINGLE_VIEW = 1;


    public Checkout_Adapter(Context context, ArrayList<CartModel> cartModels, String cartType) {
        this.context = context;
        this.cartModels = cartModels;
        this.cartType = cartType;


    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.cart_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        helperClass = new HelperClass(context);
        userdata = context.getSharedPreferences("Userdata", context.MODE_PRIVATE);
        authtoken = userdata.getString("authToken", "");

        CartModel model = cartModels.get(position);

        dataSync = new ApiDataSync(context);
        holder.lname.setText(model.getProduct_name());


//        if(model.getCapacity().equals("")||model.getCapacity().equals("null")){
//            holder.seates.setVisibility(View.GONE);
//        }else {
//            holder.seates.setVisibility(View.VISIBLE);
//            holder.seates.setText("No of Seats: " + model.getCapacity());
//        }
        holder.timeslots.setText(model.getSlots());
        holder.price_txt.setText("Rs." + model.getPrice() + "/-");
        Log.e("cartid", model.getPrice() + "," + model.getSlots());
        if (model.getBooking_type().equals("cluster") || model.getBooking_type().equals("MC")) {

            if (model.getBooking_type().equals("MC")) {
                holder.lname.setTextColor(ContextCompat.getColor(context, R.color.blue));
                holder.cartrow.setBackground(ContextCompat.getDrawable(context, R.drawable.elevaion_round));
            } else {
                holder.lname.setTextColor(ContextCompat.getColor(context, R.color.orange));
                holder.cartrow.setBackground(ContextCompat.getDrawable(context, R.drawable.red_bg));
            }

            holder.sateslay.setVisibility(View.GONE);
            holder.etdate.setVisibility(View.VISIBLE);
            holder.e_date.setText(model.getTo_date());
            holder.s_date.setText(model.getFrom_date());
            holder.timeslotslay.setVisibility(View.GONE);

        } else {

            if (model.getBooking_type().equals("CR")) {
                holder.sateslay.setVisibility(View.GONE);
                holder.seates.setVisibility(View.GONE);
                holder.timeslotslay.setVisibility(View.VISIBLE);
                holder.etdate.setVisibility(View.GONE);
                holder.seates.setText(model.getCapacity());
            } else if (model.getBooking_type().equals("WS") || model.getBooking_type().equals("ind")) {
                holder.sateslay.setVisibility(View.VISIBLE);
                holder.seates.setVisibility(View.VISIBLE);
                holder.seates.setText(model.getChairs());
                holder.etdate.setVisibility(View.VISIBLE);
                holder.timeslotslay.setVisibility(View.GONE);
            }
            holder.cartrow.setBackground(ContextCompat.getDrawable(context, R.drawable.elevaion_round));
            holder.s_date.setText(model.getFrom_date());
            holder.e_date.setText(model.getTo_date());
            holder.lname.setTextColor(ContextCompat.getColor(context, R.color.blue));

        }

        holder.lname.setText(model.getProduct_name());


        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartid = model.getCart_id();
                booktitle = model.getBooking_type();
                deleteid = model.getCart_id();
                showPopupMenu(view, cartid, booktitle, deleteid);

            }
        });
        Log.e("catrtt", cartType);
        if (cartType.equals("1")) {

            holder.imageButton.setVisibility(View.GONE);
            holder.checkbox.setVisibility(View.VISIBLE);
            final boolean isChecked = holder.checkbox.isChecked();
//            if (isSelectedAll) {
//                holder.checkbox.setChecked(true);
//                Log.e("addednumber", model.getFrom_date() + "...." + "0");
//                cartlists.add(model.getCart_id());
//                model.setS_flag("0");
//                Log.e("addednumber", cartlists.size() + "...." + "0");
//            } else {
//                cartlists.remove(model.getCart_id());
//                model.setS_flag("1");
//                Log.e("addednumber", model.getFrom_date() + "...." + "1");
//                Log.e("addednumber", cartlists.size() + "...." + "0");
//            }
            if (model.isCheck_status()) {
                holder.checkbox.setChecked(true);
                cartlists.add(model.getCart_id());
            } else {
                cartlists.remove(model.getCart_id());
                holder.checkbox.setChecked(false);
            }
            Log.e("chekboxstatus", String.valueOf(model.isCheck_status()) + "," + model.getProduct_name());
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final boolean isChecked = holder.checkbox.isChecked();
                    if (isChecked) {
                        model.setCheck_status(true);
                        cartlists.add(model.getCart_id());
                        model.setS_flag("0");
                        Log.e("addednumber", model.getCart_id() + "...." + "0");

                    } else {
                        model.setCheck_status(false);
                        cartlists.remove(model.getCart_id());
                        model.setS_flag("1");
                        Log.e("addednumber", model.getCart_id() + "...." + "1");

                    }
                   Log.e("CheckoutLust", "=tessss=" + String.valueOf(cartlists.size()));


                }

            });

        } else {
            holder.imageButton.setVisibility(View.VISIBLE);
            holder.checkbox.setVisibility(View.GONE);
        }


    }

    private void showPopupMenu(View view, String cartid, String booktitle, String deleteid) {
        Log.e("carttt", cartid);
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.cart_menu);
        popupMenu.setOnMenuItemClickListener(this::onMenuItemClick);
        popupMenu.show();

    }


    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.edit_menu:
//                OpenPopup(cartid,booktitle);
//                Log.e("carttt",cartid+"="+"0");
//
//                return true;
            case R.id.del_menu:
                Log.d("cartttt", "onMenuItemClick: action_popup_delete @ " + cart_adapter.cartid);
//                if (helperClass.checkInternetConnection(context)) {
//                    dialog.ShowExitPopup("Are You sure you want Delete ?", deleteid);
//                }else {
//                    Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
//                }
                deleteRecord(deleteid);

                return true;
            default:
                return false;
        }
    }

    private void deleteRecord(String cartid) {
        Log.e("deleteCartId", cartid);
        if (helperClass.checkInternetConnection(context)) {
            ApiSucessResponse apiSucessResponse = (status) -> {
                //insertEventDB(name, payload);
                if (status.equals("1")) {

                    Intent i = new Intent(context, CartActivity.class);
                    context.startActivity(i);

                } else {
                    Toast.makeText(context, "Unable to delete Please try Again.", Toast.LENGTH_SHORT).show();
                }
                Log.e("deleteCartId", status);
            };


            dataSync.deleteIDCart(authtoken, cartid, apiSucessResponse);


        } else {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
        }


    }


    private void OpenPopup(String cartid, String booktitle) {
        Log.e("cartId", cartid + "," + booktitle);
        uname = userdata.getString("firstname", "");
        emailid = userdata.getString("email", "");
        phonenum = userdata.getString("phne", "");
        compynyname = userdata.getString("compy_name", "");
        gstNo = userdata.getString("gst_no", "");
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.bookslot);
        dialog.setCanceledOnTouchOutside(false);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        ImageView addimg = dialog.findViewById(R.id.add1_img);
        ImageView minusimg = dialog.findViewById(R.id.minus_img);
        TextView addtxt = dialog.findViewById(R.id.add_txt);
        TextView fstname = dialog.findViewById(R.id.fstname);
        TextView email_id = dialog.findViewById(R.id.email_id);
        TextView moblie_id = dialog.findViewById(R.id.moblie_id);
        TextView startdate = dialog.findViewById(R.id.startdate1);
        TextView enddate = dialog.findViewById(R.id.enddate1);
        Button submit = dialog.findViewById(R.id.sumit_btn);
        TextView compname = dialog.findViewById(R.id.compname);
        TextView gstno = dialog.findViewById(R.id.gstno);
        Button submit2 = dialog.findViewById(R.id.sumit2_btn);
        Button submit3 = dialog.findViewById(R.id.sumit3_btn);
        ImageView gif_image = dialog.findViewById(R.id.gif_image);
        LinearLayout resultpage = dialog.findViewById(R.id.book_result);
        LinearLayout slotpage = dialog.findViewById(R.id.book_slot);
        LinearLayout verifydetails = dialog.findViewById(R.id.verifydetails);
        ImageView cance2 = dialog.findViewById(R.id.cancel_button);
        ImageView cancel = dialog.findViewById(R.id.cancel_button1);
        fstname.setText(uname);
        email_id.setText(emailid);
        moblie_id.setText(phonenum);
        if (compynyname.isEmpty()) {
            compname.setText("");
        } else {
            compname.setText(compynyname);
        }

        if (gstNo.isEmpty()) {
            gstno.setText("");
        } else {
            gstno.setText(gstNo);
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        cance2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Glide.with(context).load(R.drawable.thumsup1).into(gif_image);
        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                Calendar etdate = Calendar.getInstance();

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(dialog.getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        // sdate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        // startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        etdate.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SimpleDateFormat")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                etdate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                etdate.set(Calendar.MINUTE, minute);
                                // Log.v(TAG, "The choosen one " + date.getTime());
                                // Toast.makeText(getContext(),"The choosen one " + date.getTime(),Toast.LENGTH_SHORT).show();
                                sdate = (new SimpleDateFormat("dd-MM-yyyy h:mm a").format(etdate.getTime()));
                                startdate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(etdate.getTime()));
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();

                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(c.getTimeInMillis());

                dpd.show();

            }
        });
        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c1 = Calendar.getInstance();
                mYear = c1.get(Calendar.YEAR);
                mMonth = c1.get(Calendar.MONTH);
                mDay = c1.get(Calendar.DAY_OF_MONTH);
                Calendar etdate = Calendar.getInstance();

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(dialog.getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        //  edate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        // enddate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        etdate.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                etdate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                etdate.set(Calendar.MINUTE, minute);
                                // Log.v(TAG, "The choosen one " + date.getTime());
                                // Toast.makeText(getContext(),"The choosen one " + date.getTime(),Toast.LENGTH_SHORT).show();
                                edate = (new SimpleDateFormat("dd-MM-yyyy h:mm a").format(etdate.getTime()));
                                enddate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(etdate.getTime()));
                            }
                        }, c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE), false).show();

                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(c1.getTimeInMillis());
                // dpd.getDatePicker().setMinDate(milliseconds(changeDate(sdate)) + 1);
                dpd.show();
            }
        });

        addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(200);
                counter = counter + 1;
                addtxt.setText("" + counter);
            }
        });

        minusimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (counter < 0) {
                    counter = 0;
                    addtxt.setText(counter + "");
                }
                if (counter > 0) {
                    vibrator.vibrate(200);
                    counter = counter - 1;
                    addtxt.setText(counter + "");
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seates = addtxt.getText().toString();
                if (sdate.isEmpty() || edate.isEmpty()) {
                    Toast.makeText(context, "Select Start Date and End Date", Toast.LENGTH_SHORT).show();

                } else if (seates.equals("0") || seates.isEmpty() || seates.equals("")) {
                    Toast.makeText(context, "Please Select No of Seats", Toast.LENGTH_SHORT).show();

                } else {

                    Log.e("bookslots", seates + "," + sdate + "," + edate);
                    if (helperClass.checkInternetConnection(context)) {
                        ApiSucessResponse apiSucessResponse = (status) -> {
                            //insertEventDB(name, payload);
                            if (status.equals("1")) {
                                slotpage.setVisibility(View.GONE);
                                verifydetails.setVisibility(View.VISIBLE);
                                resultpage.setVisibility(View.GONE);

//                                storeValues(cartid,booktitle,sdate,edate,seates);
//                                dialog.dismiss();
                                Log.e("cartId", cartid + "1");


                            } else {
                                Toast.makeText(context, "Slots are not Available on these Date", Toast.LENGTH_SHORT).show();
                            }
                            Log.e("printtest", status);
                        };
                        Log.e("cartId", cartid + "2");

                        dataSync.Bookingslotdates(authtoken, cartid, sdate, edate, seates, booktitle, apiSucessResponse);
                        dialog.dismiss();
                        Intent i = new Intent(context, CartActivity.class);
                        context.startActivity(i);
                    } else {
                        Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
                    }


                }


            }
        });
        submit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname = fstname.getText().toString();
                emailid = email_id.getText().toString();
                phonenum = moblie_id.getText().toString();
                compynyname = compname.getText().toString();
                gstNo = gstno.getText().toString();
                if (uname.isEmpty() || uname.equals("")) {
                    fstname.setError("Please Enter Your Name");
                } else if (emailid.isEmpty() || emailid.equals("")) {
                    email_id.setText("Please Enter Your Email");
                } else if (phonenum.isEmpty() || phonenum.equals("")) {
                    moblie_id.setText("Please Enter Your Mobile Number");
                } else {
                    if (helperClass.checkInternetConnection(context)) {
                        ApiSucessResponse apiSucessResponse = (status) -> {
                            //insertEventDB(name, payload);
                            if (status.equals("1")) {
                                slotpage.setVisibility(View.GONE);
                                verifydetails.setVisibility(View.GONE);
                                resultpage.setVisibility(View.VISIBLE);

                            } else {
                                Toast.makeText(context, "Please give Proper Details", Toast.LENGTH_SHORT).show();
                            }
                            Log.e("printtest", status);
                        };
                        Log.e("sdatils", authcode + "," + cartid + "," + sdate + "," + edate + "," + seates + "," + booktitle + "," + uname + "," + emailid + "," + phonenum);
                        dataSync.BookingConfirmdetails(authtoken, cartid, sdate, edate, seates, booktitle, uname, emailid, phonenum, compynyname, gstNo, apiSucessResponse);
                    } else {
                        Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });


        dialog.show();


    }

    @Override
    public int getItemCount() {

        return (null != cartModels ? cartModels.size() : 0);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lname, s_date, seates, e_date, timeslots, price_txt;
        ImageButton imageButton;
        RelativeLayout cartrow;
        CheckBox checkbox;
        LinearLayout etdate, sateslay, timeslotslay, pricelotslay, check_list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lname = itemView.findViewById(R.id.title1);
            s_date = itemView.findViewById(R.id.sdate);
            e_date = itemView.findViewById(R.id.edate);
            seates = itemView.findViewById(R.id.sates);
            timeslots = itemView.findViewById(R.id.timeslots);
            imageButton = itemView.findViewById(R.id.imageButton);
            cartrow = itemView.findViewById(R.id.cartrow);
            checkbox = itemView.findViewById(R.id.checkbox);
            etdate = itemView.findViewById(R.id.etdate);
            timeslotslay = itemView.findViewById(R.id.timeslotslay);
            sateslay = itemView.findViewById(R.id.sateslay);
            pricelotslay = itemView.findViewById(R.id.pricelotslay);
            price_txt = itemView.findViewById(R.id.price_txt);
            check_list = itemView.findViewById(R.id.check_list);


        }
    }


}
