package l.chernenkiy.aqua.ShoppingBasket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import java.util.Objects;

import l.chernenkiy.aqua.Equipment.EquipmentAccessActivity;
import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.Support;
import l.chernenkiy.aqua.R;

import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;
import static l.chernenkiy.aqua.ShoppingBasket.ShoppingBasket.btnOrder;

public class EquipmentBasket extends Fragment {

    @SuppressLint("StaticFieldLeak")
    public static AdapterEquip adapterEquip;
    @SuppressLint("StaticFieldLeak")
    public static ListView lvShopEquipBasket;
    @SuppressLint("StaticFieldLeak")
    public static TextView tvNotItemsCart;
    @SuppressLint("StaticFieldLeak")
    public static TextView tvBackToCatalog;

    Support support = new Support();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.basket_equip, container, false);

        lvShopEquipBasket = view.findViewById(R.id.shopping_basketEquip_list);
        tvNotItemsCart = view.findViewById(R.id.tv_equip_not_item_cart);
        tvBackToCatalog = view.findViewById(R.id.tv_equip_back_to_catalog);

        if(!cartEquipmentItem.isEmpty()){
            tvNotItemsCart.setVisibility(View.INVISIBLE);
            tvBackToCatalog.setVisibility(View.INVISIBLE);
        }

        tvBackToCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EquipmentAccessActivity.class);
                startActivity(intent);
            }
        });

        adapterEquip = new AdapterEquip(getContext(), cartEquipmentItem);
        support.sortShopBaskAlphabetical(cartEquipmentItem);
        lvShopEquipBasket.setAdapter(adapterEquip);

        cartItemOnClick();

        deleteItem (tvNotItemsCart, tvBackToCatalog);

        hideKeyboard();

        return view;
    }

    private void deleteItem(final TextView tvNotItemsCart, final TextView tvBackToCatalog) {
        lvShopEquipBasket.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Dialog dialogDeleteItem = new Dialog(getActivity(), R.style.FullHeightDialog);
                dialogDeleteItem.setContentView(R.layout.dialog_delete_item);
                dialogDeleteItem.getWindow().setBackgroundDrawable(new ColorDrawable (Color.TRANSPARENT));

                Button btnDeleteCancel = dialogDeleteItem.findViewById(R.id.cancel_btn_dialog_deleteItem);
                btnDeleteCancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        dialogDeleteItem.dismiss();
                    }
                });
                Button btnDeleteItem = dialogDeleteItem.findViewById(R.id.ok_btn_dialog_deleteItem);
                btnDeleteItem.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View view) {
                        cartEquipmentItem.remove(i);
                        btnOrder.setText ("Сумма покупки " + CartHelper.finalSumOrder(cartItems , cartEquipmentItem)+ " грн.");
                        adapterEquip = new AdapterEquip(getContext(), cartEquipmentItem);
                        lvShopEquipBasket.setAdapter(adapterEquip);
                        if (cartAddItemText != null) {
                            int cartItemText = Integer.parseInt((String) cartAddItemText.getText());
                            String newCartItemText = String.valueOf((cartItemText - 1));
                            cartAddItemText.setText(newCartItemText);
                        }
                        if(cartEquipmentItem.isEmpty()){
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
    }

    @SuppressLint("ClickableViewAccessibility")
    private void hideKeyboard() {
        lvShopEquipBasket.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                return false;
            }
        });
    }


    private void updateCartItemShop(final int i, final Dialog dialog, final EditText editQuantity) {
        final Button btnEditQuantity = dialog.findViewById(R.id.edit_quantity_btn);
        btnEditQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                String quantityFish = editQuantity.getText().toString();

                if (editQuantity.length () < 1) {
                    support.showToast (getContext(), "Укажите количество");
                    return;
                }

                cartEquipmentItem.get(i).put("quantity", quantityFish);

                ArrayList<HashMap<String, String> > editListQuantity = cartEquipmentItem;

                editListQuantity.get(i).put("quantity", quantityFish);

                btnOrder.setText ("Купить за " + CartHelper.finalSumOrder(cartItems , cartEquipmentItem)+ " грн.");

                AdapterEquip adapterEquip = new AdapterEquip(getContext(), editListQuantity);
                lvShopEquipBasket.setAdapter(adapterEquip);

                dialog.dismiss();
            }
        });
    }

    private void cartItemOnClick (){
        lvShopEquipBasket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Dialog dialog = new Dialog(getActivity(), R.style.FullHeightDialog);
                dialog.setContentView(R.layout.dialog_edit_quantity);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable (Color.TRANSPARENT));

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
}
