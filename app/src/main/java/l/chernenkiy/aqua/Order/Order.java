package l.chernenkiy.aqua.Order;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import l.chernenkiy.aqua.Order.Tables.ClientTable;
import l.chernenkiy.aqua.Order.Tables.EquipmentTable;
import l.chernenkiy.aqua.Order.Tables.FishTable;
import l.chernenkiy.aqua.R;
import l.chernenkiy.aqua.ShoppingBasket.ShoppingBasket;

import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;

public class Order extends AppCompatActivity {

    public static HashMap<String, String> clientData = new HashMap<>();

    private ProgressDialog progressDialog;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar toolbar3 = findViewById (R.id.toolbar3);
        setSupportActionBar(toolbar3);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar3.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Order.this, ShoppingBasket.class);
                startActivity (intent);

            }
        });

        ConstraintLayout constraintLayout = findViewById (R.id.constrLayout);
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
         if (   clientData.get("name").isEmpty()
             || clientData.get("number").isEmpty()
             || clientData.get("city").isEmpty())
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

    public static String generateMailContent(ArrayList<HashMap> cartItems,
                                             ArrayList<HashMap> cartEquipmentItem,
                                             HashMap<String, String> clientData)
    {

        String result = ClientTable.generateHtml (clientData);

        result += FishTable.generateHtml (cartItems);

        result+= EquipmentTable.generateHtml (cartEquipmentItem);

        result+= getTotalSumOrder();

        return  result;
    }

    private static String getTotalSumOrder() {

        String sum = String.valueOf (CartHelper.finalSumOrder());

        return "<h3 style=\"margin-top:10px;text-align:right;\">Общая сумма заказа: " +sum+" грн.</h3>";
    }

    public class AsyncSendMail extends AsyncTask<Void, Void, Void> {


        private int statusCode;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String url = "http://aqua-m.com.ua/mail_order.php";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");

                String tableInfo = generateMailContent(cartItems, cartEquipmentItem,clientData);
                String password = "55555";
                String subject = "Приложение - " + clientData.get("name");

                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "utf-8"));
                writer.write("message=" + tableInfo + "&pass=" + password + "&subject=" + subject);
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

            cartItems.removeAll(cartItems);
            cartEquipmentItem.removeAll (cartEquipmentItem);

            getIntent().removeExtra("cartItems");
            getIntent().removeExtra("cartEquipmentItem");

            CartHelper.calculateItemsCart ();
            CartHelper.calculateItemsCartMain ();


            if(statusCode == 200)
                showToast("Спасибо за заказ.\nМенеджеры свяжутся с Вами\nв ближайшее время.");
            else
                showToast("Возникла ошибка\nпри оформлении заказа\nПопробуйте позже!");
        }
    }
}
