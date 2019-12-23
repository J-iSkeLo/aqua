package l.chernenkiy.aqua;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ortiz.touchview.TouchImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Fish extends AppCompatActivity {

    private RequestQueue mQueue;
    private ProductListAdapter adapter;
    public static ListView lvProduct;
    public static ArrayList<HashMap> cartItems = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    Toolbar toolbar;
    public static TextView cartAddItemText;
    MenuItem cartIconMenuItem;
    ImageButton cartImageBtn;
    SearchView searchView;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item, menu);
        cartIconMenuItem = menu.findItem(R.id.cart_count_menu_item);
        View actionView = cartIconMenuItem.getActionView();
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setQueryHint(getString(R.string.mr_chooser_searching));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.myFilter(newText);
                return false;
            }
        });

        if (actionView != null) {
            cartAddItemText = actionView.findViewById(R.id.text_item_cart);
            cartImageBtn = actionView.findViewById(R.id.btn_image_cart);
            if (!cartItems.isEmpty()) {
                cartAddItemText.setVisibility(View.VISIBLE);
                cartAddItemText.setText(String.valueOf(cartItems.size()));
            }
        }

        cartImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View actionView) {
                Intent intent = new Intent(Fish.this, ShoppingBasket.class);
                intent.putExtra("cartItems", cartItems);
                startActivity(intent);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fish);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Intent(Fish.this, MainActivity.class);
                finish();
            }
        });

        cartAddItemText = findViewById(R.id.text_item_cart);

        mQueue = Volley.newRequestQueue(this);
        lvProduct = findViewById(R.id.listFish);

        progressDialog = new ProgressDialog(this);
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

    private void showToastInternetPresent(String msg) {
        Toast toast = Toast.makeText
                (getApplicationContext(),msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void hideKeyboard() {
        lvProduct.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                return false;
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        TouchImageView touchImageView;
        ProgressBar progressBar;


        public DownloadImageTask(TouchImageView touchImageView, ProgressBar progressBar) {
            this.touchImageView = touchImageView;
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(progressBar.VISIBLE);
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            touchImageView.setImageBitmap(result);
        }
    }

    public void jsonParse() {

        String url = "https://aqua-m.kh.ua/api/price-list";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Iterator<String> keys = response.keys();
                            final ArrayList result = new ArrayList<>();

                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONArray allFish = response.getJSONArray(key);

                                result.add(new Product(0, "", "", "", key, ""));

                                for (int i = 0; i < allFish.length(); i++) {
                                    JSONObject fishItem = allFish.getJSONObject(i);

                                    int number = fishItem.getInt("number");
                                    String name = fishItem.getString("name");
                                    String size = fishItem.getString("size");
                                    String price = fishItem.getString("price");
                                    String image = fishItem.getString("image");

                                    result.add(new Product(number, name, size, price, "", image));
                                }
                            }
                            adapter = new ProductListAdapter(getApplicationContext(), result);
                            lvProduct.setAdapter(adapter);

                            showDialogOnItemClick(result);

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

    class AsyncGetPrice extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsonParse();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

        }
    }

    private void showDialogOnItemClick(final ArrayList result) {
        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Product product = (Product) result.get(i);

                if (!product.getTitle().isEmpty())
                    return;

                final Dialog dialog = new Dialog(Fish.this, R.style.FullHeightDialog);
                dialog.setContentView(R.layout.dialog_item_set);

                TextView nameDialog = dialog.findViewById(R.id.name_dialog);
                TextView sizeDialog = dialog.findViewById(R.id.size_dialog);
                TextView priceDialog = dialog.findViewById(R.id.price_dialog);
                final TextView quantity = dialog.findViewById(R.id.quantity_dialog);

                nameDialog.setText(product.getName());
                sizeDialog.setText(product.getSize() + " см.");
                priceDialog.setText(product.getPrice() + " грн.");

                TouchImageView touchImageView = dialog.findViewById(R.id.imageTouch);
                progressBar = dialog.findViewById(R.id.progressBar_image);
                new DownloadImageTask(touchImageView, progressBar).execute(product.getImage());


                Button btnCancel = dialog.findViewById(R.id.cancel_dialog_btn);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                Button btnAddShopBask = dialog.findViewById(R.id.addShopBask_btn);
                btnAddShopBask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String quantityFish = quantity.getText().toString();
                        if (quantityFish.isEmpty() || Integer.parseInt(quantityFish) < 1) {
                            return;
                        }

                        Map<String, String> singleItem = new HashMap();
                        singleItem.put("number", String.valueOf(product.getNumber()));
                        singleItem.put("name", product.getName());
                        singleItem.put("quantity", quantityFish);
                        singleItem.put("size", product.getSize());
                        singleItem.put("price", product.getPrice());

                        cartItems.add((HashMap) singleItem);
                        calculateItemsCart(cartItems.size());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

        });
    }

    private void calculateItemsCart(int size) {
        cartAddItemText.setVisibility(View.VISIBLE);
        cartAddItemText.setText(String.valueOf(size));
    }

    @Override
    public void onBackPressed() {
        new Intent(Fish.this, MainActivity.class);
        finish();
    }
}
