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

public class Chemistry extends AppCompatActivity {

    private RequestQueue mQueue;
    private static ListView lvChemistry;
    private ProgressDialog progressDialog;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemistry);

        Toolbar toolbar = findViewById(R.id.toolbarChemistry);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Intent(Chemistry.this, EquipmentActivity.class);
                finish();
            }
        });

        mQueue = Volley.newRequestQueue(this);
        lvChemistry = findViewById(R.id.lv_chemistry);

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
        new Intent(Chemistry.this, EquipmentActivity.class);
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
        lvChemistry.setOnTouchListener(new View.OnTouchListener() {
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

        String url = "https://aqua-m.kh.ua/api/chemistry";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Iterator<String> keys = response.keys();
                            final ArrayList resultEquip = new ArrayList<>();

                            while (keys.hasNext()) {
                                String category = keys.next();
                                JSONArray allChemistry = response.getJSONArray(category);
                                ArrayList resultChildrenCategory = new ArrayList<ItemChemistry>();

                                for (int i = 0; i < allChemistry.length(); i++) {
                                    JSONObject chemistryItem = allChemistry.getJSONObject(i);

                                    String article = chemistryItem.getString("article");
                                    String name = chemistryItem.getString("name");
                                    String description = chemistryItem.getString("description");
                                    String capacity = chemistryItem.getString("capacity");
                                    String price = chemistryItem.getString("price");
                                    String image = chemistryItem.getString("image");

                                    resultChildrenCategory.add(new ItemEquipment(article, name, description, capacity,  price, image));
                                }

                                resultEquip.add(new ItemCategory(category, resultChildrenCategory));
                            }

                            adapter = new CategoryAdapter(getApplicationContext(), resultEquip);
                            lvChemistry.setAdapter(adapter);

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
        lvChemistry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),CategoryActivity.class);
                intent.putExtra("position", resultEuip.get(i));
                startActivity(intent);


            }
        });
    }

}
