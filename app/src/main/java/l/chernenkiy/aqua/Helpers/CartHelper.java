package l.chernenkiy.aqua.Helpers;


import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartAddItemTextMain;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;

public class CartHelper {

    public static double countFinalSum(ArrayList<HashMap> arrayList){
        double result = 0;

        for (int i=0; i<arrayList.size(); i++){
          result += itemSum(arrayList.get(i));
        }
        return result;
    }

    public static double itemSum(HashMap<String, String> cartItems){
        Double quantity = convertDoublePoint(cartItems.get("quantity"));
        Double price = convertDoublePoint(cartItems.get("price"));
        return price * quantity;

    }

    public static double countFinalSumEquip(ArrayList<HashMap> arrayList){
        double result = 0;

        for (int i=0; i<arrayList.size(); i++){
            result += itemSumEquip(arrayList.get(i));
        }

        return result;
    }

    public static double itemSumEquip(HashMap<String, String> cartEquipmentItem){
        Double quantity = convertDoublePoint(cartEquipmentItem.get("quantity"));
        Double price = convertDoublePoint(cartEquipmentItem.get("price"));

        BigDecimal bigDecimal = new BigDecimal(quantity * price);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.DOWN);
        return bigDecimal.doubleValue ();

    }

    public static Double convertDoublePoint(String number){
        if (number.contains(",")){
            number = number.replace(",",".");
        }
        if (number.isEmpty()){
            number = "0";
        }
        return Double.valueOf(number);
    }

    public static BigDecimal finalSumOrder (){
        double finalSumOrder;

        double fish = CartHelper.countFinalSum(cartItems);
        double equipment = CartHelper.countFinalSumEquip(cartEquipmentItem);

        finalSumOrder = fish + equipment;

        BigDecimal bigDecimal = new BigDecimal(finalSumOrder);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.CEILING);

        return bigDecimal;
    }

    public static boolean findCartItem(String name, String price, ArrayList<HashMap> arrayList) {
        for (int i=0; i<arrayList.size(); i++) {
            if (arrayList.get(i).get("name").equals(name)&&arrayList.get(i).get("price").equals(price)) {
                return true;
            }

        }
        return false;
    }

    public static void calculateItemsCart() {
        if (!cartItems.isEmpty() || !cartEquipmentItem.isEmpty()){
            cartAddItemText.setVisibility(View.VISIBLE);
            cartAddItemText.setText(String.valueOf(cartItems.size() + cartEquipmentItem.size()));

        }else {
            cartAddItemText.setVisibility(View.INVISIBLE);
        }
    }

    public static void calculateItemsCartMain() {
        if (!cartItems.isEmpty() || !cartEquipmentItem.isEmpty()){
            cartAddItemTextMain.setVisibility(View.VISIBLE);
            cartAddItemTextMain.setText(String.valueOf(cartItems.size() + cartEquipmentItem.size()));

        }else {
            cartAddItemTextMain.setVisibility(View.INVISIBLE);
        }
    }



}
