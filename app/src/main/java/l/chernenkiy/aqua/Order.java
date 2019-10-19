package l.chernenkiy.aqua;

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
import java.util.ArrayList;
import java.util.HashMap;
import javax.mail.MessagingException;

import static l.chernenkiy.aqua.CartListAdapter.cartItems;

public class Order extends AppCompatActivity {

    public static HashMap<String, String> clientData = new HashMap<>();

    private ProgressDialog progressDialog;
    Toolbar toolbar3;
    private ConstraintLayout constraintLayout;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;


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
                new Intent(Order.this, ShoppingBasket.class);
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
                    showToast();
                    return;
                }
                isInternetPresent = cd.ConnectingToInternet();
                if (isInternetPresent){
                    new AsyncSendMail().execute();
                    showToastSendMail();
                    Intent home = new Intent(Order.this, MainActivity.class);
                    startActivity(home);
                } else{
                    showToastInternetPresent();
                    }

            }
        });

    }

    private void showToastInternetPresent() {
        Toast toast = Toast.makeText
                (getApplicationContext(),"У Вас нет Интернет соединения",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();

    }

    private void putToHashMap(String tvName, String tvCity, String tvNumber, String tvComment) {
        clientData.put("name", tvName);
        clientData.put("number", tvNumber);
        clientData.put("city", tvCity);
        clientData.put("comment", tvComment);
    }

    private void showToast() {
        Toast toast = Toast.makeText
                (getApplicationContext(),getError(),Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();

    }

    private void showToastSendMail() {
        Toast toast = Toast.makeText
                (getApplicationContext(),"Спасибо за заказ.\nМенеджеры свяжутся с Вами\nв ближайшее время.",Toast.LENGTH_LONG);
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
                "</tr>";

        for (int i = 0, c = 1; i < cartItems.size(); i++, c++) {
            result += "" +
                "<tr>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+c+"</td>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+cartItems.get(i).get("name")+"</td>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+cartItems.get(i).get("quantity")+"</td>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+cartItems.get(i).get("price")+"</td>" +
                "</tr>";
    }

        return result + "</table>";
    }

    public class AsyncSendMail extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            try {
                saveData();
                Fish.cartItems.removeAll(Fish.cartItems);
                getIntent().removeExtra("cartItems");
                MailSender.sendMail(generateMailContent(cartItems, clientData));
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }




        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();


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
