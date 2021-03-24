package l.chernenkiy.aqua;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;
import static l.chernenkiy.aqua.MainActivity.sharedPreferences;

public class MySettings  {

    public void loadFishShopBask(Context context){

        Gson gson = new Gson();

        SharedPreferences sharedPreferences = context.getSharedPreferences("shopping basket" , Context.MODE_PRIVATE);

        String loadFishBasket = sharedPreferences.getString("fish", null);

        Type type = new TypeToken<ArrayList<HashMap>>() {}.getType();

        cartItems = gson.fromJson(loadFishBasket, type);

        if (cartItems == null){
            cartItems = new ArrayList<>();
        }
    }

    public void loadEquipShopBask(Context context){

        Gson gson = new Gson();
        
        SharedPreferences sharedPreferences = context.getSharedPreferences("shopping basket" , Context.MODE_PRIVATE);

        String loadEquipBasket = sharedPreferences.getString("equip", null);

        Type type = new TypeToken<ArrayList<HashMap>>() {}.getType();

        cartEquipmentItem = gson.fromJson(loadEquipBasket, type);

        if (cartEquipmentItem == null){
            cartEquipmentItem = new ArrayList<>();
        }
    }

    public void saveFishShopBask() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String saveFishBasket = gson.toJson(cartItems);

        editor.putString("fish", saveFishBasket);
        editor.apply();
    }

    public void saveEquipShopBask() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String saveEquipBasket = gson.toJson(cartEquipmentItem);

        editor.putString("equip", saveEquipBasket);
        editor.apply();
    }



}
