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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.MainActivity;
import l.chernenkiy.aqua.R;

import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;
import static l.chernenkiy.aqua.ShoppingBasket.ShopBaskTest.btnOrder;

public class EquipmentBasket extends Fragment {
    private static final String TAG = "Оборудование";

    public static ShopBaskEquipAdapter shopBaskEquipAdapter;
    public ArrayList<HashMap> cartEquipItemShop;

    public static ListView lvShopEquipBasket;

    public static TextView tvNotItemsCart;
    public static TextView tvBackToCatalog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.equip_basket_test, container, false);


        cartEquipItemShop  = (ArrayList<HashMap>) getExtras().get("cartEquipmentItem");
        lvShopEquipBasket = view.findViewById(R.id.shopping_basketEquip_list);

        tvNotItemsCart = view.findViewById(R.id.tv_equip_not_item_cart);
        tvBackToCatalog = view.findViewById(R.id.tv_equip_back_to_catalog);
        if(!cartEquipItemShop.isEmpty()){
            tvNotItemsCart.setVisibility(View.INVISIBLE);
            tvBackToCatalog.setVisibility(View.INVISIBLE);

        }
        tvBackToCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        shopBaskEquipAdapter = new ShopBaskEquipAdapter(getContext(), cartEquipItemShop);
        lvShopEquipBasket.setAdapter(shopBaskEquipAdapter);

        cartItemOnClick(view);

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
                        cartEquipmentItem.remove(i);
                        btnOrder.setText ("Купить за " + CartHelper.finalSumOrder()+ " грн.");
                        shopBaskEquipAdapter = new ShopBaskEquipAdapter (getContext(), cartEquipmentItem);
                        lvShopEquipBasket.setAdapter(shopBaskEquipAdapter);
                        Integer cartItemText = Integer.valueOf((String) cartAddItemText.getText());
                        String newCartItemText = String.valueOf((cartItemText-1));
                        cartAddItemText.setText(newCartItemText);
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

    private Bundle getExtras() {
        return getActivity().getIntent().getExtras();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void hideKeyboard() {
        lvShopEquipBasket.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                return false;
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
                cartEquipmentItem.get(i).put("quantity", quantityFish);

                ArrayList<HashMap> editListQuantity = cartEquipmentItem;

                editListQuantity.get(i).put("quantity", quantityFish);

                btnOrder.setText ("Купить за " + CartHelper.finalSumOrder()+ " грн.");

                ShopBaskEquipAdapter shopBaskEquipAdapter = new ShopBaskEquipAdapter(getContext(), editListQuantity);
                lvShopEquipBasket.setAdapter(shopBaskEquipAdapter);

                dialog.dismiss();
            }
        });
    }

    private void cartItemOnClick (View v){
        lvShopEquipBasket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
}
