package ca.lucas.starwarsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.SingleLineTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ca.lucas.starwarsapp.listItems.filmItem;
import ca.lucas.starwarsapp.listItems.singleItem;
import ca.lucas.starwarsapp.singleton.MySingleton;

public class descriptionActivity extends AppCompatActivity {

    private ListView lvFilmFeed;
    private TextView tvTitle;
    private TextView tvEpisode;
    private TextView tvCrowl;
    private TextView tvDirector;
    private TextView tvProducer;
    private TextView tvRelease;
 private ArrayList<singleItem> singleItems;

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

        lvFilmFeed = findViewById(R.id.lvFilms);

        tvTitle = findViewById(R.id.tvName);
        tvEpisode = findViewById(R.id.tvEpisode);
        tvCrowl = findViewById(R.id.tvCrawl);
        tvDirector = findViewById(R.id.tvDirector);
        tvProducer = findViewById(R.id.tvProducer);
        tvRelease = findViewById(R.id.tvRelease);

        tvTitle.setTextColor(Color.YELLOW);
        tvCrowl.setTextColor(Color.YELLOW);
        tvEpisode.setTextColor(Color.YELLOW);
        tvDirector.setTextColor(Color.YELLOW);
        tvProducer.setTextColor(Color.YELLOW);
        tvRelease.setTextColor(Color.YELLOW);

        vollyJsonRequest(url);
    }

    private void vollyJsonRequest(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTextView.setText("Response: " + response.toString());
                        if (response.has("name")) {
                            try {
                                tvTitle.setText(response.getString("name").toString());
                                if (response.has("height")) {
                                    tvEpisode.setText("Hair Color: " + response.getString("hair_color"));
                                    tvCrowl.setText("Eye Color: " + response.getString("eye_color"));
                                    tvDirector.setText("Birth Year: " + response.getString("birth_year"));
                                    tvProducer.setText("Gender: " + response.getString("gender"));
                                    tvRelease.setText("home " + response.getString("homeworld") + "/?format=json");
                                }
                                if (response.has("films")) {
                                    JSONArray jsArr = null;
                                    singleItems = new ArrayList<singleItem>(10);
                                    jsArr = response.getJSONArray("films");
                                    if (jsArr.length() > 0) {
                                        for (int i = 0; i < jsArr.length(); i++) {
                                            String name = jsArr.getString(i);
                                            final singleItem item = new singleItem("name");
                                            itemApadapter apadapter = new itemApadapter(descriptionActivity.this,android.R.layout.simple_list_item_1,singleItems);
                                            lvFilmFeed.setAdapter(apadapter);

                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else if (response.has("title")) {
                            try {
                                tvTitle.setText(response.getString("title").toString());
                                tvEpisode.setText("Episode : " + response.getString("episode_id"));
                                tvCrowl.setText("Opening Crowl:\n" + response.getString("opening_crawl"));
                                tvDirector.setText("Director: " + response.getString("director"));
                                tvProducer.setText("Producer: " + response.getString("producer"));
                                tvRelease.setText("Release Date: " + response.getString("release_date"));
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
        if(tvRelease.toString().contains("home"))
        {
            String text = tvRelease.toString();
            text.substring(5);
           tvRelease.setText("Home World: " + getName(tvRelease.toString()));
        }
    }

    private class itemApadapter extends ArrayAdapter<singleItem> {

        private ArrayList<singleItem> items;

        public itemApadapter(@NonNull Context context, int resource, @NonNull ArrayList<singleItem> singleItems) {
            super(context, resource, singleItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.single_list_item, null);
            }
            singleItem o = items.get(position);
            if (o != null) {
                TextView tt = v.findViewById(R.id.toptext);


                if (tt != null) {
                    tt.setTextColor(Color.YELLOW);
                    tt.setText(o.getName());
                }
            }
            return v;
        }
    }

    String theReturn = "nothing";
    private String getName(String url) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTextView.setText("Response: " + response.toString());
                        if(response.has("name"))
                        {
                            try
                            {
                                theReturn = response.getString("name").toString();
                            }
                            catch (JSONException e) {
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
        return theReturn;
    }

}
