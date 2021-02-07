package l.chernenkiy.aqua.LastOrder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import l.chernenkiy.aqua.Equipment.EquipmentAccessActivity;
import l.chernenkiy.aqua.Helpers.Support;
import l.chernenkiy.aqua.R;
import l.chernenkiy.aqua.ShoppingBasket.AdapterEquip;

import static l.chernenkiy.aqua.MainActivity.lastEquipShopArray;

public class LastOrderEquipment extends Fragment {

    public ListView lvEquipCart;
    public AdapterEquip adapterEquip;
    Support support = new Support();

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.last_order_equipment, container, false);

        lvEquipCart = view.findViewById(R.id.lv_last_order_equip);
        TextView tvLastShop = view.findViewById(R.id.tv_last_shop);
        TextView tvCommonToCatalog = view.findViewById(R.id.tv_back_to_catalog_equip);

        adapterEquip = new AdapterEquip(getActivity(), lastEquipShopArray);
        lvEquipCart.setAdapter(adapterEquip);

        if(!lastEquipShopArray.isEmpty()){
            tvLastShop.setVisibility(View.INVISIBLE);
            tvCommonToCatalog.setVisibility(View.INVISIBLE);
        }

        tvCommonToCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), EquipmentAccessActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
