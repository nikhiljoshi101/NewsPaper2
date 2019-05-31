package com.example.newspaper;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity  {
 RecyclerView mRecyclerview;
  RecyclerView.LayoutManager mlayoutManager;
  RecyclerView.Adapter madapter;
  ArrayList<HashMap<String,String>> arrayListNews;
  static  String description1;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    private void initComponents () {
        mRecyclerview=findViewById(R.id.mRecyclerview);
        mlayoutManager=new LinearLayoutManager(MainActivity.this);
        mRecyclerview.setLayoutManager(mlayoutManager);
        callAPI();
    }

    private void callAPI () {


// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.myjson.com/bins/6q2qn";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        Toast.makeText(MainActivity.this, "Response is:"+ response.substring(0,500), Toast.LENGTH_SHORT).show();
                        Log.d("====","=========response:"+response);
                                parseAPIResponse(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "That didn't work!", Toast.LENGTH_SHORT).show();

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void parseAPIResponse (String response) {
        try {
            JSONObject objResponse = new JSONObject(response);
            JSONArray arrayHeadlines = objResponse.getJSONArray("headlines");
            arrayListNews = new ArrayList<>();

            for (int i = 0; i < arrayHeadlines.length(); i++) {
                JSONObject objItem = arrayHeadlines.getJSONObject(i);
                String title = objItem.getString("title");
                String imgUrl = objItem.getString("imgUrl");
                String description = objItem.getString("description");

                HashMap<String, String> map = new HashMap<>();
                map.put("title", title);
                map.put("url", imgUrl);
                map.put("detail", description);
                arrayListNews.add(map);
            }

            //set adapter
            madapter = new HomeListAdapter(MainActivity.this, arrayListNews);
            mRecyclerview.setAdapter(madapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
