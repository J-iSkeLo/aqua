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

import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Order.Order;
import l.chernenkiy.aqua.R;
import l.chernenkiy.aqua.ShoppingBasket.AdapterFish;

import static android.content.Context.MODE_PRIVATE;
import static l.chernenkiy.aqua.MainActivity.cartItems;

public class LastOrderFish extends Fragment {

    public ListView lvLoadCart;
    public AdapterFish adapterFish;
    public FloatingActionButton fab;
    private boolean flag;

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.last_order_fish, container, false);

        lvLoadCart = view.findViewById(R.id.myListCart);
        TextView TvLastShop = view.findViewById(R.id.txt_last_shop);
        TextView TvCommonToCatalog = view.findViewById(R.id.txt_common_to_catalog);
        fab = view.findViewById(R.id.floatingActionButton);

        loadData();

        adapterFish = new AdapterFish(getActivity(), cartItems);
        lvLoadCart.setAdapter(adapterFish);

        if(!cartItems.isEmpty()){
            TvLastShop.setVisibility(View.INVISIBLE);
            TvCommonToCatalog.setVisibility(View.INVISIBLE);
        }
        else{
            fab.setVisibility(View.INVISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Order.class);
                startActivity(intent);
            }
        });

        lvLoadCart.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE){
                    fab.animate().scaleX(1f).scaleY(1f).start();
                    flag = true;
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (flag){
                    fab.animate().scaleX(0f).scaleY(0f).start();
                    flag = false;
                }
            }
        });
        return view;
    }

    private void loadData(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared preferences fish", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("cartItems", null);
        Type type = new TypeToken<ArrayList<HashMap>>() {}.getType();
        cartItems = gson.fromJson(json, type);

        if(cartItems == null){
            cartItems = new ArrayList<>();
        }
    }

}
