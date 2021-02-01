package l.chernenkiy.aqua.Helpers;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import l.chernenkiy.aqua.Equipment.Items.ItemCategory;
import l.chernenkiy.aqua.Fish.Product;
import l.chernenkiy.aqua.Equipment.Items.ItemEquipment;
import l.chernenkiy.aqua.Equipment.Items.ItemSubCategory;

import static java.nio.file.Paths.get;

public class JsonRequest {

    public ArrayList makeFishRequest(RequestQueue mQueue, final ArrayList <Product> resultFish) {

        String url = "https://aqua-m.kh.ua/api/price-list";

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Iterator<String> keys = response.keys();

                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONArray allFish = response.getJSONArray(key);

                                resultFish.add(new Product ("", "", "", key, ""));

                                for (int i = 0; i < allFish.length(); i++) {
                                    JSONObject fishItem = allFish.getJSONObject(i);

                                    String name = fishItem.getString("name");
                                    String size = fishItem.getString("size");
                                    String price = fishItem.getString("price");
                                    String image = fishItem.getString("image");

                                    resultFish.add(new Product(name, size, price, "", image));

                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace ();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace ();
            }



        });
        mQueue.add(request);
        return resultFish;

    }


    public ArrayList makeAllEquipRequest(RequestQueue mQueue, final ArrayList<ItemCategory> arrayCategory,
                                         String url, final String generalColKey) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace ();
            }
        });
        mQueue.add(request);
        return arrayCategory;
    }
}
