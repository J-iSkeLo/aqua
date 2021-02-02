package l.chernenkiy.aqua.Equipment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import l.chernenkiy.aqua.Equipment.Adapters.CategoryAdapter;
import l.chernenkiy.aqua.Equipment.Adapters.EquipmentDialogAdapter;
import l.chernenkiy.aqua.Equipment.Items.ItemCategory;
import l.chernenkiy.aqua.Equipment.Items.ItemEquipment;
import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.NavigationBar;
import l.chernenkiy.aqua.Helpers.SearchActivity;
import l.chernenkiy.aqua.Helpers.Support;
import l.chernenkiy.aqua.MainActivity;
import l.chernenkiy.aqua.R;
import l.chernenkiy.aqua.ShoppingBasket.ShoppingBasket;

import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;
import static l.chernenkiy.aqua.MainActivity.lastBottomNavBar;
import static l.chernenkiy.aqua.MainActivity.lastClass;
import static l.chernenkiy.aqua.MainActivity.listEquip;
import static l.chernenkiy.aqua.MainActivity.nextSubcategory;


public class EquipmentAccessActivity extends AppCompatActivity {

    public static ListView lvEquipment;
    MenuItem cartIconMenuItem;
    SearchView searchView;
    ImageButton cartImageBtn;
    Button btnAllItemsCategory;
    EquipmentDialogAdapter equipmentDialogAdapter;
    Support support = new Support();


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
                Intent intent = new Intent (EquipmentAccessActivity.this, SearchActivity.class);
                intent.putExtra ("class", EquipmentAccessActivity.class);
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
                Intent intent = new Intent(EquipmentAccessActivity.this, ShoppingBasket.class);
                intent.putExtra("cartItems", cartItems);
                intent.putExtra("cartEquipmentItem", cartEquipmentItem);
                intent.putExtra ("class", EquipmentAccessActivity.class);
                startActivity(intent);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessories);

        Toolbar toolbar = findViewById(R.id.toolbarEquipAccess);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EquipmentAccessActivity.this, MainActivity.class);
                intent.putExtra("cartItems", cartItems);
                intent.putExtra("cartEquipmentItem", cartEquipmentItem);
                startActivity (intent);
            }
        });

        lvEquipment = findViewById(R.id.lv_equipment);
        btnAllItemsCategory = findViewById(R.id.btn_all_items_in_category_1);

        CategoryAdapter adapter = new CategoryAdapter(getApplicationContext (),listEquip);
        lvEquipment.setAdapter (adapter);
        openNewActivity (listEquip);

        hideKeyboard();

        BottomNavigationView navigation = findViewById(R.id.nav_bar_bottom);
        navigation.setSelectedItemId(R.id.equipment_accessories_Activity);
        lastBottomNavBar = 1;
        NavigationBar.itemSelected (navigation, getApplicationContext (),R.id.equipment_accessories_Activity);
        overridePendingTransition (0, 0);

        btnAllItemsCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(EquipmentAccessActivity.this, R.style.Theme_AppCompat_DialogWhenLarge);
                dialog.setContentView(R.layout.dialog_all_items_equip);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ListView lvAllItemsEquip = dialog.findViewById(R.id.lv_allItems_equip);
                ImageButton btnCloseDialog = dialog.findViewById(R.id.btn_close_dialog);

                final ArrayList<ItemEquipment> allItemsCategory = itemEquipmentArrayList(listEquip);
                equipmentDialogAdapter = new EquipmentDialogAdapter(getApplicationContext(), allItemsCategory);
                lvAllItemsEquip.setAdapter(equipmentDialogAdapter);

                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }

        });
    }

    private ArrayList<ItemEquipment> itemEquipmentArrayList (ArrayList<ItemCategory> arrayEquipment) {
        ArrayList<ItemEquipment> allItemsCategory = new ArrayList<>();

        for (int i = 0; i<arrayEquipment.size(); i++){

            ArrayList<ItemEquipment> itemsInCategory = arrayEquipment.get(i).getAllItems();

            allItemsCategory.addAll(itemsInCategory);
        }
        return allItemsCategory;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EquipmentAccessActivity.this, MainActivity.class);
        intent.putExtra("cartItems", cartItems);
        intent.putExtra("cartEquipmentItem", cartEquipmentItem);
        startActivity (intent);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void hideKeyboard() {
        lvEquipment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                return false;
            }
        });
    }

    private void openNewActivity (final ArrayList<ItemCategory> resultEquip){
        lvEquipment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                nextSubcategory = resultEquip.get(i);
                lastClass = EquipmentAccessActivity.class;
                startActivity(intent);
            }
        });
    }



}
