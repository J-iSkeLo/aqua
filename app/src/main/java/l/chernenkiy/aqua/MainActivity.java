package l.chernenkiy.aqua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import l.chernenkiy.aqua.Equipment.EquipmentActivity;
import l.chernenkiy.aqua.Equipment.Equipment_accessories_Activity;
import l.chernenkiy.aqua.Fish.Fish;
import l.chernenkiy.aqua.My_Order.MyListCart;


public class MainActivity extends AppCompatActivity {

    public static TextView cartAddItemText;
    public static ArrayList<HashMap> cartItems = new ArrayList<>();
    public static ArrayList<HashMap> cartEquipmentItem = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Button btnFish = findViewById(R.id.btnFish);
        btnFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(MainActivity.this, Fish.class);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Button btnEquipment = findViewById(R.id.btnEquip);
        btnEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(MainActivity.this, EquipmentActivity.class);
                    startActivity(intent);
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

        new AsyncDate().execute();



        BottomNavigationView navigation = findViewById(R.id.nav_bar_bottom);
        navigation.setSelectedItemId(R.id.mainActivity2);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.fish:
                        startActivity(new Intent(getApplicationContext(), Fish.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.equipment_accessories_Activity:
                        startActivity(new Intent(getApplicationContext(), Equipment_accessories_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.mainActivity2:
                        return true;
                }

                return false;
            }
        });




    }

    public class AsyncDate extends AsyncTask <Void, Void, Void> {
        private String date = "";
        private int statusCode;

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://aqua-m.kh.ua/api/info";
            URL obj;
            try {
                obj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                date = response.toString();
                statusCode = connection.getResponseCode();

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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            TextView updateDate = findViewById(R.id.updateDate);

            if(statusCode == 200) {
                updateDate.setText("Прайс обновлён:\n" + date);
            }
            else {
                updateDate.setTextColor(Color.rgb(220, 20, 60));
                updateDate.setText("Нет доступа к серверу!");
            }

        }

    }

    private void showToast(String message) {
        Toast toast = Toast.makeText
                (getApplicationContext(),message,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
