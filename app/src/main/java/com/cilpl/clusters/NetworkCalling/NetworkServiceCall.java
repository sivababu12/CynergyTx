package com.cilpl.clusters.NetworkCalling;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cilpl.clusters.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class NetworkServiceCall {

    public Context context;
    private static final String TAG = NetworkServiceCall.class.getSimpleName();
    private OnServiceCallCompleteListener listener;
    private boolean isProgressDialogShow;
    private ProgressDialog pDialog;
    private RequestQueue requestQueue;
    private static final String DEVICE_OFFLINE_MESSAGE = "Internet not available, Cross check your internet connectivity and try again";

    public void setOnServiceCallCompleteListener(OnServiceCallCompleteListener listener) {
        this.listener = listener;
    }

    public NetworkServiceCall(Context context, boolean isProgressDialogShow) {
        this.context = context;
        this.isProgressDialogShow = isProgressDialogShow;
    }

    public static boolean isOnline(Context context) {
        return true;
    }


    public void makeJSONObjectPostStringRequest(String URL, final HashMap<String, String> postParam, final Request.Priority priority) {

            if (isProgressDialogShow) {
                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Uploading...");
                pDialog.setCancelable(false);
                pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                pDialog.setIndeterminate(true);
                pDialog.setCanceledOnTouchOutside(false);
                if (isProgressDialogShow) {
                    pDialog.show();
                    pDialog.setContentView(R.layout.my_network_progress);
                }

            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //AppLog.d(TAG, response.toString());
                            try {
                                if (isProgressDialogShow) {
                                    pDialog.dismiss();
                                }
                                listener.onJSONObjectResponse(getString(response));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (isProgressDialogShow) {
                                GetVolleyError(error);
                                pDialog.dismiss();
                                listener.onErrorResponse(error);
                            }
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    return postParam;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();

                    return hashMap;
                }
            };
               if(context!=null){
                   requestQueue = Volley.newRequestQueue(context);
                   //adding the string request to request queue
                   requestQueue.add(stringRequest);
               }



    }

    public void makeJSONObjectGetStringRequest(String URL, final HashMap<String, String> postParam, final Request.Priority priority) {
        //RequestQueue initialized
        if (AppUtil.getNetworkState() == NetworkConnectionState.CONNECTED && isOnline(context)) {
            if (isProgressDialogShow) {
                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Uploading...");
                pDialog.setCancelable(false);
                pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                pDialog.setIndeterminate(true);
                pDialog.setCanceledOnTouchOutside(false);
                if (isProgressDialogShow) {
                    pDialog.show();
                    pDialog.setContentView(R.layout.my_network_progress);
                }
            }
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //AppLog.d(TAG, response.toString());
                            try {
                                if (isProgressDialogShow) {
                                    pDialog.dismiss();
                                }
                                listener.onJSONObjectResponse(getString(response));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            listener.onErrorResponse(error);
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    return postParam;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    return hashMap;
                }
            };

            //creating a request queue
            requestQueue = Volley.newRequestQueue(context);
            //adding the string request to request queue
            requestQueue.add(stringRequest);
        } else {
            //AlertUtil.showErrorAlert(context, DEVICE_OFFLINE_MESSAGE);
            //   Toast.makeText(context, DEVICE_OFFLINE_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void GetVolleyError(VolleyError error) {

        NetworkResponse networkResponse = error.networkResponse;
        String errorMessage = "Unknown error";
        if (networkResponse == null) {
            if (error.getClass().equals(TimeoutError.class)) {
                errorMessage = "Request timeout";
            } else if (error.getClass().equals(NoConnectionError.class)) {
                errorMessage = "Failed to connect server";
            }
        } else {
            String result = new String(networkResponse.data);
            try {
                JSONObject response = new JSONObject(result);
                String status = response.getString("status");
                String message = response.getString("message");
                errorMessage = response.getString("message");

                Log.e("Error Status", status);
                Log.e("Error Message", message);
                if (networkResponse.statusCode == 404) {
                    errorMessage = "Resource not found";
                } else if (networkResponse.statusCode == 401) {
                    errorMessage = message + " Please login again";
                } else if (networkResponse.statusCode == 400) {
                    errorMessage = message + " Check your inputs";
                } else if (networkResponse.statusCode == 500) {
                    errorMessage = message + " Something is getting wrong";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //Toast.makeText(context, errorMessage.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error", errorMessage);
    }


    public JSONObject getString(String str) {
        try {
            return new JSONObject(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJSONObject(JSONObject stringOject) {
        try {
            return new JSONObject(String.valueOf(stringOject));
        } catch (JSONException ex) {
            return null;
        }
    }

    public JSONArray getJsonArray(String strObj) {
        try {
            return new JSONArray(strObj);
        } catch (JSONException e) {
            return null;
        }
    }



}
