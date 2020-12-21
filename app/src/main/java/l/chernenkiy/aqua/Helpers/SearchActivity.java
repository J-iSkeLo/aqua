package l.chernenkiy.aqua.Helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.ortiz.touchview.TouchImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import l.chernenkiy.aqua.Equipment.Adapters.EquipmentListAdapter;
import l.chernenkiy.aqua.Equipment.Items.ItemCategory;
import l.chernenkiy.aqua.Equipment.Items.ItemEquipment;
import l.chernenkiy.aqua.R;
import l.chernenkiy.aqua.ShoppingBasket.ShopBaskTest;

import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;
import static l.chernenkiy.aqua.MainActivity.listAquariums;
import static l.chernenkiy.aqua.MainActivity.listChemistry;
import static l.chernenkiy.aqua.MainActivity.listEquip;
import static l.chernenkiy.aqua.MainActivity.listFeed;

public class SearchActivity extends AppCompatActivity {
    EquipmentListAdapter adapter;
    MenuItem cartIconMenuItem;
    SearchView searchView;
    ImageButton cartImageBtn;
    public static ListView lvSearch;
    public  static ArrayList <ItemEquipment> listResultSearch;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item, menu);
        cartIconMenuItem = menu.findItem(R.id.cart_count_menu_item);
        final MenuItem menuSearchItem = menu.findItem(R.id.app_bar_search);
        final View actionView = cartIconMenuItem.getActionView();
        searchView = (SearchView) MenuItemCompat.getActionView(menuSearchItem);

        searchView.setQueryHint("Поиск позиции...");
        searchView.setIconifiedByDefault(true);
        searchView.setImeOptions (EditorInfo.IME_ACTION_SEARCH);
        searchView.setFocusable(true);
        searchView.setIconified(false);

        searchView.setOnQueryTextListener (new SearchView.OnQueryTextListener ( ) {
            @Override
            public boolean onQueryTextSubmit(String query) {
                lvSearch = findViewById (R.id.list_search);
                TextView tvNotFoundSearch = findViewById (R.id.tv_not_found_search);

                adapter = new EquipmentListAdapter (getApplicationContext(), searchItem (query.toLowerCase(Locale.getDefault())));
                listResultSearch = new ArrayList<>(searchItem (query.toLowerCase(Locale.getDefault())));
                lvSearch.setAdapter (adapter);
                adapter.notifyDataSetChanged ();

                if(lvSearch.getCount () == 0){
                    tvNotFoundSearch.setVisibility (View.VISIBLE);
                }else{
                    tvNotFoundSearch.setVisibility (View.INVISIBLE);
                }

                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getApplicationContext (),
                        MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
                suggestions.saveRecentQuery(query, null);

                searchView.clearFocus();
                searchView.setIconifiedByDefault(true);
                searchView.onActionViewCollapsed ();

                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

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
                onBackPressed();
            }
        });

        lvSearch = findViewById (R.id.list_search);

        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Dialog dialog = new Dialog(SearchActivity.this, R.style.FullHeightDialog);
                dialog.setContentView(R.layout.dialog_item_equip_set);

                final ItemEquipment item = listResultSearch.get (i);

                TextView nameEquip = dialog.findViewById(R.id.name_dialog_equip);
                TextView articleEquip = dialog.findViewById(R.id.article_dialog);
                TextView producerEquip = dialog.findViewById(R.id.dialog_producer_equip);
                TextView priceEquip = dialog.findViewById(R.id.priceEquip_dialog);
                TextView descriptionEquip = dialog.findViewById(R.id.dialog_description_equip);
                TouchImageView touchImageView = dialog.findViewById(R.id.imageTouchEquip);

                final TextView quantity = dialog.findViewById(R.id.quantity_equip_dialog);

                nameEquip.setText(item.getName());
                articleEquip.setText("Артикул: " + item.getArticle());
                producerEquip.setText(item.getGeneralColKey ());
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

                        Map<String, String> singleEquipItem = new HashMap<> ();
                        singleEquipItem.put("name", item.getName());
                        singleEquipItem.put("article", item.getArticle());
                        singleEquipItem.put("producer", item.getGeneralColKey ());
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


        hideKeyboard();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void hideKeyboard() {
        lvSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                return false;
            }
        });
    }


    public ArrayList<ItemEquipment> searchItem (String query) {
        if (query == "") return new ArrayList<> ();

        ArrayList resultSearch = new ArrayList<ItemEquipment>();

        addMatchingItem (query, resultSearch, listEquip);
        addMatchingItem (query, resultSearch, listAquariums);
        addMatchingItem (query, resultSearch, listChemistry);
        addMatchingItem (query, resultSearch, listFeed);

        return resultSearch;
    }

    private void addMatchingItem(String query, ArrayList category, ArrayList<ItemCategory> list) {
        for (int i = 0; i<list.size(); i++){
            ArrayList<ItemEquipment> items = list.get(i).getItems();

            for (int j = 0; j<items.size(); j++){

                String name = items.get (j).getName ().toLowerCase(Locale.getDefault());
                String generalColKey = items.get (j).getGeneralColKey ().toLowerCase (Locale.getDefault ());

                boolean queryContainsName = name != null && name.contains (query);
                boolean queryContainsProducer = generalColKey != null && generalColKey.contains (query);

                if (queryContainsName || queryContainsProducer){
                    category.add (items.get (j));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Class onBackClass = (Class) getIntent ().getSerializableExtra ("class");
        if (onBackClass.equals (null)){
            return;
        } else {
            Intent intent = new Intent(getApplicationContext (), onBackClass);
            intent.putExtra("cartItems", cartItems);
            intent.putExtra("cartEquipmentItem", cartEquipmentItem);
            startActivity (intent);
            getIntent ().removeExtra ("class");
            finish ();
        }


    }
}
