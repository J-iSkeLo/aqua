package l.chernenkiy.aqua.ShoppingBasket;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import l.chernenkiy.aqua.Delivery.SectionPageAdapter;
import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.MainActivity;
import l.chernenkiy.aqua.My_Order.Order;
import l.chernenkiy.aqua.R;

import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;


public class ShopBaskTest extends AppCompatActivity {
    Toolbar toolbar;
    public SectionPageAdapter mSectionPageAdapter;
    private ViewPager vp;
    MenuItem cartIconMenuItem;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_shop_bask, menu);

        cartIconMenuItem = menu.findItem(R.id.menu_shopBask_item);
        String finalSumOrder;
        finalSumOrder = "Сумма заказа: " + CartHelper.finalSumOrder()+ " грн.";

        SpannableString s  = new SpannableString(finalSumOrder);
        s.setSpan(new TextAppearanceSpan(this, R.style.ColorTextFinalSumOrder),0,s.length(),0);

        cartIconMenuItem.setTitle(s);

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

        Button btnOrder = findViewById(R.id.btnOrder);
        if (!cartItems.isEmpty()|| !cartEquipmentItem.isEmpty())
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
