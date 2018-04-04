package ca.lucas.starwarsapp;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent i = getIntent();
        String url = i.getStringExtra("url");
        String layout = i.getStringExtra("layout");

        if(layout.equals("film")){
            setContentView(R.layout.film_layout);
        }

        tvTitle = findViewById(R.id.tvName);
        tvEpisode = findViewById(R.id.tvEpisode);

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
                            //mTextView.setText("name: " + response.getString("name").toString());
                        }
                        else if(response.has("title")){
                            try {
                                tvTitle.setText(response.getString("title").toString());
                                tvEpisode.setText("Episode : " + response.getString("episode_id" ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
//                        else if(response.has("results")) {
//                            try {
//                                JSONArray jsArr = response.getJSONArray("results");
//                                if (jsArr.length() > 0) {
//                                    for (int i = 0; i < jsArr.length(); i++) {
//
//                                        String title = jsArr.getJSONObject(i).getString("title");
//                                        String episode = jsArr.getJSONObject(i).getString("episode_id");
//                                        String crawl = jsArr.getJSONObject(i).getString("opening_crawl");
//                                        String director = jsArr.getJSONObject(i).getString("director");
//                                        String producer = jsArr.getJSONObject(i).getString("producer");
//                                        String releaseDate = jsArr.getJSONObject((i)).getString("release_date");
//                                        final String url = jsArr.getJSONObject(i).getString("url") + "?format=json";
//                                        final filmItem item = new filmItem(title, episode, crawl, director, releaseDate, producer, url);
//
////                                mTextView.setText("title: " + response.getString("title").toString() +
////                                        "\n\n opening crawl:" + response.getString("opening_crawl").toString());
//                                    }
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
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
