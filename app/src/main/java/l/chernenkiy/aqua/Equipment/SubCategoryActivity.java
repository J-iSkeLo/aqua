package l.chernenkiy.aqua.Equipment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ortiz.touchview.TouchImageView;

import java.util.ArrayList;
import java.util.HashMap;

import l.chernenkiy.aqua.Equipment.Adapters.EquipmentListAdapter;
import l.chernenkiy.aqua.Equipment.Items.ItemEquipment;
import l.chernenkiy.aqua.Equipment.Items.ItemSubCategory;
import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.ConnectionDetector;
import l.chernenkiy.aqua.Helpers.NavigationBar;
import l.chernenkiy.aqua.Helpers.SearchActivity;
import l.chernenkiy.aqua.Helpers.Support;
import l.chernenkiy.aqua.MySettings;
import l.chernenkiy.aqua.R;
import l.chernenkiy.aqua.ShoppingBasket.ShoppingBasket;

import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;
import static l.chernenkiy.aqua.MainActivity.lastBottomNavBar;
import static l.chernenkiy.aqua.MainActivity.lastClassCategory;
import static l.chernenkiy.aqua.MainActivity.nextItemsSubCategory;

public class SubCategoryActivity extends AppCompatActivity {

    ListView lvSubCategory;
    EquipmentListAdapter equipmentListAdapter;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Support support = new Support();
    MenuItem cartIconMenuItem;
    SearchView searchView;
    ImageButton cartImageBtn;
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
                Intent intent = new Intent (SubCategoryActivity.this, SearchActivity.class);
                intent.putExtra ("class", SubCategoryActivity.class);
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
                Intent intent = new Intent(SubCategoryActivity.this, ShoppingBasket.class);
                intent.putExtra("cartItems", cartItems);
                intent.putExtra("cartEquipmentItem", cartEquipmentItem);
                intent.putExtra ("class", SubCategoryActivity.class);
                intent.putExtra ("position", getIntent().getSerializableExtra("position"));
                startActivity(intent);
            }
        });

        searchView.setQueryHint("Поиск позиции...");
        searchView.setIconifiedByDefault(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategoty);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.ConnectingToInternet();
        lvSubCategory = findViewById(R.id.list_equip_subcategory);

        toolbar(nextItemsSubCategory);

        if (nextItemsSubCategory != null){
            ArrayList<ItemEquipment> nextItemsSubCategoryItems = nextItemsSubCategory.getItems();
            support.sortItemEquipmentAlphabetical(nextItemsSubCategory.getItems());
            equipmentListAdapter = new EquipmentListAdapter(getApplicationContext(), nextItemsSubCategoryItems);
            lvSubCategory.setAdapter(equipmentListAdapter);
            equipmentListAdapter.notifyDataSetChanged();

            lvOnItemClickListener(nextItemsSubCategoryItems);
        }
        BottomNavigationView navigation = findViewById(R.id.nav_bar_bottom);
        NavigationBar.itemSelected (navigation, getApplicationContext (), 0);
        navigation.getMenu().getItem(lastBottomNavBar).setChecked(true);
        overridePendingTransition (0, 0);
    }

    public void lvOnItemClickListener(final ArrayList<ItemEquipment> itemEquipmentCategory){
        lvSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Dialog dialog = new Dialog(SubCategoryActivity.this, R.style.FullHeightDialog);
                dialog.setContentView(R.layout.dialog_equip_set);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final ItemEquipment items = itemEquipmentCategory.get(i);

                TextView nameEquip = dialog.findViewById(R.id.name_dialog_equip);
                TextView articleEquip = dialog.findViewById(R.id.article_dialog);
                TextView producerEquip = dialog.findViewById(R.id.dialog_producer_equip);
                TextView priceEquip = dialog.findViewById(R.id.priceEquip_dialog);
                TextView descriptionEquip = dialog.findViewById(R.id.dialog_description_equip);
                TouchImageView touchImageView = dialog.findViewById(R.id.imageTouchEquip);

                if(isInternetPresent) {
                    support.loadImage(touchImageView, items.getImage(), getApplicationContext());
                }
                else{
                    support.showToast(getApplicationContext(),"Нет интернет соединения для загрузки изображения!");
                }

                final TextView quantity = dialog.findViewById(R.id.quantity_equip_dialog);

                nameEquip.setText(items.getName());
                articleEquip.setText("Артикул: " + items.getArticle());
                producerEquip.setText(items.getGeneralColKey ());
                priceEquip.setText(items.getPrice() + " грн.");
                descriptionEquip.setText(items.getDescription());


                Button btnCancelDialog = dialog.findViewById(R.id.cancel_dialogEquip_btn);
                Button btnAddShopBask = dialog.findViewById(R.id.addShopBaskEquip_btn);
                ImageButton btnCloseDialog = dialog.findViewById(R.id.btn_close_equip_dialog);

                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                btnCancelDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnAddShopBask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String quantityEquip = quantity.getText().toString();

                        if (quantity.length () < 1) {

                            support.showToast(getApplicationContext(),"Укажите количество");
                            return;
                        }

                        HashMap<String, String> singleEquipItem = new HashMap<>();
                        singleEquipItem.put("name", items.getName());
                        singleEquipItem.put("article", items.getArticle());
                        singleEquipItem.put("producer", items.getGeneralColKey ());
                        singleEquipItem.put("price", items.getPrice());
                        singleEquipItem.put("description", items.getDescription());
                        singleEquipItem.put("quantity", quantityEquip);
                        singleEquipItem.put ("image" , items.getImage ());

                        boolean hasDuplicate = CartHelper.findCartItem(singleEquipItem.get("name"),singleEquipItem.get("price"), cartEquipmentItem);

                        if (hasDuplicate)
                        {
                            support.showToast(getApplicationContext(), "Позиция уже в Корзине");
                        }
                        else
                        {
                            cartEquipmentItem.add(singleEquipItem);
                            CartHelper.calculateItemsCart();
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
            }
        });
    }

    private void toolbar(ItemSubCategory itemSubCategory){

        Toolbar toolbar = findViewById(R.id.toolbarEquipSubCategory);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        try {
            if(itemSubCategory.getName() == null){
                toolbar.setTitle ("Оборудование");
            }
            toolbar.setTitle(itemSubCategory.getName());
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Class onBackClass = (Class) getIntent ().getSerializableExtra ("class");
        if (onBackClass == null){
            Intent intent = new Intent(SubCategoryActivity.this, lastClassCategory);
            startActivity (intent);
        } else {
            Intent intent = new Intent(SubCategoryActivity.this, onBackClass);
            intent.putExtra("cartItems", cartItems);
            intent.putExtra("cartEquipmentItem", cartEquipmentItem);
            CartHelper.calculateItemsCart ();
            getIntent ().removeExtra ("class");
            startActivity (intent);
        }
    }
}