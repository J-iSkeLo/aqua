package l.chernenkiy.aqua.Helpers;


import android.view.View;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static l.chernenkiy.aqua.MainActivity.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartAddItemTextMain;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;

public class CartHelper {

    public static double countFinalSum(ArrayList<HashMap<String, String>> arrayList){
        double result = 0;

        for (int i=0; i<arrayList.size(); i++){
          result += itemSum(arrayList.get(i));
        }
        return result;
    }

    public static double itemSum(HashMap <String, String> hashMap){
        Double quantity = convertDoublePoint(Objects.requireNonNull(hashMap.get("quantity")));
        Double price = convertDoublePoint(Objects.requireNonNull(hashMap.get("price")));
        return price * quantity;

    }

    public static double countFinalSumEquip(ArrayList<HashMap<String, String> > arrayList){
        double result = 0;

        for (int i=0; i<arrayList.size(); i++){
            result += itemSumEquip(arrayList.get(i));
        }

        BigDecimal bigDecimal = new BigDecimal(result);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.DOWN);

        return bigDecimal.doubleValue ();
    }

    public static double itemSumEquip(HashMap<String, String> cartEquipmentItem){
        Double quantity = convertDoublePoint(Objects.requireNonNull(cartEquipmentItem.get("quantity")));
        Double price = convertDoublePoint(Objects.requireNonNull(cartEquipmentItem.get("price")));

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

    public static BigDecimal finalSumOrder (ArrayList<HashMap<String, String> > cartItems, ArrayList<HashMap<String, String> > cartEquipmentItem ){
        double finalSumOrder;

        double fish = CartHelper.countFinalSum(cartItems);
        double equipment = CartHelper.countFinalSumEquip(cartEquipmentItem);

        finalSumOrder = fish + equipment;

        BigDecimal bigDecimal = new BigDecimal(finalSumOrder);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.CEILING);

        return bigDecimal;
    }

    public static boolean findCartItem(String name, String price, ArrayList<HashMap<String, String> > arrayList) {
        for (int i=0; i<arrayList.size(); i++) {
            if (Objects.equals(arrayList.get(i).get("name"), name) && Objects.equals(arrayList.get(i).get("price"), price)) {
                return true;
            }

        }
        return false;
    }

    public static void calculateItemsCart() {
        if (!(cartAddItemText == null)) {
            if (cartItems.isEmpty() && cartEquipmentItem.isEmpty()) {
                cartAddItemText.setVisibility(View.INVISIBLE);
            } else {
                cartAddItemText.setVisibility(View.VISIBLE);
                cartAddItemText.setText(String.valueOf(cartItems.size() + cartEquipmentItem.size()));
            }
        }
    }

    public static void calculateItemsCartMain() {

        if (!cartItems.isEmpty() || !cartEquipmentItem.isEmpty()) {
            cartAddItemTextMain.setVisibility(View.VISIBLE);
            cartAddItemTextMain.setText(String.valueOf(cartItems.size() + cartEquipmentItem.size()));

        } else {
            cartAddItemTextMain.setVisibility(View.INVISIBLE);
        }

    }



}
