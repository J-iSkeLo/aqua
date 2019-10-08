package l.chernenkiy.aqua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

import static l.chernenkiy.aqua.Fish.cartAddItemText;
import static l.chernenkiy.aqua.Fish.cartItems;


public class ShoppingBasket extends AppCompatActivity {

    public static ListView lvShopBasket;
    public CartListAdapter cartListAdapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_shop_cart, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search_cart);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.mr_chooser_searching));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cartListAdapter.myFilter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_cart_clear) {
            if (cartItems.isEmpty()){
                return false;
            }

            final Dialog dialogClearCart = new Dialog(ShoppingBasket.this, R.style.FullHeightDialog);
            dialogClearCart.setContentView(R.layout.dialog_clear_cart);

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
                    cartItems.removeAll(cartItems);
                    lvShopBasket.setAdapter(null);
                    getIntent().removeExtra("cartItems");
                    cartListAdapter.notifyDataSetChanged();
                    cartAddItemText.setText("");
                    cartAddItemText.setVisibility(View.INVISIBLE);
                    calcAndSetFinalSum();
                    TextView tvNotItemsCart = findViewById(R.id.txt_not_item_cart);
                    TextView tvBackToCatalog = findViewById(R.id.txt_back_to_catalog);
                    if(cartItems.isEmpty()){
                        tvNotItemsCart.setVisibility(View.VISIBLE);
                        tvBackToCatalog.setVisibility(View.VISIBLE);
                    }
                    Button btnOrder = findViewById(R.id.btn_order);
                    if (cartItems.isEmpty())btnOrder.setVisibility(View.INVISIBLE);

                    dialogClearCart.dismiss();
                }
            });
            dialogClearCart.setCancelable(false);
            dialogClearCart.show();

        }
        return true;
    }



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_basket);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Intent(ShoppingBasket.this, Fish.class);
                if(cartItems.isEmpty()){
                    cartAddItemText.setText("");
                    cartAddItemText.setVisibility(View.INVISIBLE);
                }
                finish();
            }
        });

        final TextView tvNotItemsCart = findViewById(R.id.txt_not_item_cart);
        final TextView tvBackToCatalog = findViewById(R.id.txt_back_to_catalog);
        if(!cartItems.isEmpty()){
            tvNotItemsCart.setVisibility(View.INVISIBLE);
            tvBackToCatalog.setVisibility(View.INVISIBLE);
        }
        tvBackToCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Intent(ShoppingBasket.this, Fish.class);
                if(cartItems.isEmpty()){
                    cartAddItemText.setText("");
                    cartAddItemText.setVisibility(View.INVISIBLE);
                }
                finish();
            }
        });

        lvShopBasket = findViewById(R.id.shopping_basket_list);
        lvShopBasket.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lvShopBasket.setItemsCanFocus(false);

        final ArrayList cartItemsShop = (ArrayList) getIntent().getExtras().get("cartItems");

        cartListAdapter = new CartListAdapter(getApplicationContext(), cartItemsShop);

        lvShopBasket.setAdapter(cartListAdapter);

        calcAndSetFinalSum();

        cartItemOnClick();

        lvShopBasket.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Dialog dialogDeleteItem = new Dialog(ShoppingBasket.this, R.style.FullHeightDialog);
                dialogDeleteItem.setContentView(R.layout.dialog_delete_item);
                Button btnDeleteCancel = dialogDeleteItem.findViewById(R.id.cancel_btn_dialog_deleteItem);
                btnDeleteCancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        dialogDeleteItem.dismiss();
                    }
                });
                Button btnDeleteItem = dialogDeleteItem.findViewById(R.id.ok_btn_dialog_deleteItem);
                btnDeleteItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cartItems.remove(i);
                        calcAndSetFinalSum();
                        cartListAdapter = new CartListAdapter(getApplicationContext(), cartItems);
                        lvShopBasket.setAdapter(cartListAdapter);
                        Integer cartItemText = Integer.valueOf((String) cartAddItemText.getText());
                        String newCartItemText = String.valueOf((cartItemText-1));
                        cartAddItemText.setText(newCartItemText);
                        if(!cartItems.isEmpty()){
                            tvNotItemsCart.setVisibility(View.INVISIBLE);
                            tvBackToCatalog.setVisibility(View.INVISIBLE);
                        }
                        Button btnOrder = findViewById(R.id.btn_order);
                        if (cartItems.isEmpty())btnOrder.setVisibility(View.INVISIBLE);
                        dialogDeleteItem.dismiss();
                    }
                });
                dialogDeleteItem.setCancelable(false);
                dialogDeleteItem.show();

                return true;
            }
        });

        Button btnOrder = findViewById(R.id.btn_order);
        if (!cartItems.isEmpty())btnOrder.setVisibility(View.VISIBLE);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingBasket.this, Order.class);
                startActivity(intent);
            }
        });

        hideKeyboard();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void hideKeyboard() {
        lvShopBasket.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                return false;
            }
        });
    }


    private void cartItemOnClick (){
        lvShopBasket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Dialog dialog = new Dialog(ShoppingBasket.this, R.style.FullHeightDialog);
                dialog.setContentView(R.layout.dialog_edit_quantity);

                final EditText editQuantity = dialog.findViewById(R.id.quantity_edit_dialog);
                final Button btnCancelDialog = dialog.findViewById(R.id.cancel_dialog_quantity__btn);
                btnCancelDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                updateCartItemShop(i, dialog, editQuantity);
                dialog.show();

            }

        });

    }

    private void updateCartItemShop(final int i, final Dialog dialog, final EditText editQuantity) {
        final Button btnEditQuantity = dialog.findViewById(R.id.edit_quantity_btn);
        btnEditQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String quantityFish = editQuantity.getText().toString();
                if (quantityFish.isEmpty() || Integer.parseInt(quantityFish) < 1) {
                    return;
                }
                cartItems.get(i).put("quantity", quantityFish);

                ArrayList<HashMap> editListQuantity = cartItems;

                editListQuantity.get(i).put("quantity", quantityFish);

                calcAndSetFinalSum();

                cartListAdapter = new CartListAdapter(getApplicationContext(), editListQuantity);
                lvShopBasket.setAdapter(cartListAdapter);

                dialog.dismiss();

            }


        });
    }

    public void calcAndSetFinalSum() {
        String finalSum = String.valueOf(CartHelper.countFinalSum(cartItems));
        TextView viewFinalSum = findViewById(R.id.txt_final_sum);
        if(cartItems.isEmpty()){
            viewFinalSum.setVisibility(View.INVISIBLE);
        }
        viewFinalSum.setText("Сумма: " + finalSum + " грн.");
    }


    @Override
    public void onBackPressed() {
        new Intent(ShoppingBasket.this, Fish.class);
        if(cartItems.isEmpty()){
            cartAddItemText.setText("");
            cartAddItemText.setVisibility(View.INVISIBLE);
        }
        finish();
    }
}


