package l.chernenkiy.aqua.Equipment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;

import l.chernenkiy.aqua.Fish.Fish;
import l.chernenkiy.aqua.Fish.ShoppingBasket;
import l.chernenkiy.aqua.MainActivity;
import l.chernenkiy.aqua.R;

public class EquipmentActivity extends AppCompatActivity {

    MenuItem cartIconMenuItem;
    ImageButton cartImageBtn;
    public static ArrayList<HashMap> cartEquipment = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item_basket, menu);
        cartIconMenuItem = menu.findItem(R.id.cart_menu_item);
        final View actionView = cartIconMenuItem.getActionView();

        if (actionView != null) {
            cartImageBtn = actionView.findViewById(R.id.btn_image_cart);
            }

        cartImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View actionView) {
                Intent intent = new Intent(EquipmentActivity.this, ShoppingBasket.class);
//                intent.putExtra("cartItems", cartItems);
                startActivity(intent);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);

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


    }

    @Override
    public void onBackPressed() {
        new Intent(EquipmentActivity.this, MainActivity.class);
        finish();
    }
}
