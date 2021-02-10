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

import l.chernenkiy.aqua.Fish.Fish;
import l.chernenkiy.aqua.R;
import l.chernenkiy.aqua.ShoppingBasket.AdapterFish;

import static l.chernenkiy.aqua.MainActivity.lastFishShopArray;

public class LastOrderFish extends Fragment {

    public ListView lvLoadCart;
    public AdapterFish adapterFish;

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.last_order_fish, container, false);

        lvLoadCart = view.findViewById(R.id.myListCart);
        TextView tvLastShop = view.findViewById(R.id.txt_last_shop);
        TextView tvCommonToCatalog = view.findViewById(R.id.txt_common_to_catalog);

        adapterFish = new AdapterFish(getActivity(), lastFishShopArray);
        lvLoadCart.setAdapter(adapterFish);

        if(!lastFishShopArray.isEmpty()){
            tvLastShop.setVisibility(View.INVISIBLE);
            tvCommonToCatalog.setVisibility(View.INVISIBLE);
        }

        tvCommonToCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), Fish.class);
                startActivity(intent);
            }
        });


        return view;
    }
}
