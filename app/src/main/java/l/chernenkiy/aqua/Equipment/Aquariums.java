package l.chernenkiy.aqua.Equipment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import l.chernenkiy.aqua.Equipment.Adapters.CategoryAdapter;
import l.chernenkiy.aqua.Equipment.Items.ItemCategory;
import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.NavigationBar;
import l.chernenkiy.aqua.Helpers.SearchActivity;
import l.chernenkiy.aqua.MainActivity;
import l.chernenkiy.aqua.R;
import l.chernenkiy.aqua.ShoppingBasket.ShoppingBasket;

import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;
import static l.chernenkiy.aqua.MainActivity.lastBottomNavBar;
import static l.chernenkiy.aqua.MainActivity.lastClass;
import static l.chernenkiy.aqua.MainActivity.listAquariums;
import static l.chernenkiy.aqua.MainActivity.nextSubcategory;

public class Aquariums extends AppCompatActivity {

    private static ListView lvAquariums;
    MenuItem cartIconMenuItem;
    SearchView searchView;
    ImageButton cartImageBtn;

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item, menu);
        cartIconMenuItem = menu.findItem(R.id.cart_count_menu_item);
        final View actionView = cartIconMenuItem.getActionView();
        final MenuItem menuSearchItem = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuSearchItem);
        searchView.setQueryHint("Поиск позиции...");
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(false);
        searchView.setOnSearchClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (Aquariums.this, SearchActivity.class);
                intent.putExtra ("class", Aquariums.class);
                startActivity (intent);
            }
        });

        if (actionView != null) {
            cartAddItemText = actionView.findViewById(R.id.text_item_cart);
            cartImageBtn = actionView.findViewById(R.id.btn_image_cart);
            CartHelper.calculateItemsCart();
        }

        cartImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View actionView) {
                Intent intent = new Intent(Aquariums.this, ShoppingBasket.class);
                intent.putExtra("cartItems", cartItems);
                intent.putExtra("cartEquipmentItem", cartEquipmentItem);
                intent.putExtra ("class", Aquariums.class);
                startActivity(intent);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquariums);

        Toolbar toolbar = findViewById(R.id.toolbarAquariums);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Aquariums.this, MainActivity.class);
                intent.putExtra("cartItems", cartItems);
                intent.putExtra("cartEquipmentItem", cartEquipmentItem);
                startActivity (intent);
                finish();
            }
        });

        lvAquariums = findViewById(R.id.lv_aquariums);
        CategoryAdapter adapter = new CategoryAdapter (getApplicationContext (),listAquariums);
        lvAquariums.setAdapter (adapter);

        openNewActivity (listAquariums);

        hideKeyboard();

        BottomNavigationView navigation = findViewById(R.id.nav_bar_bottom);
        navigation.setSelectedItemId(R.id.aquariums);
        lastBottomNavBar = 4;
        NavigationBar.itemSelected (navigation, getApplicationContext (), R.id.aquariums);
        overridePendingTransition (0, 0);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Aquariums.this, MainActivity.class);
        intent.putExtra("cartItems", cartItems);
        intent.putExtra("cartEquipmentItem", cartEquipmentItem);
        startActivity (intent);
        finish();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void hideKeyboard() {
        lvAquariums.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                return false;
            }
        });
    }

    private void openNewActivity (final ArrayList<ItemCategory> resultEquip){
        lvAquariums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),CategoryActivity.class);
                nextSubcategory = resultEquip.get(i);
                lastClass = Aquariums.class;
                intent.putExtra ("class", Aquariums.class);
                startActivity(intent);
            }
        });
    }
}


