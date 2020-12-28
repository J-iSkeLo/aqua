package l.chernenkiy.aqua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import l.chernenkiy.aqua.Delivery.Delivery;
import l.chernenkiy.aqua.Equipment.Items.ItemCategory;
import l.chernenkiy.aqua.Fish.Product;
import l.chernenkiy.aqua.Helpers.ConnectionDetector;
import l.chernenkiy.aqua.Helpers.JsonRequest;
import l.chernenkiy.aqua.Helpers.NavigationBar;
import l.chernenkiy.aqua.ShoppingBasket.ShopBaskTest;

import static l.chernenkiy.aqua.Helpers.CartHelper.calculateItemsCartMain;


public class MainActivity extends AppCompatActivity {

    public static TextView cartAddItemText;
    public static TextView cartAddItemTextMain;
    public static ArrayList<HashMap> cartItems = new ArrayList<>();
    public static ArrayList<HashMap> cartEquipmentItem = new ArrayList<>();

    public static ArrayList <Product> listFish = new ArrayList ();
    public static ArrayList <ItemCategory> listEquip = new ArrayList ();
    public static ArrayList <ItemCategory> listFeed = new ArrayList ();
    public static ArrayList <ItemCategory> listChemistry = new ArrayList ();
    public static ArrayList <ItemCategory> listAquariums = new ArrayList ();

    public RequestQueue mQueue;
    public static ProgressBar progressBar;


    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueue = Volley.newRequestQueue(this);
        progressBar = findViewById (R.id.progressBarMain);

        ImageButton shopBaskButton = findViewById (R.id.btn_image_cart_main);
        cartAddItemTextMain = findViewById (R.id.text_item_cart_main);
        calculateItemsCartMain();

        shopBaskButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(MainActivity.this, ShopBaskTest.class);
                    intent.putExtra("cartItems", cartItems);
                    intent.putExtra("cartEquipmentItem", cartEquipmentItem);
                    intent.putExtra ("class", MainActivity.class);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        Button btnAboutUs = findViewById(R.id.btn_about_us);
        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(MainActivity.this, AboutUs.class);
                    startActivity(intent);finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Button btnContacts = findViewById(R.id.btn_contacts);
        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(MainActivity.this, Contacts.class);
                    startActivity(intent);
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Button btnLastShop = findViewById(R.id.btn_last_shop);
        btnLastShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(MainActivity.this, MyListCart.class);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Button btnDelivery = findViewById(R.id.btn_delivery);
        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(MainActivity.this, Delivery.class);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        cd = new ConnectionDetector (getApplicationContext());
        isInternetPresent = cd.ConnectingToInternet();

        if (isInternetPresent)
        {
            new AsyncRequestHttp ().execute();
        }else{
            showToastInternetPresent("У Вас нет Интернет соединения");
        }

        BottomNavigationView navigation = findViewById(R.id.nav_bar_bottom);
        NavigationBar.itemSelected (navigation, getApplicationContext (), 0);
        overridePendingTransition (0, 0);
    }

    public class AsyncRequestHttp extends AsyncTask <Void, Integer, Void> {
        private String date = "";
        private int statusCode;

        JsonRequest jsonRequest = new JsonRequest ();

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute ( );
            progressBar.setMax (100);


        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://aqua-m.kh.ua/api/info";
            try {
                updatePriceDate (url);

                requestAll ( );
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (ProtocolException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private void requestAll() {
            listEquip = jsonRequest.makeEquipRequest (mQueue);
            listFeed = jsonRequest.makeFeedRequest (mQueue);
            listChemistry = jsonRequest.makeChemistryRequest (mQueue);
            listAquariums = jsonRequest.makeAquariumsRequest (mQueue);
            listFish = jsonRequest.makeFishRequest (mQueue);
        }

        private void updatePriceDate(String url) throws IOException {
            URL obj;
            obj = new URL (url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader (connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            int i = connection.getConnectTimeout ();
            publishProgress (i);
            date = response.toString();
            statusCode = connection.getResponseCode();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            TextView updateDate = findViewById(R.id.updateDate);
            progressBar.setVisibility (View.INVISIBLE);

            if(statusCode == 200) {
                updateDate.setText("Прайс обновлён:\n" + date);
            }
            else {
                updateDate.setTextColor(Color.rgb(220, 20, 60));
                updateDate.setText("Нет доступа к серверу!");
            }

        }

    }

    private void showToastInternetPresent(String msg) {
        Toast toast = Toast.makeText
                (getApplicationContext(),msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public void onBackPressed(){
        moveTaskToBack(true);
    }
}
