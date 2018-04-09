package ca.lucas.starwarsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import ca.lucas.starwarsapp.listItems.peopleItem;
import ca.lucas.starwarsapp.singleton.MySingleton;
import ca.lucas.starwarsapp.listItems.filmItem;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SharedPreferences sharedPreferences;
    Button btnGo;
    Spinner spnSearch;
    EditText etSearch;

    private ListView lvFeed;
    ArrayAdapter<CharSequence> searchAdapter;
    private ArrayList<filmItem > filmItems;
    private ArrayList<peopleItem> peopleItems;

    Button btnSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = findViewById(R.id.btnSearch);
        btnGo = findViewById(R.id.btnGO);
        etSearch = findViewById(R.id.etSearch);
        spnSearch = findViewById(R.id.spnSearch);
        lvFeed = findViewById(R.id.lvFeed);

        searchAdapter = ArrayAdapter.createFromResource(this,R.array.searchMain, android.R.layout.simple_spinner_item);
        searchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnSearch.setAdapter(searchAdapter);
        spnSearch.setOnItemSelectedListener(this);

        sharedPreferences = getSharedPreferences("searchURL",0);

        final String[] url = new String[1];
        url[0] = sharedPreferences.getString("searchURL", "https://swapi.co/api/films/?format=json");


        vollyJsonRequest(url[0]);

        //final String url = "https://swapi.co/api/people/1/?format=json";

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUrl = url[0];
                //newUrl.substring(newUrl.length() -12, newUrl.length());
                String searchText = etSearch.getText().toString();
                String var = "?search=" + searchText + "&format=json";
                newUrl.replace("?format=json",searchText);
                SharedPreferences.Editor searchUrl = sharedPreferences.edit();
                searchUrl.putString("searchURL", newUrl);
                searchUrl.commit();
                finish();
                startActivity(getIntent());
            }
        });

    }

    private void vollyJsonRequest(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTextView.setText("Response: " + response.toString());
                       if(response.has("results")) {
                            try {
                                filmItems = new ArrayList<filmItem>(25);
                                peopleItems = new ArrayList<peopleItem>(50);
                                JSONArray jsArr = response.getJSONArray("results");
                                if (jsArr.length() > 0) {
                                    for (int i = 0; i < jsArr.length(); i++) {
                                        if(jsArr.getJSONObject(i).has("title")) {
                                            String title = jsArr.getJSONObject(i).getString("title");
                                            String episode = jsArr.getJSONObject(i).getString("episode_id");
                                            String crawl = jsArr.getJSONObject(i).getString("opening_crawl");
                                            String director = jsArr.getJSONObject(i).getString("director");
                                            String producer = jsArr.getJSONObject(i).getString("producer");
                                            String releaseDate = jsArr.getJSONObject((i)).getString("release_date");
                                            String url = jsArr.getJSONObject(i).getString("url") + "?format=json";
                                            final filmItem item = new filmItem(title, episode, crawl, director, releaseDate, producer, url);
                                            filmItems.add(item);
                                            filmApadapter apadapter = new filmApadapter(MainActivity.this, android.R.layout.simple_list_item_1, filmItems);
                                            lvFeed.setAdapter(apadapter);

                                            lvFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                    Intent intent = new Intent(MainActivity.this, descriptionActivity.class);
                                                    intent.putExtra("url", filmItems.get(i).getUrl());
                                                    intent.putExtra("layout", "film");
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                        else if(jsArr.getJSONObject(i).has("name")) {
                                                String name = jsArr.getJSONObject(i).getString("name");
                                                String url = jsArr.getJSONObject(i).getString("url") + "?format=json";

                                                final peopleItem item = new peopleItem(name, url);
                                                peopleItems.add(item);
                                                peopleAdaptor adaptor = new peopleAdaptor(MainActivity.this, android.R.layout.simple_list_item_1, peopleItems);
                                                lvFeed.setAdapter(adaptor);

                                                lvFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Intent intent = new Intent(MainActivity.this, descriptionActivity.class);
                                                        intent.putExtra("url", peopleItems.get(i).getUrl());
                                                        intent.putExtra("layout", "other");
                                                        startActivity(intent);
                                                    }
                                                });
                                        }

                                    }
                                }
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


    protected void onStop() {

        super.onStop();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spnSearch.setSelection(position,false);
       //final TextView mTextView = findViewById(R.id.text);
        String url = "https://swapi.co/api/";
        switch (position){
            case 0:
                url += "films";
                break;
            case 1:
                url += "people";
                break;
            case 2:
                url += "planets";
                break;
            case 3:
                url += "species";
                break;
            case 4:
                url += "starships";
                break;
            case 5:
                url += "vehicles";
                break;
            default:
                url += "films/1";
        }
        url += "/?format=json";
        SharedPreferences.Editor searchUrl = sharedPreferences.edit();
        searchUrl.putString("searchURL",url);
        searchUrl.commit();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class filmApadapter extends ArrayAdapter<filmItem> {

        private ArrayList<filmItem> items;

        public filmApadapter(Context context, int textViewResourceId, ArrayList<filmItem> items){
            super(context,textViewResourceId,items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }
            filmItem o = items.get(position);
            if (o != null) {
                TextView tt = v.findViewById(R.id.toptext);
                TextView bt = v.findViewById(R.id.bottemtext);

                if (tt != null) {
                    tt.setTextColor(Color.YELLOW);
                    tt.setText(o.getTitle());

                    bt.setTextColor(Color.YELLOW);
                    bt.setText(o.getDirector());
                }
            }
            return v;
        }
    }

    private class peopleAdaptor extends ArrayAdapter<peopleItem>{
        private ArrayList<peopleItem> items;

        public peopleAdaptor(@NonNull Context context, int resource, @NonNull ArrayList<peopleItem> items) {
            super(context, resource, items);
            this.items = items;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.single_list_item, null);
            }
            peopleItem o = items.get(position);
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

    private void refreshPage() {
        finish();
        startActivity(getIntent());
    }

}
