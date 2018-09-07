package com.arif.friendchat.requesthandler;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arif.friendchat.constant.AppData;
import com.arif.friendchat.constant.Constant;


import java.util.HashMap;
import java.util.Map;


public class RemoteDataController {

    private DataController dataController;
    private Context context;
    RequestQueue requestQueue;
    public RemoteDataController(DataController dataController, Context context){

        this.dataController= dataController;
        this.context = context;
    }

    public void getData(String url, final HashMap map, final int tag){
            //  fetchDataFromServerForExpert();
            Log.e("Interner conection","connected");

            if(requestQueue!=null)
                return;
                requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            dataController.DataReceivedFromDataController(response,tag);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            dataController.errorReceivedFromDataController(error.getMessage(),tag);
                        }
                    }){

                @Override
                protected HashMap<String,String> getParams(){

                    return map;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> headers = new HashMap<>();

                    headers.put("Authorization", AppData.getData(AppData.Acess_Toten,context));

                    return headers;
                }
            };
            requestQueue.add(stringRequest);

    }
    public void postData(String url, final HashMap map, final int tag){

            //  fetchDataFromServerForExpert();
            Log.e("Interner conection", "connected");
            if(requestQueue!=null)
                return;
                requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
Log.e("real response",""+response);
                            dataController.DataReceivedFromDataController(response, tag);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("error response",""+error);
                            error.printStackTrace();
                            dataController.errorReceivedFromDataController(error.getMessage(), tag);
                        }
                    }) {

                @Override
                protected HashMap<String, String> getParams() {

                    return map;
                }

                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> headers = new HashMap<>();

                        //headers.put("Content-Type", "application/json");
                  //  headers.put("Authorization", API.OAUTH_TOKEN_PREFIX+ AppData.getData(AppData.Acess_Toten,context));
                  //  headers.put("Authorization", AppData.getData(AppData.Acess_Toten,context));
                    headers.put("key", "123456654321");
                    Log.e("headers",""+headers+"  "+AppData.getData(AppData.Acess_Toten,context));

                    return headers;
                }
            };
                  requestQueue.add(stringRequest);

        }

    public void FCMPushMessage(String url, final HashMap map, final int tag){

        //  fetchDataFromServerForExpert();
        Log.e("Interner conection", "connected");
        if(requestQueue!=null)
            return;
        requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("real response",""+response);
                        dataController.DataReceivedFromDataController(response, tag);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error response",""+error);
                        error.printStackTrace();
                        dataController.errorReceivedFromDataController(error.getMessage(), tag);
                    }
                }) {

            @Override
            protected HashMap<String, String> getParams() {

                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();

                headers.put("Content-Type", "application/json");
                  headers.put("Authorization", Constant.FCM_TOKEN);
                //  headers.put("Authorization", AppData.getData(AppData.Acess_Toten,context));
                headers.put("key", "123456654321");
                Log.e("headers",""+headers+"  "+AppData.getData(AppData.Acess_Toten,context));

                return headers;
            }
        };
        requestQueue.add(stringRequest);

    }



}

