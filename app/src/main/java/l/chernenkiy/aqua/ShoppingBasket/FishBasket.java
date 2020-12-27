package l.chernenkiy.aqua.ShoppingBasket;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.util.ArrayList;
import java.util.HashMap;

import l.chernenkiy.aqua.Fish.Fish;
import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.R;
import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;
import static l.chernenkiy.aqua.ShoppingBasket.ShopBaskTest.btnOrder;

public class FishBasket extends Fragment {
    private static final String TAG = "Рыба";
    ShopBaskFishAdapter shopBaskFishAdapter;
    public static ListView lvShopBasket;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fish_basket_test, container, false);

        lvShopBasket = view.findViewById(R.id.shopping_basket_list);
        final ArrayList<HashMap> cartItemsShop = (ArrayList<HashMap>) getActivity().getIntent().getExtras().get("cartItems");
        final TextView tvNotItemsCart = view.findViewById(R.id.txt_not_item_cart);
        final TextView tvBackToCatalog = view.findViewById(R.id.txt_back_to_catalog);


        if(!cartItems.isEmpty()){
            tvNotItemsCart.setVisibility(View.INVISIBLE);
            tvBackToCatalog.setVisibility(View.INVISIBLE);

        }
        tvBackToCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(getActivity(), Fish.class);
                    startActivity(intent);
            }
        });

        shopBaskFishAdapter = new ShopBaskFishAdapter(getActivity(), cartItemsShop);

        lvShopBasket.setAdapter(shopBaskFishAdapter);
        cartItemOnClick(view);

        lvShopBasket.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                final Dialog dialogDeleteItem = new Dialog(getActivity(), R.style.FullHeightDialog);
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
                        btnOrder.setText ("Купить за " + CartHelper.finalSumOrder()+ " грн.");
                        shopBaskFishAdapter = new ShopBaskFishAdapter(getContext(), cartItems);
                        lvShopBasket.setAdapter(shopBaskFishAdapter);
                        Integer cartItemText = Integer.valueOf((String) cartAddItemText.getText());
                        String newCartItemText = String.valueOf((cartItemText-1));
                        cartAddItemText.setText(newCartItemText);
                        if(cartItems.isEmpty()){
                            tvNotItemsCart.setVisibility(View.VISIBLE);
                            tvBackToCatalog.setVisibility(View.VISIBLE);
                        }
                        if (cartEquipmentItem.isEmpty() && cartItems.isEmpty ())
                        {
                            btnOrder.setVisibility (View.INVISIBLE);
                        }
                        dialogDeleteItem.dismiss();
                    }
                });
                dialogDeleteItem.setCancelable(false);
                dialogDeleteItem.show();

                return true;
            }
        });

        hideKeyboard();

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void hideKeyboard() {
        lvShopBasket.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                return false;
            }
        });
    }


    private void cartItemOnClick (View v){
        lvShopBasket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Dialog dialog = new Dialog(getActivity(), R.style.FullHeightDialog);
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

                btnOrder.setText ("Купить за " + CartHelper.finalSumOrder()+ " грн.");

                shopBaskFishAdapter = new ShopBaskFishAdapter(getContext(), editListQuantity);
                lvShopBasket.setAdapter(shopBaskFishAdapter);

                dialog.dismiss();
            }
        });
    }



}
