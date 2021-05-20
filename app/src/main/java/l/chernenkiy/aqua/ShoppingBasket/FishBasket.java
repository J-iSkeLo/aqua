package l.chernenkiy.aqua.ShoppingBasket;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import l.chernenkiy.aqua.Fish.CategoryFish;
import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.Support;
import l.chernenkiy.aqua.R;

import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;
import static l.chernenkiy.aqua.ShoppingBasket.ShoppingBasket.btnOrder;

public class FishBasket extends Fragment {

    @SuppressLint("StaticFieldLeak")
    public static AdapterFish adapterFish;

    @SuppressLint("StaticFieldLeak")
    public static ListView lvShopBasket;

    @SuppressLint("StaticFieldLeak")
    public static TextView tvFishNotItemsCart;

    @SuppressLint("StaticFieldLeak")
    public static TextView tvFishBackToCatalog;

    Support support = new Support();


    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.basket_fish, container, false);

        lvShopBasket = view.findViewById(R.id.shopping_basket_list);
        tvFishNotItemsCart = view.findViewById(R.id.txt_not_item_cart);
        tvFishBackToCatalog = view.findViewById(R.id.txt_back_to_catalog);


        if(!cartItems.isEmpty()){
            tvFishNotItemsCart.setVisibility(View.INVISIBLE);
            tvFishBackToCatalog.setVisibility(View.INVISIBLE);

        }
        tvFishBackToCatalog.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), CategoryFish.class);
            startActivity(intent);
        });

        adapterFish = new AdapterFish(getActivity(), cartItems);
        support.sortShopBaskAlphabetical(cartItems);
        lvShopBasket.setAdapter(adapterFish);
        cartItemOnClick();

        lvShopBasket.setOnItemLongClickListener((adapterView, view12, i, l) -> {

            final Dialog dialogDeleteItem = new Dialog(getActivity(), R.style.FullHeightDialog);
            dialogDeleteItem.setContentView(R.layout.dialog_delete_item);
            dialogDeleteItem.getWindow().setBackgroundDrawable(new ColorDrawable (Color.TRANSPARENT));

            Button btnDeleteCancel = dialogDeleteItem.findViewById(R.id.cancel_btn_dialog_deleteItem);
            btnDeleteCancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view12) {
                    dialogDeleteItem.dismiss();
                }
            });
            Button btnDeleteItem = dialogDeleteItem.findViewById(R.id.ok_btn_dialog_deleteItem);
            btnDeleteItem.setOnClickListener(view121 -> {

                cartItems.remove(i);
                btnOrder.setText ("Купить за " + CartHelper.finalSumOrder(cartItems , cartEquipmentItem)+ " грн.");
                adapterFish = new AdapterFish(getContext(), cartItems);
                lvShopBasket.setAdapter(adapterFish);
                int cartItemText = Integer.parseInt((String) cartAddItemText.getText());
                String newCartItemText = String.valueOf((cartItemText-1));
                cartAddItemText.setText(newCartItemText);
                if(cartItems.isEmpty()){
                    tvFishNotItemsCart.setVisibility(View.VISIBLE);
                    tvFishBackToCatalog.setVisibility(View.VISIBLE);
                }
                if (cartEquipmentItem.isEmpty() && cartItems.isEmpty ())
                {
                    if (btnOrder != null){
                        btnOrder.setVisibility (View.INVISIBLE);
                    }
                    return;
                }
                dialogDeleteItem.dismiss();
            });
            dialogDeleteItem.setCancelable(false);
            dialogDeleteItem.show();

            return true;
        });

        hideKeyboard();

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void hideKeyboard() {
        lvShopBasket.setOnTouchListener((view, motionEvent) -> {
            InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
            return false;
        });
    }


    private void cartItemOnClick (){
        lvShopBasket.setOnItemClickListener((adapterView, view, i, l) -> {
            final Dialog dialog = new Dialog(getActivity(), R.style.FullHeightDialog);
            dialog.setContentView(R.layout.dialog_edit_quantity);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable (Color.TRANSPARENT));

            final EditText editQuantity = dialog.findViewById(R.id.quantity_edit_dialog);
            final Button btnCancelDialog = dialog.findViewById(R.id.cancel_dialog_quantity__btn);
            btnCancelDialog.setOnClickListener(view1 -> dialog.dismiss());

            updateCartItemShop(i, dialog, editQuantity);
            dialog.show();

        });

    }
    @SuppressLint("SetTextI18n")
    private void updateCartItemShop(final int i, final Dialog dialog, final EditText editQuantity) {
        final Button btnEditQuantity = dialog.findViewById(R.id.edit_quantity_btn);
        btnEditQuantity.setOnClickListener(view -> {

            String quantityFish = editQuantity.getText().toString();

            if (editQuantity.length () < 1) {
                Support support = new Support();
                support.showToast (getContext(), "Укажите количество");
                return;
            }

            cartItems.get(i).put("quantity", quantityFish);

            ArrayList<HashMap<String, String> > editListQuantity = cartItems;

            editListQuantity.get(i).put("quantity", quantityFish);

            btnOrder.setText ("Сумма покупки " + CartHelper.finalSumOrder(cartItems , cartEquipmentItem)+ " грн.");

            adapterFish = new AdapterFish(getContext(), editListQuantity);
            lvShopBasket.setAdapter(adapterFish);

            dialog.dismiss();
        });
    }
}
