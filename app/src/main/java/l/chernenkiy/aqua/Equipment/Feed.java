package l.chernenkiy.aqua.Equipment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import l.chernenkiy.aqua.Helpers.ConnectionDetector;
import l.chernenkiy.aqua.R;

public class Feed extends AppCompatActivity {

    private RequestQueue mQueue;
    private static ListView lvFeed;
    private ProgressDialog progressDialog;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = findViewById(R.id.toolbarFeed);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Intent(Feed.this, EquipmentActivity.class);
                finish();
            }
        });



        mQueue = Volley.newRequestQueue(this);
        lvFeed = findViewById(R.id.lv_feed);


        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Загрузка. Пожалуйста подождите..");

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.ConnectingToInternet();
        if (isInternetPresent){
            new AsyncGetPrice().execute();
        } else{
            showToastInternetPresent("У Вас нет Интернет соединения");
            onBackPressed();
        }


        hideKeyboard();

    }

    @Override
    public void onBackPressed() {
        new Intent(Feed.this, EquipmentActivity.class);
        finish();
    }

    private void showToastInternetPresent(String msg) {
        Toast toast = Toast.makeText
                (getApplicationContext(),msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void hideKeyboard() {
        lvFeed.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                return false;
            }
        });
    }

    class AsyncGetPrice extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsonParse();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }

    public void jsonParse() {

        String url = "https://aqua-m.kh.ua/api/feed";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Iterator<String> keys = response.keys();
                            final ArrayList resultEquip = new ArrayList<>();

                            while (keys.hasNext()) {
                                String category = keys.next();
                                JSONArray allFeed = response.getJSONArray(category);
                                ArrayList resultChildrenCategory = new ArrayList<ItemFeed>();

                                for (int i = 0; i < allFeed.length(); i++) {
                                    JSONObject feedItem = allFeed.getJSONObject(i);

                                    String article = feedItem.getString("article");
                                    String name = feedItem.getString("name");
                                    String description = feedItem.getString("description");
                                    String weight = feedItem.getString("weight");
                                    String price = feedItem.getString("price");
                                    String image = feedItem.getString("image");

                                    resultChildrenCategory.add(new ItemEquipment(article, name, description, weight,  price, image));
                                }

                                resultEquip.add(new ItemCategory(category, resultChildrenCategory));
                            }

                            adapter = new CategoryAdapter(getApplicationContext(), resultEquip);
                            lvFeed.setAdapter(adapter);

                            openNewActivity(resultEquip);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToastInternetPresent("Ошибка загрузки данных, попробуйте позже");
                onBackPressed();
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void openNewActivity (final ArrayList<ItemCategory> resultEuip){
        lvFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),CategoryActivity.class);
                intent.putExtra("position", resultEuip.get(i));
                startActivity(intent);


            }
        });
    }

}
