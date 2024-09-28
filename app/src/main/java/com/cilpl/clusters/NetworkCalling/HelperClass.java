package com.cilpl.clusters.NetworkCalling;


import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HelperClass {

    Context context;



    public HelperClass(Context context) {
        this.context = context;
    }

    public static boolean validateEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void ImageViewAnimatedChange(Context context, final ImageView v, final int new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        final Animation anim_in = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);

        anim_in.setDuration(200);
        anim_out.setDuration(200);

        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageResource(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    // Show alert dialog






//    public void customToast(Context context, String message) {
//        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
//        View view = toast.getView();
//        view.setBackgroundResource(R.drawable.custom_toast_background);
//        TextView text = (TextView) view.findViewById(android.R.id.message);
//        text.setTextColor(Color.parseColor("#FFFFFF"));
//        /*Here you can do anything with above textview like text.setTextColor(Color.parseColor("#000000"));*/
//        toast.show();
//    }
//
//    public void displaySnackbar(View view, String stng) {
//        Snackbar snack = Snackbar.make(view, stng, Snackbar.LENGTH_SHORT);
//        View snackView = snack.getView();
//        snackView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
//        TextView snackViewText = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
//        snackViewText.setTextColor(context.getResources().getColor(R.color.colorWhite));
//        snackViewText.setTextSize(15f);
//        snackViewText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        snack.show();
//    }


    public boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return false;
        }
        return true;
    }

    public void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


//    public void showError(Context context, ANError anError) {
//        try {
//            JSONObject errorObject = new JSONObject();
//            try {
//                errorObject = new JSONObject(anError.getErrorBody());
//            } catch (Exception e) {
//                errorObject = new JSONObject(anError.getErrorDetail());
//            }
//            String errorMsg = errorObject.getString("message");
//            showErrorDialog(context, errorMsg);
//        } catch (Exception e) {
//            hideProgress();
//            showErrorDialog(context, context.getResources().getString(R.string.server_error));
//            e.printStackTrace();
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    public void showImageAlert(Context context, String imageUri) {
//        try {
//            if (imageUri != null) {
//                this.context = context;
//                Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_NoActionBar);
//                dialog.setCancelable(true);
//                dialog.getWindow().setBackgroundDrawable(
//                        new ColorDrawable(Color.TRANSPARENT));
//                final PhotoView imageView = new PhotoView(context);
//                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                imageView.setCropToPadding(true);
//                loadImage(false, context, String.valueOf(imageUri), imageView);
//                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//                dialog.setContentView(imageView);
//                dialog.show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            hideProgress();
//        }
//    }
//
//    // Get Date & Time as required format from CurrentTimeMillisec
//    public String setDateAndTime(Context context, String timemiles) {
//        if (timemiles == null) {
//            return null;
//        } else {
//            Date d2 = new Date();
//            Date d1 = new Date(Long.valueOf(timemiles));
//
//            long diffInDays = d2.getDate() - d1.getDate();
//
//            if (diffInDays >= 2 || diffInDays < 0) {
//                String date = d1.getDate() + "/" + (d1.getMonth() + 1) + "/" + (d1.getYear() + 1900);
//                try {
//                    SimpleDateFormat inFormat = new SimpleDateFormat("dd/MM/yyyy");
//                    SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM yyyy");
//                    String dateFormat = outFormat.format(inFormat.parse(date));
//                    return dateFormat;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return date;
//                }
//            } else if (diffInDays == 1)
//                return context.getResources().getString(R.string.yesterday);
//            else
//                return d1.getHours() + ":" + new DecimalFormat("00").format(d1.getMinutes());
//        }
//    }
//
//    public String convertToTimemillisec(String time) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date mDate = sdf.parse(time);
//            long timeInMilliseconds = mDate.getTime();
//            return String.valueOf(timeInMilliseconds);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return time;
//        }
//    }
//
//    public void stopShimmerAnimation(final ShimmerFrameLayout shimmer) {
//        try {
//            if (shimmer != null) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (shimmer != null) {
//                            shimmer.stopShimmerAnimation();
//                            shimmer.setVisibility(View.GONE);
//                        }
//                    }
//                }, 2000);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void checkLocationStatus() {

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }



    }




}
