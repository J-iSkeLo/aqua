package l.chernenkiy.aqua.Helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.ortiz.touchview.TouchImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import l.chernenkiy.aqua.Equipment.Adapters.SearchListAdapter;
import l.chernenkiy.aqua.Equipment.Items.ItemCategory;
import l.chernenkiy.aqua.Equipment.Items.ItemEquipment;
import l.chernenkiy.aqua.R;

import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;
import static l.chernenkiy.aqua.MainActivity.lastClass;
import static l.chernenkiy.aqua.MainActivity.listAquariums;
import static l.chernenkiy.aqua.MainActivity.listChemistry;
import static l.chernenkiy.aqua.MainActivity.listEquip;
import static l.chernenkiy.aqua.MainActivity.listFeed;

public class SearchActivity extends AppCompatActivity {

    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    SearchListAdapter adapter;
    MenuItem cartIconMenuItem;
    SearchView searchView;
    ImageButton cartImageBtn;
    public static ListView lvSearch;
    public  static ArrayList <ItemEquipment> listResultSearch;
    Support support = new Support();

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item, menu);
        cartIconMenuItem = menu.findItem(R.id.cart_count_menu_item);
        final MenuItem menuSearchItem = menu.findItem(R.id.app_bar_search);
        final View actionView = cartIconMenuItem.getActionView();
        searchView = (SearchView) MenuItemCompat.getActionView(menuSearchItem);
        searchSetCustomization();
        searchItem();
        calculateItemsInShopBask(actionView);
        support.openShopBask(cartImageBtn, getApplicationContext(), SearchActivity.class);

        return super.onCreateOptionsMenu(menu);
    }

    private void searchSetCustomization() {
        searchView.setQueryHint("Поиск позиции...");
        searchView.setIconifiedByDefault(true);
        searchView.setImeOptions (EditorInfo.IME_ACTION_SEARCH);
        searchView.setFocusable(true);
        searchView.setIconified(false);
    }

    private void calculateItemsInShopBask(View actionView) {
        if (actionView != null) {
            cartAddItemText = actionView.findViewById(R.id.text_item_cart);
            cartImageBtn = actionView.findViewById(R.id.btn_image_cart);
            CartHelper.calculateItemsCart();
        }
    }

    private void searchItem() {
        searchView.setOnQueryTextListener (new SearchView.OnQueryTextListener ( ) {
            @Override
            public boolean onQueryTextSubmit(String query) {
                lvSearch = findViewById (R.id.list_search);
                TextView tvNotFoundSearch = findViewById (R.id.tv_not_found_search);

                adapter = new SearchListAdapter(getApplicationContext(), searchItem (query.toLowerCase(Locale.getDefault())));
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
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.ConnectingToInternet();
        lvSearch = findViewById (R.id.list_search);

        toolbarCreate();


        addItemInShopBask();

        hideKeyboard();

    }

    private void addItemInShopBask() {
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Dialog dialog = new Dialog(SearchActivity.this, R.style.FullHeightDialog);
                dialog.setContentView(R.layout.dialog_equip_set);

                final ItemEquipment item = listResultSearch.get (i);

                final TextView quantity = dialog.findViewById(R.id.quantity_equip_dialog);
                TextView nameEquip = dialog.findViewById(R.id.name_dialog_equip);
                TextView articleEquip = dialog.findViewById(R.id.article_dialog);
                TextView producerEquip = dialog.findViewById(R.id.dialog_producer_equip);
                TextView priceEquip = dialog.findViewById(R.id.priceEquip_dialog);
                TextView descriptionEquip = dialog.findViewById(R.id.dialog_description_equip);
                TouchImageView touchImageView = dialog.findViewById(R.id.imageTouchEquip);

                if(isInternetPresent) {
                    support.loadImage(touchImageView, item.getImage(), getApplicationContext());
                }
                else{
                    support.showToast(getApplicationContext(),"Нет интернет соединения для загрузки изображения!");
                }

                dialogSetText(item, nameEquip, articleEquip, producerEquip, priceEquip, descriptionEquip);

                Button btnCancelDialog = dialog.findViewById(R.id.cancel_dialogEquip_btn);
                Button btnAddShopBask = dialog.findViewById(R.id.addShopBaskEquip_btn);
                ImageButton btnCloseDialog = dialog.findViewById (R.id.btn_close_equip_dialog);

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
                            support.showToast(getApplicationContext(),"Укажите количество");
                            return;
                        }

                        HashMap<String, String> singleEquipItem = getStringStringHashMap(quantityEquip);

                        boolean hasDuplicate = CartHelper.findCartItem(singleEquipItem.get("name"),singleEquipItem.get("price"), cartEquipmentItem);

                        if (hasDuplicate)
                        {
                            support.showToast(getApplicationContext(),"Позиция уже в Корзине");
                        }
                        else
                        {
                            cartEquipmentItem.add(singleEquipItem);
                            CartHelper.calculateItemsCart();
                            dialog.dismiss();
                        }
                    }

                    @NotNull
                    private HashMap<String, String> getStringStringHashMap(String quantityEquip) {
                        HashMap<String, String> singleEquipItem = new HashMap<> ();
                        singleEquipItem.put("name", item.getName());
                        singleEquipItem.put("article", item.getArticle());
                        singleEquipItem.put("producer", item.getGeneralColKey ());
                        singleEquipItem.put("price", item.getPrice());
                        singleEquipItem.put("description", item.getDescription());
                        singleEquipItem.put("quantity", quantityEquip);
                        singleEquipItem.put ("image" , item.getImage ());
                        return singleEquipItem;
                    }
                });

                dialog.show();
            }

            private void dialogSetText(ItemEquipment item, TextView nameEquip,
                                       TextView articleEquip, TextView producerEquip,
                                       TextView priceEquip, TextView descriptionEquip) {
                nameEquip.setText(item.getName());
                articleEquip.setText("Артикул: " + item.getArticle());
                producerEquip.setText(item.getGeneralColKey ());
                priceEquip.setText(item.getPrice() + " грн.");
                descriptionEquip.setText(item.getDescription());
            }
        });
    }

    private void toolbarCreate() {
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
        if (query.equals("")) return new ArrayList<> ();

        ArrayList<ItemEquipment> resultSearch = new ArrayList<>();

        addMatchingItem (query, resultSearch, listEquip);
        addMatchingItem (query, resultSearch, listFeed);
        addMatchingItem (query, resultSearch, listChemistry);
        addMatchingItem (query, resultSearch, listAquariums);

        return resultSearch;
    }

    private void addMatchingItem(String query, ArrayList<ItemEquipment> arrayResultEquipment, ArrayList<ItemCategory> arrayEquipment) {

        for (int i = 0; i<arrayEquipment.size(); i++){

            ArrayList<ItemEquipment> itemsInCategory = arrayEquipment.get(i).getAllItems();

            for (int j = 0; j < itemsInCategory.size(); j++){
                ItemEquipment equipment = itemsInCategory.get(j);
                String name = equipment.getName().toLowerCase(Locale.getDefault());
                String generalColKey = equipment.getGeneralColKey().toLowerCase (Locale.getDefault ());
                boolean queryContainsName = name != null && name.contains (query);
                boolean queryContainsProducer = generalColKey != null && generalColKey.contains (query);
                if (queryContainsName || queryContainsProducer){
                    arrayResultEquipment.add (equipment);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Class onBackClass = (Class) getIntent ().getSerializableExtra ("class");
        Intent intent;

        if (onBackClass == null){
            intent = new Intent(getApplicationContext(), lastClass);
        } else {
            intent = new Intent(getApplicationContext(), onBackClass);
        }
        putCartItemsCartEquipItems(intent);
        CartHelper.calculateItemsCart ();
        startActivity (intent);
    }

    private void putCartItemsCartEquipItems(Intent intent) {
        intent.putExtra("cartItems", cartItems);
        intent.putExtra("cartEquipmentItem", cartEquipmentItem);
    }
}
