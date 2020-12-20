package l.chernenkiy.aqua.Equipment;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import java.util.ArrayList;
import java.util.Locale;

import l.chernenkiy.aqua.Equipment.Adapters.EquipmentListAdapter;
import l.chernenkiy.aqua.Equipment.Items.ItemEquipment;
import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.MySuggestionProvider;
import l.chernenkiy.aqua.MainActivity;
import l.chernenkiy.aqua.R;
import l.chernenkiy.aqua.ShoppingBasket.ShopBaskTest;

import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;
import static l.chernenkiy.aqua.MainActivity.listEquip;

public class SearchActivity extends AppCompatActivity {
    EquipmentListAdapter adapter;
    MenuItem cartIconMenuItem;
    SearchView searchView;
    ImageButton cartImageBtn;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item, menu);
        cartIconMenuItem = menu.findItem(R.id.cart_count_menu_item);
        final MenuItem menuSearchItem = menu.findItem(R.id.app_bar_search);
        final View actionView = cartIconMenuItem.getActionView();
        searchView = (SearchView) MenuItemCompat.getActionView(menuSearchItem);
        searchView.setQueryHint("Поиск позиции...");
        searchView.setIconifiedByDefault(false);
        searchView.setImeOptions (EditorInfo.IME_ACTION_DONE);
        searchView.setFocusable(true);
        searchView.setIconified(false) ;

        searchView.setOnQueryTextListener (new SearchView.OnQueryTextListener ( ) {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ListView lvSearch = findViewById (R.id.list_search);
                TextView tvNotFoundSearch = findViewById (R.id.tv_not_found_search);

                adapter = new EquipmentListAdapter (getApplicationContext(), searchItem (query.toLowerCase(Locale.getDefault())));
                lvSearch.setAdapter (adapter);
                adapter.notifyDataSetChanged ();

                if(lvSearch.getCount () == 0){
                    tvNotFoundSearch.setVisibility (View.VISIBLE);
                }else{
                    tvNotFoundSearch.setVisibility (View.INVISIBLE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
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
                Intent intent = new Intent(SearchActivity.this, ShopBaskTest.class);
                intent.putExtra("cartItems", cartItems);
                intent.putExtra("cartEquipmentItem", cartEquipmentItem);
                startActivity(intent);
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, EquipmentAccessActivity.class);
                intent.putExtra("cartItems", cartItems);
                intent.putExtra("cartEquipmentItem", cartEquipmentItem);
                startActivity (intent);
            }
        });



        Intent intent  = getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
        }

    }

    public ArrayList<ItemEquipment> searchItem (String query) {
        if (query == "") return new ArrayList<> ();

        ArrayList category = new ArrayList<ItemEquipment>();

        for (int i = 0; i<listEquip.size(); i++){
            ArrayList<ItemEquipment> items = listEquip.get(i).getItems();


            for (int j = 0; j<items.size(); j++){
                String name = items.get (j).getName ().toLowerCase(Locale.getDefault());
                if (name != null && name.contains (query)){
                    category.add (items.get (j));
                }
            }
        }
        return category;
    }
}
