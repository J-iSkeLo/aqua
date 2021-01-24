package l.chernenkiy.aqua.Equipment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ortiz.touchview.TouchImageView;
import java.util.HashMap;

import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;
import static l.chernenkiy.aqua.MainActivity.lastClass;
import static l.chernenkiy.aqua.MainActivity.nextSubcategory;

import l.chernenkiy.aqua.Equipment.Adapters.EquipmentListAdapter;
import l.chernenkiy.aqua.Equipment.Items.ItemCategory;
import l.chernenkiy.aqua.Equipment.Items.ItemEquipment;
import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.ConnectionDetector;
import l.chernenkiy.aqua.Helpers.NavigationBar;
import l.chernenkiy.aqua.Helpers.SearchActivity;
import l.chernenkiy.aqua.R;
import l.chernenkiy.aqua.ShoppingBasket.ShoppingBasket;

public class CategoryActivity extends AppCompatActivity {
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    ListView lvCategory;
    EquipmentListAdapter adapter;
    MenuItem cartIconMenuItem;
    SearchView searchView;
    ImageButton cartImageBtn;

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
                Intent intent = new Intent (CategoryActivity.this, SearchActivity.class);
                intent.putExtra ("class", CategoryActivity.class);
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
                Intent intent = new Intent(CategoryActivity.this, ShoppingBasket.class);
                intent.putExtra("cartItems", cartItems);
                intent.putExtra("cartEquipmentItem", cartEquipmentItem);
                intent.putExtra ("class", CategoryActivity.class);
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
        setContentView(R.layout.activity_category_equip);


        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.ConnectingToInternet();

        toolbar(nextSubcategory);

        if (nextSubcategory != null) {
            adapter = new EquipmentListAdapter(getApplicationContext(), nextSubcategory.getItems ());
        }else{
            showToastInternetPresent ("Возникла ошибка!\n Мы постараемся её решить\n В ближайшее время!");
            finish ();
        }
        lvCategory = findViewById(R.id.list_equip2);
        lvCategory.setAdapter(adapter);

        lvOnItemClickListener(nextSubcategory);

        BottomNavigationView navigation = findViewById(R.id.nav_bar_bottom);
        NavigationBar.itemSelected (navigation, getApplicationContext (), 0);
        overridePendingTransition (0, 0);

    }

    public void lvOnItemClickListener(final ItemCategory itemCategory){
        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Dialog dialog = new Dialog(CategoryActivity.this, R.style.FullHeightDialog);
                dialog.setContentView(R.layout.dialog_equip_set);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable (Color.TRANSPARENT));

                final ItemEquipment item = itemCategory.getItems().get(i);

                TextView nameEquip = dialog.findViewById(R.id.name_dialog_equip);
                TextView articleEquip = dialog.findViewById(R.id.article_dialog);
                TextView producerEquip = dialog.findViewById(R.id.dialog_producer_equip);
                TextView priceEquip = dialog.findViewById(R.id.priceEquip_dialog);
                TextView descriptionEquip = dialog.findViewById(R.id.dialog_description_equip);
                TouchImageView touchImageView = dialog.findViewById(R.id.imageTouchEquip);

                if(isInternetPresent) {
                    loadImage(touchImageView, item.getImage());
                }
                else{
                    showToastInternetPresent("Нет интернет соединения для загрузки изображения!");
                }

                final TextView quantity = dialog.findViewById(R.id.quantity_equip_dialog);

                nameEquip.setText(item.getName());
                articleEquip.setText("Артикул: " + item.getArticle());
                producerEquip.setText(item.getGeneralColKey ());
                priceEquip.setText(item.getPrice() + " грн.");
                descriptionEquip.setText(item.getDescription());


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
                            showToastInternetPresent ("Укажите количество");
                            return;
                        }

                        HashMap <String, String> singleEquipItem = new HashMap<>();
                        singleEquipItem.put("name", item.getName());
                        singleEquipItem.put("article", item.getArticle());
                        singleEquipItem.put("producer", item.getGeneralColKey ());
                        singleEquipItem.put("price", item.getPrice());
                        singleEquipItem.put("description", item.getDescription());
                        singleEquipItem.put("quantity", quantityEquip);
                        singleEquipItem.put ("image" , item.getImage ());

                        boolean hasDuplicate = CartHelper.findCartItem(singleEquipItem.get("name"),singleEquipItem.get("price"), cartEquipmentItem);

                        if (hasDuplicate)
                        {
                            Toast toast = Toast.makeText
                                    (getApplicationContext(),"Позиция уже в Корзине",Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
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

    public void loadImage (TouchImageView touchImageView, String imageURL){

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(60f);
        circularProgressDrawable.setColorSchemeColors(Color.rgb (155,155,155));
        circularProgressDrawable.start();



        Glide.with(this)
                .load(imageURL)
                .placeholder(circularProgressDrawable)
                .into(touchImageView);
    }

    private void toolbar(ItemCategory itemCategory){

        Toolbar toolbar = findViewById(R.id.toolbarEquipCategory2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        try {
            if(itemCategory.getName() == null){
                toolbar.setTitle ("Оборудование");
            }
            toolbar.setTitle(itemCategory.getName());
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

    private void showToastInternetPresent(String msg) {
        Toast toast = Toast.makeText
                (getApplicationContext(),msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        Class onBackClass = (Class) getIntent ().getSerializableExtra ("class");
        if (onBackClass == null){
            Intent intent = new Intent(CategoryActivity.this, lastClass);
            startActivity (intent);
        } else {
            Intent intent = new Intent(CategoryActivity.this, onBackClass);
            intent.putExtra("cartItems", cartItems);
            intent.putExtra("cartEquipmentItem", cartEquipmentItem);
            CartHelper.calculateItemsCart ();
            getIntent ().removeExtra ("class");
            startActivity (intent);
        }
    }
}
