package l.chernenkiy.aqua.LastOrder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import l.chernenkiy.aqua.MainActivity;
import l.chernenkiy.aqua.Order.Order;
import l.chernenkiy.aqua.R;
import l.chernenkiy.aqua.ShoppingBasket.AdapterEquip;
import l.chernenkiy.aqua.ShoppingBasket.AdapterFish;

import static android.content.Context.MODE_PRIVATE;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;

public class LastOrderEquipment extends Fragment {

    public ListView lvEquipCart;
    public AdapterEquip adapterEquip;
    public FloatingActionButton fabEquip;
    private boolean flag;

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.last_order_equipment, container, false);

        lvEquipCart = view.findViewById(R.id.lv_last_order_equip);
        TextView TvLastShop = view.findViewById(R.id.tv_last_shop);
        TextView TvCommonToCatalog = view.findViewById(R.id.tv_back_to_catalog_equip);
        fabEquip = view.findViewById(R.id.fab_equip);

        loadData();
        adapterEquip = new AdapterEquip(getActivity(), cartEquipmentItem);
        lvEquipCart.setAdapter(adapterEquip);

        if(!cartEquipmentItem.isEmpty()){
            TvLastShop.setVisibility(View.INVISIBLE);
            TvCommonToCatalog.setVisibility(View.INVISIBLE);
        }
        else{
            fabEquip.setVisibility(View.INVISIBLE);
        }

        fabEquip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Order.class);
                startActivity(intent);
            }
        });

        lvEquipCart.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE){
                    fabEquip.animate().scaleX(1f).scaleY(1f).start();
                    flag = true;
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (flag){
                    fabEquip.animate().scaleX(0f).scaleY(0f).start();
                    flag = false;
                }
            }
        });
        return view;
    }

    public void loadData(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared preferences equip", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("cartEquipmentItems", null);
        Type type = new TypeToken<ArrayList<HashMap>>() {}.getType();
        cartEquipmentItem = gson.fromJson(json, type);
        if(cartEquipmentItem == null){
            cartEquipmentItem = new ArrayList<>();
        }
    }
}
