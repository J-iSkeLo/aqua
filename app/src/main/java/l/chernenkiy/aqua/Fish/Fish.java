package l.chernenkiy.aqua.Fish;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ortiz.touchview.TouchImageView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import l.chernenkiy.aqua.Fish.Items.FishCategory;
import l.chernenkiy.aqua.Fish.Items.Product;
import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.ConnectionDetector;
import l.chernenkiy.aqua.Helpers.NavigationBar;
import l.chernenkiy.aqua.Helpers.Support;
import l.chernenkiy.aqua.MySettings;
import l.chernenkiy.aqua.R;
import l.chernenkiy.aqua.ShoppingBasket.ShoppingBasket;

import static l.chernenkiy.aqua.Helpers.CartHelper.finalSumOrder;
import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;
import static l.chernenkiy.aqua.MainActivity.lastBottomNavBar;
import static l.chernenkiy.aqua.MainActivity.nextFishSubcategory;


public class Fish extends AppCompatActivity {

    public FishListAdapter adapter;
    @SuppressLint("StaticFieldLeak")
    public static ListView lvProduct;

    MenuItem cartIconMenuItem;
    ImageButton btnBask;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Support support = new Support();
    MySettings mySettings = new MySettings();
    @SuppressLint("StaticFieldLeak")
    public static Button btnSumOrder;


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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item_fish, menu);
        cartIconMenuItem = menu.findItem(R.id.cart_count_menu_item);
        final View actionView = cartIconMenuItem.getActionView();

        if (actionView != null) {
            cartAddItemText = actionView.findViewById(R.id.text_item_cart);
            btnBask = actionView.findViewById(R.id.btn_image_cart);
            if (!cartItems.isEmpty() || !cartEquipmentItem.isEmpty()) {
                CartHelper.calculateItemsCart();
            }
        }

        btnBask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View actionView) {
                try{
                    Intent intent = new Intent(Fish.this, ShoppingBasket.class);
                    intent.putExtra ("class", Fish.class);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish);

        cd = new ConnectionDetector (getApplicationContext());
        isInternetPresent = cd.ConnectingToInternet();
        cartAddItemText = findViewById(R.id.text_item_cart);
        lvProduct = findViewById(R.id.listFish);
        btnSumOrder = findViewById(R.id.btn_shop_bask_sum_fish_category);

        toolbar(nextFishSubcategory);

            if (nextFishSubcategory != null) {

                viewBtnSumOrder();
                support.sortProductAlphabetical(nextFishSubcategory.getItems());
                adapter = new FishListAdapter(getApplicationContext(), nextFishSubcategory.getItems());
                lvProduct.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                showDialogOnItemClick(nextFishSubcategory.getItems());
            }

        hideKeyboard();

        BottomNavigationView navigation = findViewById(R.id.nav_bar_bottom);
        NavigationBar.itemSelected (navigation, getApplicationContext (), 0);
        navigation.getMenu().getItem(lastBottomNavBar).setChecked(true);
        overridePendingTransition (0, 0);
    }

    @SuppressLint("SetTextI18n")
    private void viewBtnSumOrder() {
        BigDecimal sumOrder = finalSumOrder(cartItems , cartEquipmentItem);

        if(sumOrder.doubleValue() == 0.00){
            btnSumOrder.setVisibility(View.INVISIBLE);
        } else{
            btnSumOrder.setVisibility(View.VISIBLE);
            lvProduct.setPadding(0,0,0,180);
        }

        btnSumOrder.setText ("Сумма покупки: " + finalSumOrder(cartItems , cartEquipmentItem)+ " грн.");
        btnSumOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Fish.this, ShoppingBasket.class);
                intent.putExtra ("class", Fish.class);
                intent.putExtra ("position", getIntent().getSerializableExtra("position"));
                startActivity(intent);
            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void hideKeyboard() {
        lvProduct.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                return false;
            }
        });
    }

    private void showDialogOnItemClick(final ArrayList<Product> result) {
        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Product product = result.get(i);

                final Dialog dialog = new Dialog(Fish.this, R.style.FullHeightDialog);
                dialog.setContentView(R.layout.dialog_fish_set);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable (Color.TRANSPARENT));

                TextView nameDialog = dialog.findViewById(R.id.name_dialog);
                TextView sizeDialog = dialog.findViewById(R.id.size_dialog);
                TextView priceDialog = dialog.findViewById(R.id.price_dialog);
                final EditText quantity = dialog.findViewById(R.id.quantity_dialog);

                nameDialog.setText(product.getName());
                sizeDialog.setText(product.getSize() + " см.");
                priceDialog.setText(product.getPrice() + " грн.");

                TouchImageView touchImageView = dialog.findViewById(R.id.imageTouch);
                if(isInternetPresent) {
                    support.loadImage(touchImageView, product.getImage(), getApplicationContext());
                }
                else{
                    support.showToast(getApplicationContext(),"Нет интернет соединения для загрузки изображения!");
                }

                ImageButton btnCloseDialog = dialog.findViewById (R.id.btn_close_fish_dialog);
                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                Button btnCancel = dialog.findViewById(R.id.cancel_dialog_btn);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                Button btnAddShopBask = dialog.findViewById(R.id.addShopBask_btn);
                btnAddShopBask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String quantityFish = quantity.getText().toString();

                        if (quantity.length () < 1) {
                            support.showToast (getApplicationContext(),"Укажите количество");
                            return;
                        }

                        HashMap<String, String> singleItem = new HashMap<>();
                        singleItem.put("name", product.getName());
                        singleItem.put("quantity", quantityFish);
                        singleItem.put("size", product.getSize());
                        singleItem.put("price", product.getPrice());
                        singleItem.put ("image", product.getImage ());

                        boolean hasDuplicate = CartHelper.findCartItem(singleItem.get("name"),singleItem.get("price"), cartItems);

                        if (hasDuplicate) {
                            Toast toast = Toast.makeText
                                    (getApplicationContext(),"Позиция уже в Корзине",Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        } else {
                            cartItems.add(singleItem);
                            CartHelper.calculateItemsCart();
                            viewBtnSumOrder();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }

        });
    }

    private void toolbar(FishCategory fishCategory){

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar == null ){
            return;
        }
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);
        try {
            if(fishCategory.getName() == null){
                toolbar.setTitle ("Рыба");
            }
            toolbar.setTitle(fishCategory.getName());
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
        Intent intent = new Intent(Fish.this, CategoryFish.class);
        startActivity (intent);
    }
}
