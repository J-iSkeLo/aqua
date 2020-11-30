package l.chernenkiy.aqua.Equipment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.MainActivity;
import l.chernenkiy.aqua.R;
import l.chernenkiy.aqua.ShoppingBasket.ShopBaskTest;
import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;

public class EquipmentActivity extends AppCompatActivity {

    MenuItem cartIconMenuItem;
    ImageButton cartImageBtn;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item_basket, menu);
        cartIconMenuItem = menu.findItem(R.id.cart_menu_item);
        final View actionView = cartIconMenuItem.getActionView();



        if (actionView != null) {
            cartAddItemText = actionView.findViewById(R.id.text_item_cart);
            cartImageBtn = actionView.findViewById(R.id.btn_image_cart);
            if (!cartItems.isEmpty() || !cartEquipmentItem.isEmpty()) {
                CartHelper.calculateItemsCart();
            }
        }

        cartImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View actionView) {
                Intent intent = new Intent(EquipmentActivity.this, ShopBaskTest.class);
                intent.putExtra("cartItems", cartItems);
                intent.putExtra("cartEquipmentItem", cartEquipmentItem);
                startActivity(intent);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_activity);

        cartAddItemText = findViewById(R.id.text_item_cart);

        Toolbar toolbar = findViewById(R.id.toolbarEquip);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Intent(EquipmentActivity.this, MainActivity.class);
                finish();
            }
        });

        Button btnEquipAccess = findViewById(R.id.btnEquipAcces);
        btnEquipAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(EquipmentActivity.this, Equipment_accessories_Activity.class);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Button btnFeed = findViewById(R.id.btnFeed);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(EquipmentActivity.this, Feed.class);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Button btnChemistry = findViewById(R.id.btnChemistry);
        btnChemistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(EquipmentActivity.this, Chemistry.class);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Button btnAquariums = findViewById(R.id.btnAquarium);
        btnAquariums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(EquipmentActivity.this, Aquariums.class);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        new Intent(EquipmentActivity.this, MainActivity.class);
        finish();
    }
}
