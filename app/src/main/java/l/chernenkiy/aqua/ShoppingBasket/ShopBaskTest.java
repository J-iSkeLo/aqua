package l.chernenkiy.aqua.ShoppingBasket;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.NavigationBar;
import l.chernenkiy.aqua.Helpers.SectionPageAdapter;
import l.chernenkiy.aqua.Order.Order;
import l.chernenkiy.aqua.R;

import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;


public class ShopBaskTest extends AppCompatActivity {
    Toolbar toolbar;
    public SectionPageAdapter mSectionPageAdapter;
    private ViewPager vp;
    public static Button btnOrder;
    MenuItem cartMenuItem;
    ImageButton clearShopBask_btn;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shop_bask, menu);
        cartMenuItem = menu.findItem (R.id.clear_shop_bask);
//        final View actionView = cartMenuItem.getActionView();

        if (cartEquipmentItem.isEmpty () && cartItems.isEmpty ()){
            cartMenuItem.setOnMenuItemClickListener (new MenuItem.OnMenuItemClickListener ( ) {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    return false;
                }
            });
        } else {
            cartMenuItem.setOnMenuItemClickListener (new MenuItem.OnMenuItemClickListener ( ) {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    final Dialog dialogDeleteItem = new Dialog(ShopBaskTest.this, R.style.Theme_AppCompat_DayNight_Dialog);
                    dialogDeleteItem.setContentView(R.layout.dialog_delete_item);
                    dialogDeleteItem.setTitle ("Очистить корзину");
                    Button btnDeleteCancel = dialogDeleteItem.findViewById(R.id.cancel_btn_dialog_deleteItem);
                    btnDeleteCancel.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            dialogDeleteItem.dismiss();
                        }
                    });
                    Button btnRemoveShopBask = dialogDeleteItem.findViewById(R.id.ok_btn_dialog_deleteItem);
                    btnRemoveShopBask.setText ("Очистить");
                    btnRemoveShopBask.setOnClickListener (new View.OnClickListener ( ) {
                        @Override
                        public void onClick(View view) {
                            cartItems.removeAll (cartItems);
                            cartEquipmentItem.removeAll (cartEquipmentItem);
                            CartHelper.calculateItemsCart ();
                            CartHelper.calculateItemsCartMain ();
                            btnOrder.setVisibility (View.INVISIBLE);
                            dialogDeleteItem.dismiss();
                        }
                    });

                    dialogDeleteItem.setCancelable(false);
                    dialogDeleteItem.show();

                    return true;
                }
            });
        }



        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppin_basket_test);

        toolbar();

        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        vp = findViewById(R.id.containerShopBask);
        setupViewPager(vp);

        TabLayout tabLayout = findViewById(R.id.tabLayoutShopBask);
        tabLayout.setupWithViewPager(vp);

        if (cartItems.isEmpty () && !cartEquipmentItem.isEmpty ()){
            vp.setCurrentItem (1);
        }
        else vp.setCurrentItem (0);

        btnOrder = findViewById(R.id.btnOrder);
        btnOrder.setText ("Купить за " + CartHelper.finalSumOrder()+ " грн.");
        if (!cartItems.isEmpty() || !cartEquipmentItem.isEmpty())
        {
            btnOrder.setVisibility(View.VISIBLE);
        }
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopBaskTest.this, Order.class);
                startActivity(intent);
            }
        });

        BottomNavigationView navigation = findViewById(R.id.nav_bar_bottom);
        NavigationBar.itemSelected (navigation, getApplicationContext (), 0);
        overridePendingTransition (0, 0);

    }

    private void setupViewPager(ViewPager vp){
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FishBasket(), "Рыба");
        adapter.addFragment(new EquipmentBasket(), "Оборудование");
        vp.setAdapter(adapter);
    }


    private void toolbar() {
        toolbar = findViewById(R.id.toolbarShopBask);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }


}
