package l.chernenkiy.aqua.Fish;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ortiz.touchview.TouchImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.ConnectionDetector;
import l.chernenkiy.aqua.Helpers.NavigationBar;
import l.chernenkiy.aqua.MainActivity;
import l.chernenkiy.aqua.R;
import l.chernenkiy.aqua.ShoppingBasket.ShopBaskTest;

import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;
import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.listFish;


public class Fish extends AppCompatActivity {

    private ProductListAdapter adapter;
    public static ListView lvProduct;

    Toolbar toolbar;
    MenuItem cartIconMenuItem;
    ImageButton btnBask;
    SearchView searchView;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item, menu);
        cartIconMenuItem = menu.findItem(R.id.cart_count_menu_item);
        final View actionView = cartIconMenuItem.getActionView();
        final MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


        searchView.setQueryHint("Поиск позиции...");
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.myFilter(newText);
                return false;
            }

        });


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
                    Intent intent = new Intent(Fish.this, ShopBaskTest.class);
                    intent.putExtra("cartItems", cartItems);
                    intent.putExtra("cartEquipmentItem", cartEquipmentItem);
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
        setContentView(R.layout.fish);

        cd = new ConnectionDetector (getApplicationContext());
        isInternetPresent = cd.ConnectingToInternet();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fish.this, MainActivity.class);
                intent.putExtra("cartItems", cartItems);
                intent.putExtra("cartEquipmentItem", cartEquipmentItem);
                startActivity (intent);
            }
        });

        cartAddItemText = findViewById(R.id.text_item_cart);

        lvProduct = findViewById(R.id.listFish);
        adapter = new ProductListAdapter(getApplicationContext(), listFish);
        lvProduct.setAdapter(adapter);
        showDialogOnItemClick(listFish);

        hideKeyboard();

        BottomNavigationView navigation = findViewById(R.id.nav_bar_bottom);
        navigation.setSelectedItemId(R.id.fish);

        NavigationBar.itemSelected (navigation, getApplicationContext (), R.id.fish);
        overridePendingTransition (0, 0);
    }



    private void showToastInternetPresent(String msg) {
        Toast toast = Toast.makeText
                (getApplicationContext(),msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
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


    private void showDialogOnItemClick(final ArrayList result) {
        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Product product = (Product) result.get(i);

                if (!product.getTitle().isEmpty())
                    return;

                final Dialog dialog = new Dialog(Fish.this, R.style.FullHeightDialog);
                dialog.setContentView(R.layout.dialog_item_set);

                TextView nameDialog = dialog.findViewById(R.id.name_dialog);
                TextView sizeDialog = dialog.findViewById(R.id.size_dialog);
                TextView priceDialog = dialog.findViewById(R.id.price_dialog);
                final TextView quantity = dialog.findViewById(R.id.quantity_dialog);

                nameDialog.setText(product.getName());
                sizeDialog.setText(product.getSize() + " см.");
                priceDialog.setText(product.getPrice() + " грн.");

                TouchImageView touchImageView = dialog.findViewById(R.id.imageTouch);
                if(isInternetPresent) {
                    loadImage(touchImageView, product.getImage());
                }
                else{
                    showToastInternetPresent("Нет интернет соединения для загрузки изображения!");
                }


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
                        if (quantityFish.isEmpty() || Integer.parseInt(quantityFish) < 1) {
                            return;
                        }

                        Map<String, String> singleItem = new HashMap();
                        singleItem.put("number", String.valueOf(product.getNumber()));
                        singleItem.put("name", product.getName());
                        singleItem.put("quantity", quantityFish);
                        singleItem.put("size", product.getSize());
                        singleItem.put("price", product.getPrice());

                        boolean hasDuplicate = CartHelper.findCartItem(singleItem.get("name"),singleItem.get("price"), cartItems);

                        if (hasDuplicate) {
                            Toast toast = Toast.makeText
                                    (getApplicationContext(),"Позиция уже в Корзине",Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        } else {
                            cartItems.add((HashMap) singleItem);
                            CartHelper.calculateItemsCart();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }

        });
    }

    public void loadImage (TouchImageView touchImageView, String imageURL){

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(60f);
        circularProgressDrawable.setColorSchemeColors(Color.WHITE);
        circularProgressDrawable.start();



        Glide.with(this)
                .load(imageURL)
                .placeholder(circularProgressDrawable)
                .into(touchImageView);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Fish.this, MainActivity.class);
        intent.putExtra("cartItems", cartItems);
        intent.putExtra("cartEquipmentItem", cartEquipmentItem);
        startActivity (intent);
    }
}
