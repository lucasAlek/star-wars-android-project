package ca.lucas.starwarsapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.lucas.starwarsapp.listItems.filmItem;
import ca.lucas.starwarsapp.singleton.MySingleton;

public class descriptionActivity extends AppCompatActivity {
 public TextView tvTitle;
 public TextView tvEpisode;
 public TextView tvCrowl;
 public TextView tvDirector;
 public TextView tvProducer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent i = getIntent();
        String url = i.getStringExtra("url");
        String layout = i.getStringExtra("layout");

        if(layout.equals("film")){
            setContentView(R.layout.film_layout);
        }
        else {
            setContentView(R.layout.film_layout);
        }


        tvTitle = findViewById(R.id.tvName);
        tvEpisode = findViewById(R.id.tvEpisode);
        tvCrowl = findViewById(R.id.tvCrawl);
        tvDirector = findViewById(R.id.tvDirector);
        tvProducer = findViewById(R.id.tvProducer);

        tvTitle.setTextColor(Color.YELLOW);
        tvCrowl.setTextColor(Color.YELLOW);
        tvEpisode.setTextColor(Color.YELLOW);
        tvDirector.setTextColor(Color.YELLOW);
        tvProducer.setTextColor(Color.YELLOW);

        vollyJsonRequest(url);
    }

    private void vollyJsonRequest(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTextView.setText("Response: " + response.toString());
                        if(response.has("name"))
                        {
                            try {
                                tvTitle.setText( response.getString("name").toString());
                                if(response.has("height")){
                                    tvEpisode.setText("Height : " + response.getString("height") + " cm");
                                    tvCrowl.setText("Mass: " + response.getString("mass") + " kg");
                                    tvDirector.setText("Hair Color: " + response.getString("hair_color"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        else if(response.has("title")){
                            try {
                                tvTitle.setText(response.getString("title").toString());
                                tvEpisode.setText("Episode : " + response.getString("episode_id" ));
                                tvCrowl.setText("Opening Crowl:\n" + response.getString("opening_crawl"));
                                tvDirector.setText("Director: " + response.getString("director"));
                                tvProducer.setText("Producer: " + response.getString("producer"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

// Access the RequestQueue through your singleton class.

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

}
