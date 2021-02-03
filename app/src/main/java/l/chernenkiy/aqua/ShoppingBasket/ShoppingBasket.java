package l.chernenkiy.aqua.ShoppingBasket;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
import static l.chernenkiy.aqua.MainActivity.listFish;
import static l.chernenkiy.aqua.MainActivity.sizeListFish;
import static l.chernenkiy.aqua.ShoppingBasket.EquipmentBasket.lvShopEquipBasket;
import static l.chernenkiy.aqua.ShoppingBasket.EquipmentBasket.adapterEquip;
import static l.chernenkiy.aqua.ShoppingBasket.EquipmentBasket.tvBackToCatalog;
import static l.chernenkiy.aqua.ShoppingBasket.EquipmentBasket.tvNotItemsCart;
import static l.chernenkiy.aqua.ShoppingBasket.FishBasket.lvShopBasket;
import static l.chernenkiy.aqua.ShoppingBasket.FishBasket.adapterFish;
import static l.chernenkiy.aqua.ShoppingBasket.FishBasket.tvFishBackToCatalog;
import static l.chernenkiy.aqua.ShoppingBasket.FishBasket.tvFishNotItemsCart;


public class ShoppingBasket extends AppCompatActivity {
    Toolbar toolbar;
    SectionPageAdapter mSectionPageAdapter;
    public static ViewPager vp;
    public static Button btnOrder;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shop_bask, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear_shop_bask) {
            if (cartItems.isEmpty() && cartEquipmentItem.isEmpty ()){
                return false;
            }

            final Dialog dialogClearCart = new Dialog(ShoppingBasket.this, R.style.FullHeightDialog);
            dialogClearCart.setContentView(R.layout.dialog_clear_cart);
            dialogClearCart.getWindow().setBackgroundDrawable(new ColorDrawable (Color.TRANSPARENT));

            final Button btnCancelClearCart = dialogClearCart.findViewById(R.id.cancel_btn_dialog_clearCart);
            btnCancelClearCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogClearCart.dismiss();
                }
            });

            final Button btnOkClearCart = dialogClearCart.findViewById(R.id.clear_btn_dialog_clearCart);
            btnOkClearCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearFishAndEquipment ( );

                    calcItemText ( );

                    btnOrder.setVisibility(View.INVISIBLE);

                    updateAdapters ( );

                    visibleTextFragment ( );

                    dialogClearCart.dismiss();
                }


            });
            dialogClearCart.setCancelable(false);
            dialogClearCart.show();

        }
        return true;
    }

    private void visibleTextFragment() {
        tvNotItemsCart.setVisibility(View.VISIBLE);
        tvBackToCatalog.setVisibility(View.VISIBLE);
        tvFishNotItemsCart.setVisibility(View.VISIBLE);
        tvFishBackToCatalog.setVisibility(View.VISIBLE);
    }

    private void updateAdapters() {
        lvShopBasket.setAdapter (null);
        lvShopEquipBasket.setAdapter (null);

        adapterEquip.notifyDataSetChanged ();
        adapterFish.notifyDataSetChanged ();
        mSectionPageAdapter.notifyDataSetChanged ();
    }

    private void calcItemText() {
        cartAddItemText.setText("");
        cartAddItemText.setVisibility(View.INVISIBLE);

        CartHelper.calculateItemsCartMain ();
        CartHelper.calculateItemsCart();
    }

    private void clearFishAndEquipment() {
        cartItems.removeAll(cartItems);
        cartEquipmentItem.removeAll (cartEquipmentItem);

        getIntent().removeExtra("cartItems");
        getIntent().removeExtra("cartEquipmentItem");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_shopping);

        if(sizeListFish == 0){
            sizeListFish = listFish.size();
        }

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
        btnOrder.setText ("Сумма покупки " + CartHelper.finalSumOrder()+ " грн.");
        if (!cartItems.isEmpty() || !cartEquipmentItem.isEmpty())
        {
            btnOrder.setVisibility(View.VISIBLE);
        }
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingBasket.this, Order.class);
                startActivity(intent);
            }
        });

        BottomNavigationView navigation = findViewById(R.id.nav_bar_bottom);
        NavigationBar.itemSelected (navigation, getApplicationContext (), 0);
        navigation.getMenu().getItem(0).setCheckable(false);
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
                onBackPressed ();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Class onBackClass = (Class) getIntent ().getSerializableExtra ("class");
        if (onBackClass == null){
            finish ();
        } else {
            Intent intent = new Intent(ShoppingBasket.this, onBackClass);
            intent.putExtra("position", getIntent ().getSerializableExtra ("position"));
            startActivity (intent);
            getIntent ().removeExtra ("class");
            finish ();
        }
    }


}
