package l.chernenkiy.aqua.LastOrder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import l.chernenkiy.aqua.Helpers.SectionPageAdapter;
import l.chernenkiy.aqua.MainActivity;
import l.chernenkiy.aqua.R;

import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;
import static l.chernenkiy.aqua.MainActivity.listFish;
import static l.chernenkiy.aqua.MainActivity.sizeListFish;


public class LastOrder extends AppCompatActivity {

    public static SectionPageAdapter mSectionPageAdapter;
    public static ViewPager vp;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_order);


        if(sizeListFish == 0){
            sizeListFish = listFish.size();
        }
        toolbarCreate();

        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        vp = findViewById(R.id.containerLastOrder);
        setupViewPager(vp);

        TabLayout tabLayout = findViewById(R.id.tabLayoutLastOrder);
        tabLayout.setupWithViewPager(vp);

        if (cartItems.isEmpty () && !cartEquipmentItem.isEmpty ()){
            vp.setCurrentItem (1);
        }
        else vp.setCurrentItem (0);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LastOrder.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    public void setupViewPager(ViewPager vp){
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new LastOrderFish(), "Рыба");
        adapter.addFragment(new LastOrderEquipment(), "Оборудование");
        vp.setAdapter(adapter);
    }

    public void toolbarCreate() {
        Toolbar toolbar = findViewById(R.id.toolbar6);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}