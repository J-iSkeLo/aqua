package l.chernenkiy.aqua.LastOrder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.SectionPageAdapter;
import l.chernenkiy.aqua.MainActivity;
import l.chernenkiy.aqua.Order.Order;
import l.chernenkiy.aqua.R;

import static l.chernenkiy.aqua.MainActivity.lastEquipShopArray;
import static l.chernenkiy.aqua.MainActivity.lastFishShopArray;
import static l.chernenkiy.aqua.MainActivity.listFish;
import static l.chernenkiy.aqua.MainActivity.orderClass;
import static l.chernenkiy.aqua.MainActivity.sizeListFish;


public class LastOrder extends AppCompatActivity {

    public static SectionPageAdapter mSectionPageAdapter;
    public static ViewPager vp;
    public Button btnSendLastOrder;

    @SuppressLint({"RestrictedApi", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_order);

        equalityListFish();
        toolbarCreate();

        TabLayout tabLayout = findViewById(R.id.tabLayoutLastOrder);
        btnSendLastOrder = findViewById(R.id.btn_send_last_order);
        vp = findViewById(R.id.containerLastOrder);

        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        setupViewPager(vp);
        tabLayout.setupWithViewPager(vp);

        loadDataFish();
        loadDataEquip();

        vpSetCurrentItem();

        setVisibilityBtnSendLastOrder();
        String sumLastOrder = String.valueOf(CartHelper.finalSumOrder(lastFishShopArray, lastEquipShopArray));
        btnSendLastOrder.setText ("Сумма покупки " + sumLastOrder + " грн.");
        btnSendLastOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getApplicationContext(), Order.class);
                orderClass = LastOrder.class;
                startActivity(intent);
            }
        });
    }

    private void setVisibilityBtnSendLastOrder() {
        if (!(lastFishShopArray.isEmpty()) || !(lastEquipShopArray.isEmpty())){
            btnSendLastOrder.setVisibility(View.VISIBLE);
        }
    }

    private void vpSetCurrentItem() {
        if (lastFishShopArray.isEmpty () && !lastEquipShopArray.isEmpty ()){
            vp.setCurrentItem (1);
        }
        else vp.setCurrentItem (0);
    }

    private void equalityListFish() {
        if(sizeListFish == 0){
            sizeListFish = listFish.size();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LastOrder.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    public void setupViewPager(ViewPager vp){
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new LastOrderFish(), "Рыба");
        adapter.addFragment(new LastOrderEquipment(), "Оборудование");

        vp.setAdapter(adapter);
    }

    public void toolbarCreate() {
        Toolbar toolbar = findViewById(R.id.toolbar6);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadDataFish(){
        SharedPreferences sharedPref = getSharedPreferences("shared preferences fish", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("cartItems", null);
        Type type = new TypeToken<ArrayList<HashMap>>() {}.getType();
        lastFishShopArray = gson.fromJson(json, type);

        if(lastFishShopArray == null){
            lastFishShopArray = new ArrayList<>();
        }
    }

    private void loadDataEquip(){
        SharedPreferences sharedPref = getSharedPreferences("shared preferences equip", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("cartEquipmentItems", null);
        Type type = new TypeToken<ArrayList<HashMap>>() {}.getType();
        lastEquipShopArray = gson.fromJson(json, type);
        if(lastEquipShopArray == null){
            lastEquipShopArray = new ArrayList<>();
        }
    }
}