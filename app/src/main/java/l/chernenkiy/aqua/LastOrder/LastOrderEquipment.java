package l.chernenkiy.aqua.LastOrder;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import l.chernenkiy.aqua.R;
import l.chernenkiy.aqua.ShoppingBasket.AdapterEquip;

import static android.content.Context.MODE_PRIVATE;
import static l.chernenkiy.aqua.MainActivity.lastEquipShopArray;

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

        loadData();
        adapterEquip = new AdapterEquip(getActivity(), lastEquipShopArray);
        lvEquipCart.setAdapter(adapterEquip);

        if(!lastEquipShopArray.isEmpty()){
            TvLastShop.setVisibility(View.INVISIBLE);
            TvCommonToCatalog.setVisibility(View.INVISIBLE);
        }
        else{
            fabEquip.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    public void loadData(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared preferences equip", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("cartEquipmentItems", null);
        Type type = new TypeToken<ArrayList<HashMap>>() {}.getType();
        lastEquipShopArray = gson.fromJson(json, type);
        if(lastEquipShopArray == null){
            lastEquipShopArray = new ArrayList<>();
        }
    }
}
