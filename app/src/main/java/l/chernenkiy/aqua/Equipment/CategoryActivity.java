package l.chernenkiy.aqua.Equipment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
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
import com.ortiz.touchview.TouchImageView;
import java.util.HashMap;
import java.util.Map;

import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;

import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.ConnectionDetector;
import l.chernenkiy.aqua.R;
import l.chernenkiy.aqua.ShoppingBasket.ShopBaskTest;

public class CategoryActivity extends AppCompatActivity {
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    ListView lvCategory;
    EquipmentListAdapter adapter;
    MenuItem cartIconMenuItem;
    SearchView searchView;
    ImageButton btnBask;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item, menu);
        cartIconMenuItem = menu.findItem(R.id.cart_count_menu_item);
        final View actionView = cartIconMenuItem.getActionView();
        final MenuItem searchItem = menu.findItem(R.id.app_bar_search);

        if (actionView != null) {
            cartAddItemText = actionView.findViewById(R.id.text_item_cart);
            btnBask = actionView.findViewById(R.id.btn_image_cart);
            CartHelper.calculateItemsCart();
        }
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


        btnBask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View actionView) {

                try{Intent intent = new Intent(CategoryActivity.this, ShopBaskTest.class);
                    intent.putExtra("cartItems", cartItems);
                    intent.putExtra("cartEquipmentItem", cartEquipmentItem);
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
        setContentView(R.layout.equipment_category);


        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.ConnectingToInternet();

        ItemCategory itemCategory = (ItemCategory) getIntent().getSerializableExtra("position");

        toolbar(itemCategory);

        adapter = new EquipmentListAdapter(getApplicationContext(), itemCategory.getItems());
        lvCategory = findViewById(R.id.list_equip2);
        lvCategory.setAdapter(adapter);



        lvOnItemClickListener(itemCategory);


    }

    public void lvOnItemClickListener(final ItemCategory itemCategory){
        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Dialog dialog = new Dialog(CategoryActivity.this, R.style.FullHeightDialog);
                dialog.setContentView(R.layout.dialog_item_equip_set);

                final ItemEquipment item = itemCategory.getItems().get(i);

                TextView nameEquip = dialog.findViewById(R.id.name_dialog_equip);
                TextView articleEquip = dialog.findViewById(R.id.article_dialog);
                TextView producerEquip = dialog.findViewById(R.id.dialog_producer_equip);
                TextView priceEquip = dialog.findViewById(R.id.priceEquip_dialog);
                TextView descriptionEquip = dialog.findViewById(R.id.dialog_description_equip);
                TouchImageView touchImageView = dialog.findViewById(R.id.imageTouchEquip);

                final TextView quantity = dialog.findViewById(R.id.quantity_equip_dialog);

                nameEquip.setText(item.getName());
                articleEquip.setText("Артикул: " + item.getArticle());
                producerEquip.setText(item.getProducer());
                priceEquip.setText(item.getPrice() + " грн.");
                descriptionEquip.setText(item.getDescription());

                Button btnCancelDialog = dialog.findViewById(R.id.cancel_dialogEquip_btn);
                Button btnAddShopBask = dialog.findViewById(R.id.addShopBaskEquip_btn);


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
                        if (quantityEquip.isEmpty() || Integer.parseInt(quantityEquip) < 1) {
                            return;
                        }

                        Map <String, String> singleEquipItem = new HashMap<>();
                        singleEquipItem.put("name", item.getName());
                        singleEquipItem.put("article", item.getArticle());
                        singleEquipItem.put("producer", item.getProducer());
                        singleEquipItem.put("price", item.getPrice());
                        singleEquipItem.put("description", item.getDescription());
                        singleEquipItem.put("quantity", quantityEquip);

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
                                cartEquipmentItem.add((HashMap) singleEquipItem);
                                CartHelper.calculateItemsCart();
                                dialog.dismiss();
                            }
                    }
                });

                dialog.show();
            }
        });
    }

    private void toolbar(ItemCategory itemCategory){

        Toolbar toolbar = findViewById(R.id.toolbarEquipCategory2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        try {
            toolbar.setTitle(itemCategory.getName());
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Intent(CategoryActivity.this, Equipment_accessories_Activity.class);
                    finish();
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
        new Intent(CategoryActivity.this, Equipment_accessories_Activity.class);
        finish();
    }
}
