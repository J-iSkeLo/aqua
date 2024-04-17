package l.chernenkiy.aqua;

import static l.chernenkiy.aqua.Helpers.CartHelper.calculateItemsCartMain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import l.chernenkiy.aqua.Delivery.Delivery;
import l.chernenkiy.aqua.Equipment.Items.ItemCategory;
import l.chernenkiy.aqua.Equipment.Items.ItemSubCategory;
import l.chernenkiy.aqua.Fish.CategoryFish;
import l.chernenkiy.aqua.Fish.Items.FishCategory;
import l.chernenkiy.aqua.Helpers.ApiInfo;
import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.ConnectionDetector;
import l.chernenkiy.aqua.Helpers.JsonRequest;
import l.chernenkiy.aqua.Helpers.Support;
import l.chernenkiy.aqua.LastOrder.LastOrder;
import l.chernenkiy.aqua.ShoppingBasket.ShoppingBasket;


public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static TextView cartAddItemText;

    @SuppressLint("StaticFieldLeak")
    public static TextView cartAddItemTextMain;

    public static ArrayList<HashMap<String, String> > cartItems = new ArrayList<>();
    public static ArrayList<HashMap<String, String> > cartEquipmentItem = new ArrayList<>();
    public static ArrayList<HashMap<String, String> > lastFishShopArray = new ArrayList<>();
    public static ArrayList<HashMap<String, String> > lastEquipShopArray = new ArrayList<>();

    public static ArrayList <FishCategory> listFish = new ArrayList<>();
    public static ArrayList <ItemCategory> listEquip = new ArrayList<>();
    public static ArrayList <ItemCategory> listFeed = new ArrayList<>();
    public static ArrayList <ItemCategory> listDrugs = new ArrayList<>();
    public static ArrayList <ItemCategory> listAquariums = new ArrayList<>();

    public static HashMap<String, String> dateHashMap = new HashMap<>();
    public static ItemCategory nextSubcategory;
    public static FishCategory nextFishSubcategory;
    public static ItemSubCategory nextItemsSubCategory;

    public static Class lastClass;
    public static Class lastClassCategory;
    public static Class orderClass;

    public static int lastBottomNavBar;

    public RequestQueue mQueue;
    public String date = "";
    public TextView updateDate;

    public static SharedPreferences sharedPreferences;
    public ReviewManager manager;

    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Support support = new Support();
    MySettings mySettings = new MySettings();

    @Override
    protected void onPause() {
        super.onPause();
        mySettings.saveFishShopBask();
        mySettings.saveEquipShopBask();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mySettings.loadEquipShopBask(getApplicationContext());
        mySettings.loadFishShopBask(getApplicationContext());

        CartHelper.calculateItemsCartMain();
    }




    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("shopping basket" , MODE_PRIVATE);

        mQueue = Volley.newRequestQueue(this);
        updateDate = findViewById(R.id.updateDate);
        ImageButton shopBaskButton = findViewById (R.id.btn_image_cart_main);
        cartAddItemTextMain = findViewById (R.id.text_item_cart_main);
        calculateItemsCartMain();

        RelativeLayout btnRating = findViewById(R.id.review_btn);
        btnRating.setOnClickListener(view -> {
            manager = ReviewManagerFactory.create(getApplicationContext());
            Task<ReviewInfo> request = manager.requestReviewFlow();
            request.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ReviewInfo reviewInfo = task.getResult();
                    StartActivity(MainActivity.this, reviewInfo);
                }
            });
        });

        shopBaskButton.setOnClickListener (view -> {
            try {
                Intent intent = new Intent(MainActivity.this, ShoppingBasket.class);
                lastClass = MainActivity.class;
                startActivity(intent);
            } catch (Exception ignored){}
        });

        Button btnLastOrder = findViewById(R.id.last_order);
        btnLastOrder.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(MainActivity.this, LastOrder.class);
                startActivity(intent);
            } catch (Exception ignored){}
        });

        Button btnCatalog = findViewById(R.id.btn_catalog);
        btnCatalog.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(MainActivity.this, CategoryFish.class);
                startActivity(intent);finish();
            } catch (Exception ignored){}
        });

        Button btnAboutUs = findViewById(R.id.btn_about_us);
        btnAboutUs.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(MainActivity.this, AboutUs.class);
                startActivity(intent);finish();
            } catch (Exception ignored){}
        });

        Button btnContacts = findViewById(R.id.btn_contacts);
        btnContacts.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(MainActivity.this, Contacts.class);
                startActivity(intent);
            } catch (Exception ignored){}
        });

        Button btnDelivery = findViewById(R.id.btn_delivery);
        btnDelivery.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(MainActivity.this, Delivery.class);
                startActivity(intent);
            } catch (Exception ignored){}
        });

        cd = new ConnectionDetector (getApplicationContext());
        isInternetPresent = cd.ConnectingToInternet();

        if (!isInternetPresent){
            support.showToast(getApplicationContext(), "Нет интернет соединения!");
            btnCatalog.setClickable(false);
            shopBaskButton.setClickable(false);
        }

        final boolean hasEmptyList = listFish.isEmpty()
                || listEquip.isEmpty()
                || listFeed.isEmpty()
                || listDrugs.isEmpty()
                || listAquariums.isEmpty();

        if (isInternetPresent && (hasEmptyList)) {
            btnCatalog.setClickable(true);
            final Dialog dialog = getDialog();
            clearAllList();
            new AsyncRequestHttp (dialog).execute();
        }

        String previouslyLoadedDate = dateHashMap.get ("date");
        if (!listFish.isEmpty ()) updateDate.setText ("Прайс обновлён:\n" + previouslyLoadedDate);
    }

    private void StartActivity(Activity activity, ReviewInfo reviewInfo) {
        Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);
        flow.addOnCompleteListener(task -> {

        });
    }

    @NotNull
    private Dialog getDialog() {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.dialog_load_main_activity);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    private void clearAllList() {
        listFish.clear();
        listAquariums.clear();
        listDrugs.clear();
        listEquip.clear();
        listFeed.clear();
    }

    @SuppressLint("StaticFieldLeak")
    public class AsyncRequestHttp extends AsyncTask <Void, Integer, Void> {

        private int statusCode;
        private final Dialog dialog;
        JsonRequest jsonRequest = new JsonRequest ();

        public AsyncRequestHttp(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgressBar progressBar = dialog.findViewById(R.id.spin_kit);
            Sprite wanderingCubes = new WanderingCubes ();
            progressBar.setIndeterminateDrawable(wanderingCubes);

            dialog.show ();
            dialog.setCancelable (false);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://aqua-m.kh.ua/api/info";
            try {
                updatePriceDate(url);
                requestAll();
            } catch (IOException ignored) {}

            return null;
        }

        private void requestAll() {
            jsonRequest.makeFishRequest (mQueue, listFish);


            jsonRequest.makeAllEquipRequest (mQueue, listEquip, ApiInfo.equipment, ApiInfo.equipmentGeneralColKey);
            jsonRequest.makeAllEquipRequest (mQueue, listFeed, ApiInfo.feed, ApiInfo.feedGeneralColKey);
            jsonRequest.makeAllEquipRequest (mQueue, listDrugs, ApiInfo.drugs, ApiInfo.drugsGeneralColKey);
            jsonRequest.makeAllEquipRequest (mQueue, listAquariums, ApiInfo.aquariums, ApiInfo.aquariumsGeneralColKey);
        }

        private void updatePriceDate(String url) throws IOException {
            URL obj = new URL (url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            statusCode = connection.getResponseCode();
            if (statusCode != 200) {
                return;
            }
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader (connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            int i = connection.getConnectTimeout ();
            publishProgress (i);
            date = response.toString();
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            dialog.dismiss ();
            dateHashMap.put ("date", date);

            if (statusCode != 200){
                updateDate.setTextColor(Color.rgb(220, 20, 60));
                updateDate.setText("Нет доступа к серверу!");
                return;
            }

            updateDate.setText("Прайс обновлён:\n" + date);
        }
    }


    public void onBackPressed(){
        moveTaskToBack(true);
    }
}
