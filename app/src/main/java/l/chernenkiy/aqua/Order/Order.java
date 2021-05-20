package l.chernenkiy.aqua.Order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.ConnectionDetector;
import l.chernenkiy.aqua.Helpers.Support;
import l.chernenkiy.aqua.LastOrder.LastOrder;
import l.chernenkiy.aqua.MainActivity;
import l.chernenkiy.aqua.MySettings;
import l.chernenkiy.aqua.Order.Tables.ClientTable;
import l.chernenkiy.aqua.Order.Tables.EquipmentTable;
import l.chernenkiy.aqua.Order.Tables.FishTable;
import l.chernenkiy.aqua.R;

import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;
import static l.chernenkiy.aqua.MainActivity.lastEquipShopArray;
import static l.chernenkiy.aqua.MainActivity.lastFishShopArray;
import static l.chernenkiy.aqua.MainActivity.orderClass;

public class Order extends AppCompatActivity {

    public static HashMap<String, String> clientData = new HashMap<>();

    public ProgressDialog progressDialog;
    public ConnectionDetector cd;

    private Boolean isInternetPresent = false;

    Support support = new Support();
    MySettings mySettings = new MySettings();

    private EditText firstLastName;
    private EditText city;
    private EditText phoneNumber;
    private EditText email;
    private EditText commentOrder;

    public String sName;
    public String sCity;
    public String sNumber;
    public String sEmail;
    public String sComment;

    private Button btnSendMail;

    @Override
    protected void onPause() {
        super.onPause();
        mySettings.saveFishShopBask();
        mySettings.saveEquipShopBask();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mySettings.loadEquipShopBask();
        mySettings.loadFishShopBask();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.ConnectingToInternet();

        ConstraintLayout constraintLayout = findViewById (R.id.constrLayout);
        Toolbar toolbar3 = findViewById (R.id.toolbar3);

        if (toolbarCreate(toolbar3)) return;

        hideSoftInput(constraintLayout);
        progressDialogCreate();

        findViewById();

        loadClientData();

        sendOrderToEmail();
    }

    private void findViewById() {
        firstLastName = findViewById(R.id.first_last_name);
        city = findViewById(R.id.city);
        phoneNumber = findViewById(R.id.number_phone);
        email = findViewById(R.id.email);
        commentOrder = findViewById(R.id.comment_order);
        btnSendMail = findViewById(R.id.finish_btn);
    }

    private void sendOrderToEmail() {
        btnSendMail.setOnClickListener(view -> {

            if (orderClass == LastOrder.class) {
                cartItems = lastFishShopArray;
                cartEquipmentItem = lastEquipShopArray;
            }

            sName = firstLastName.getText().toString();
            sCity = city.getText().toString();
            sNumber = phoneNumber.getText().toString();
            sEmail = email.getText().toString();
            sComment = commentOrder.getText().toString();

            putToHashMap(sName, sCity, sNumber,sEmail, sComment);
            if(dataIsWrong()){
                support.showToast(getApplicationContext(), getError());
                return;
            }

            if (isInternetPresent){
                final Dialog dialog = new Dialog(Order.this, R.style.FullHeightDialog);
                dialog.setContentView(R.layout.dialog_order);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable (Color.TRANSPARENT));
                new AsyncSendMail(dialog).execute();
                final Intent home = new Intent(Order.this, MainActivity.class);
                Runnable dismissRunner = () -> {
                    if (home != null)
                        startActivity(home);
                };
                new Handler().postDelayed( dismissRunner, 5000 );
            } else{
                support.showToast(getApplicationContext(),"У Вас нет Интернет соединения");
            }
        });
    }



    private void progressDialogCreate() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Подождите. Отправка заказа..");
    }

    private void hideSoftInput(ConstraintLayout constraintLayout) {
        constraintLayout.setOnClickListener(view -> {
            InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);

        });
    }

    private boolean toolbarCreate(Toolbar toolbar3) {
        setSupportActionBar(toolbar3);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar == null){
            return true;
        }
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);
        toolbar3.setNavigationOnClickListener(v -> finish());
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void putToHashMap(String tvName, String tvCity, String tvNumber,String tvEmail, String tvComment) {
        clientData.put("name", tvName);
        clientData.put("number", tvNumber);
        clientData.put("email", tvEmail);
        clientData.put("city", tvCity);
        clientData.put("comment", tvComment);
    }

    public boolean dataIsWrong(){
        return Objects.requireNonNull(clientData.get("name")).isEmpty()
                || Objects.requireNonNull(clientData.get("number")).isEmpty()
                || Objects.requireNonNull(clientData.get("city")).isEmpty();
    }

    public String getError(){
        if (Objects.requireNonNull(clientData.get("name")).isEmpty()){
            return "Введите Имя Фамилию";
        }
        if (Objects.requireNonNull(clientData.get("number")).isEmpty()){
            return "Введите Номер телефона";
        }
        if (Objects.requireNonNull(clientData.get("city")).isEmpty()){
            return "Укажите Ваш Город";
        }
        return "";
    }

    public static String generateMailContent(ArrayList<HashMap<String, String> > cartItems,
                                             ArrayList<HashMap<String, String> > cartEquipmentItem,
                                             HashMap<String, String> clientData)
    {

        String result = ClientTable.generateHtml (clientData);

        result += FishTable.generateHtml (cartItems);

        result+= EquipmentTable.generateHtml (cartEquipmentItem);

        result+= getTotalSumOrder();

        return  result;
    }

    private static String getTotalSumOrder() {

        String sum = String.valueOf (CartHelper.finalSumOrder(cartItems , cartEquipmentItem));

        return "<h3 style=\"margin-top:10px;text-align:right;\">Общая сумма заказа: " +sum+" грн.</h3>";
    }

    @SuppressLint("StaticFieldLeak")
    public class AsyncSendMail extends AsyncTask<Void, Void, Void> {

        private int statusCode;
        private final Dialog dialog;

        public AsyncSendMail(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            saveDataFish();
            saveDataEquip();
            saveDataClient();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String url = "https://aqua-m.com.ua/mail_order.php";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");

                String tableInfo = generateMailContent(cartItems, cartEquipmentItem, clientData);
                String password = "55555";
                String subject = "Приложение - " + clientData.get("name");

                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, StandardCharsets.UTF_8));
                writer.write("message=" + tableInfo + "&pass=" + password + "&subject=" + subject);
                writer.close();
                wr.close();

                statusCode = con.getResponseCode();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            cartItems.removeAll(cartItems);
            cartEquipmentItem.removeAll (cartEquipmentItem);

            getIntent().removeExtra("cartItems");
            getIntent().removeExtra("cartEquipmentItem");

            CartHelper.calculateItemsCart ();
            CartHelper.calculateItemsCartMain ();

            ImageView imageDialog = dialog.findViewById(R.id.dialog_order_image);
            TextView textDialog = dialog.findViewById(R.id.dialog_order_text);

            if(statusCode == 200) {
                imageDialog.setImageResource(R.drawable.ok_order_image);
                textDialog.setText(R.string.ok_order);
            }else{
                imageDialog.setImageResource(R.drawable.error_order_image);
                textDialog.setText(R.string.error_order);
            }

            dialog.show();
            dialog.setCancelable(false);
            Runnable dismissRunner = dialog::dismiss;
            new Handler().postDelayed( dismissRunner, 5000 );

        }


    }

    private void saveDataClient() {
        SharedPreferences sharedPref = getSharedPreferences("shared preferences date client", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Gson gson = new Gson();

        String name = gson.toJson(sName);
        String city = gson.toJson(sCity);
        String number = gson.toJson(sNumber);
        String email = gson.toJson(sEmail);

        editor.putString("nameClient", name);
        editor.putString("cityClient", city);
        editor.putString("numberClient", number);
        editor.putString("emailClient", email);

        editor.apply();
    }

    private void saveDataFish(){
        SharedPreferences sharedPref = getSharedPreferences("shared preferences fish", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Gson gson = new Gson();

        String jsonFish = gson.toJson(cartItems);

        editor.putString("cartItems", jsonFish);
        editor.apply();
    }
    private void saveDataEquip(){
        SharedPreferences sharedPref = getSharedPreferences("shared preferences equip", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Gson gson = new Gson();

        String jsonEquip = gson.toJson(cartEquipmentItem);

        editor.putString("cartEquipmentItems", jsonEquip);
        editor.apply();
    }

    private void loadClientData() {
        SharedPreferences sharedPref = getSharedPreferences("shared preferences date client", MODE_PRIVATE);

        Gson gson = new Gson();

        String loadName = sharedPref.getString("nameClient", null);
        String loadCity = sharedPref.getString("cityClient", null);
        String loadNumber = sharedPref.getString("numberClient", null);
        String loadEmail = sharedPref.getString("emailClient", null);

        Type type = new TypeToken<String>() {}.getType();

        sName = gson.fromJson(loadName, type);
        sCity = gson.fromJson(loadCity, type);
        sNumber = gson.fromJson(loadNumber, type);
        sEmail = gson.fromJson(loadEmail, type);

        firstLastName.setText(sName);
        city.setText(sCity);
        phoneNumber.setText(sNumber);
        email.setText(sEmail);
    }
}
