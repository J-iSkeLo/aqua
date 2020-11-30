package l.chernenkiy.aqua.My_Order;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.ConnectionDetector;
import l.chernenkiy.aqua.MainActivity;
import l.chernenkiy.aqua.R;
import l.chernenkiy.aqua.ShoppingBasket.ShopBaskTest;

import static l.chernenkiy.aqua.ShoppingBasket.ShopBaskFishAdapter.cartItems;

public class Order extends AppCompatActivity {

    public static HashMap<String, String> clientData = new HashMap<>();

    private ProgressDialog progressDialog;
    private Toolbar toolbar3;
    private ConstraintLayout constraintLayout;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);

        toolbar3 = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar3);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar3.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Intent(Order.this, ShopBaskTest.class);
                finish();
            }
        });


        constraintLayout = findViewById(R.id.constrLayout);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);

            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Подождите. Отправка заказа..");

        final EditText firstLastName = findViewById(R.id.first_last_name);
        final EditText city = findViewById(R.id.city);
        final EditText phoneNumber = findViewById(R.id.number_phone);
        final EditText commentOrder = findViewById(R.id.comment_order);

        cd = new ConnectionDetector(getApplicationContext());

        Button btnSendMail = findViewById(R.id.finish_btn);
        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tvName = firstLastName.getText().toString();
                String tvCity = city.getText().toString();
                String tvNumber = phoneNumber.getText().toString();
                String tvComment = commentOrder.getText().toString();

                putToHashMap(tvName, tvCity, tvNumber, tvComment);
                if(dataIsWrong()){
                    showToast(getError());
                    return;
                }
                isInternetPresent = cd.ConnectingToInternet();
                if (isInternetPresent){
                    new AsyncSendMail().execute();
                    Intent home = new Intent(Order.this, MainActivity.class);
                    startActivity(home);
                } else{
                    showToast("У Вас нет Интернет соединения");
                }
            }
        });
    }

    private void putToHashMap(String tvName, String tvCity, String tvNumber, String tvComment) {
        clientData.put("name", tvName);
        clientData.put("number", tvNumber);
        clientData.put("city", tvCity);
        clientData.put("comment", tvComment);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText
                (getApplicationContext(),message,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();

    }

    public boolean dataIsWrong(){
        if (
            clientData.get("name").isEmpty() ||
            clientData.get("number").isEmpty() ||
            clientData.get("city").isEmpty())
        {
            return true;
        }

        return false;
    }

    public String getError(){
        if (clientData.get("name").isEmpty()){
            return "Введите Имя Фамилию";
        }
        if (clientData.get("number").isEmpty()){
            return "Введите Номер телефона";
        }
        if (clientData.get("city").isEmpty()){
            return "Укажите Ваш Город";
        }
        return "";
    }

    public static String generateMailContent(ArrayList<HashMap> cartItems, HashMap<String, String> clientData) {

        String result = "" +
                "<h3>Данные о клиенте</h3>" +
                "<table style=\"width:100%; border:1px solid #999; border-collapse: collapse;\">" +
                "<tr align=\"left\" style=\"background-color:#f4f4f4\">" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Имя Фамилия</th>" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Город</th>" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Номер Телефона</th>" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Комментарий</th>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+clientData.get("name")+"</td>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+clientData.get("city")+"</td>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+clientData.get("number")+"</td>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+clientData.get("comment")+"</td>" +
                "</tr>"+
                "</table><br>";

        result += "" +
                "<h3>Данные о заказе</h3>" +
                "<table style=\"width:100%; border:1px solid #999; border-collapse: collapse;\">" +
                "<tr align=\"left\" style=\"background-color:#f4f4f4\">" +
                "<th style=\"padding: 5px;border:1px solid #999;\">№</th>" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Название</th>" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Количество</th>" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Цена</th>" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Сумма</th>" +
                "</tr>";

        for (int i = 0, c = 1; i < cartItems.size(); i++, c++) {
            result += "" +
                "<tr>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+c+"</td>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+cartItems.get(i).get("name")+"</td>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+cartItems.get(i).get("quantity")+"</td>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+cartItems.get(i).get("price")+"</td>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+ CartHelper.itemSum(cartItems.get(i))+ " грн."+"</td>" +
                "</tr>";
        }
        return result + "</table>" +
                "<h3 style=\"margin-top:10px;text-align:right;\">Сумма заказа: "
                +CartHelper.countFinalSum(cartItems)+" грн.</h3>";
    }

    public class AsyncSendMail extends AsyncTask<Void, Void, Void> {


        private int statusCode;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            saveData();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String url = "http://aqua-m.com.ua/mail_order.php";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");

                String tableInfo = generateMailContent(cartItems, clientData);
                String password = "55555";
                String subject = "Приложение - " + clientData.get("name");

                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "utf-8"));
                writer.write("message=" + tableInfo + "&pass=" + password + "&subject=" + subject);
                System.out.println("message=" + tableInfo + "&pass=" + password + "&subject=" + subject);
                writer.close();
                wr.close();

                statusCode = con.getResponseCode();

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            MainActivity.cartItems.removeAll( MainActivity.cartItems);
            getIntent().removeExtra("cartItems");

            if(statusCode == 200)
                showToast("Спасибо за заказ.\nМенеджеры свяжутся с Вами\nв ближайшее время.");
            else
                showToast("Возникла ошибка\nпри оформлении заказа\nПопробуйте позже!");
        }
    }

    public void saveData(){
        SharedPreferences sharedPref = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cartItems);
        editor.putString("cart items", json);
        editor.apply();
    }




}
