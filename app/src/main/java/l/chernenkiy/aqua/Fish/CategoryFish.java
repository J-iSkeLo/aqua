package l.chernenkiy.aqua.Fish;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import l.chernenkiy.aqua.Fish.Items.FishCategory;
import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.NavigationBar;
import l.chernenkiy.aqua.Helpers.Support;
import l.chernenkiy.aqua.MainActivity;
import l.chernenkiy.aqua.MySettings;
import l.chernenkiy.aqua.R;

import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.lastBottomNavBar;
import static l.chernenkiy.aqua.MainActivity.lastClass;
import static l.chernenkiy.aqua.MainActivity.listFish;
import static l.chernenkiy.aqua.MainActivity.nextFishSubcategory;

public class CategoryFish extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static ListView lvFishCategory;
    MenuItem cartIconMenuItem;
    ImageButton cartImageBtn;
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
        mySettings.loadEquipShopBask();
        mySettings.loadFishShopBask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_fish, menu);
        cartIconMenuItem = menu.findItem(R.id.cart_count_menu_item);
        final View actionView = cartIconMenuItem.getActionView();

        calculateItemsInShopBask(actionView);

        support.openShopBask(cartImageBtn, getApplicationContext(), CategoryFish.class);

        return super.onCreateOptionsMenu(menu);
    }

    private void calculateItemsInShopBask(View actionView) {
        if (actionView != null) {
            cartAddItemText = actionView.findViewById(R.id.text_item_cart);
            cartImageBtn = actionView.findViewById(R.id.btn_image_cart);
            CartHelper.calculateItemsCart();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_fish);

        toolbarCreate();
        itemsAddListView();
        openNewActivity(listFish);
        hideKeyboard();
        navigationButton();
    }

    private void itemsAddListView() {
        lvFishCategory = findViewById(R.id.listCategoryFish);
        CategoryAdapterFish categoryAdapterFish = new CategoryAdapterFish(getApplicationContext(), listFish);
        lvFishCategory.setAdapter(categoryAdapterFish);
    }

    private void openNewActivity (final ArrayList<FishCategory> resultEquip){
        lvFishCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), Fish.class);
                nextFishSubcategory = resultEquip.get(i);
                lastClass = CategoryFish.class;
                startActivity(intent);
            }
        });
    }

    private void toolbarCreate() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar == null ){
            return;
        }
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void navigationButton() {
        BottomNavigationView navigation = findViewById(R.id.nav_bar_bottom);
        navigation.setSelectedItemId(R.id.fish);
        lastBottomNavBar = 0;
        NavigationBar.itemSelected (navigation, getApplicationContext (),R.id.fish);
        overridePendingTransition (0, 0);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void hideKeyboard() {
        lvFishCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CategoryFish.this, MainActivity.class);
        startActivity (intent);
    }
}