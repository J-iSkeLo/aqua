package l.chernenkiy.aqua.Helpers;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import l.chernenkiy.aqua.Equipment.Items.ItemCategory;
import l.chernenkiy.aqua.Equipment.Items.ItemEquipment;
import l.chernenkiy.aqua.Equipment.Items.ItemSubCategory;
import l.chernenkiy.aqua.Fish.Items.FishCategory;
import l.chernenkiy.aqua.Fish.Items.Product;


public class JsonRequest {

    public void makeFishRequest(RequestQueue mQueue, final ArrayList <FishCategory> resultFish) {

        String url = "https://aqua-m.kh.ua/api/v2/fish";

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {

                    try {
                        Iterator<String> keys = response.keys();

                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONArray allFish = response.getJSONArray(key);
                            ArrayList<Product> childItemFish = new ArrayList<>();

                            for (int i = 0; i < allFish.length(); i++) {
                                JSONObject fishItem = allFish.getJSONObject(i);

                                String vendorCode = fishItem.getString("article");
                                String name = fishItem.getString("name");
                                String size = fishItem.getString("size");
                                String price = fishItem.getString("price");
                                String image = fishItem.getString("image");

                                childItemFish.add(new Product(vendorCode, name, size, price, image));
                            }
                            resultFish.add(new FishCategory(key, childItemFish));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }
                }, Throwable::printStackTrace);
        mQueue.add(request);

    }


    public void makeAllEquipRequest(RequestQueue mQueue, final ArrayList<ItemCategory> arrayCategory,
                                    String url, final String generalColKey) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {

                    try {
                        Iterator<String> parentKeys = response.keys();

                        while (parentKeys.hasNext()) {
                            String categoryName = parentKeys.next();
                            Object allEquipment = response.get(categoryName);
                            ArrayList <ItemEquipment> resultChildrenCategory = new ArrayList<>();
                            ArrayList<ItemSubCategory> arraySubCategory = new ArrayList<>();

                            if (allEquipment instanceof  JSONObject){
                                JSONObject allObjectEquipment = (JSONObject) allEquipment;
                                Iterator<String> childKeys = allObjectEquipment.keys();


                                while (childKeys.hasNext()){
                                    String subCategoryName = childKeys.next();
                                    JSONArray allArraySubCategory = (JSONArray) allObjectEquipment.get(subCategoryName);
                                    ArrayList<ItemEquipment> itemEquipment = new ArrayList<>();

                                    for (int j = 0; j < allArraySubCategory.length(); j++){
                                        JSONObject subEquipItem = (JSONObject) allArraySubCategory.get(j);
                                        String article = subEquipItem.getString("article");
                                        String name = subEquipItem.getString("name");
                                        String description = subEquipItem.getString("description");
                                        String generalKey = subEquipItem.getString(generalColKey);
                                        String price = subEquipItem.getString("price");
                                        String image = subEquipItem.getString("image");

                                        itemEquipment.add(new ItemEquipment(article, name, description, generalKey,  price, image));
                                    }

                                    arraySubCategory.add(new ItemSubCategory(subCategoryName, itemEquipment));
                                }

                            } else {
                                JSONArray allArrayEquipment = (JSONArray) allEquipment;

                                for (int i = 0; i < allArrayEquipment.length(); i++) {
                                    JSONObject equipItem = allArrayEquipment.getJSONObject(i);

                                    String article = equipItem.getString("article");
                                    String name = equipItem.getString("name");
                                    String description = equipItem.getString("description");
                                    String generalKey = equipItem.getString(generalColKey);
                                    String price = equipItem.getString("price");
                                    String image = equipItem.getString("image");

                                    resultChildrenCategory.add(new ItemEquipment(article, name, description, generalKey,  price, image));
                                }
                            }
                            arrayCategory.add(new ItemCategory(categoryName, resultChildrenCategory, arraySubCategory));
                        }



                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }
                }, Throwable::printStackTrace);
        mQueue.add(request);
    }
}
