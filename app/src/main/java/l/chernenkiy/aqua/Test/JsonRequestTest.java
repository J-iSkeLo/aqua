package l.chernenkiy.aqua.Test;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import l.chernenkiy.aqua.Equipment.Items.ItemCategory;
import l.chernenkiy.aqua.Equipment.Items.ItemEquipment;
import l.chernenkiy.aqua.Fish.Product;

import static java.nio.file.Paths.get;

public class JsonRequestTest {


    public ArrayList testMakeAllEquipRequest(RequestQueue mQueue, final ArrayList<ItemCategoryTest> arrayCategory,
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
                                ArrayList <ItemEquipmentTest> resultChildrenCategory = new ArrayList<>();
                                ArrayList<ItemSubCategoryTest> arraySubCategory = new ArrayList<>();

                                if (allEquipment instanceof  JSONObject){
                                    JSONObject allObjectEquipment = (JSONObject) allEquipment;
                                    Iterator<String> childKeys = allObjectEquipment.keys();


                                    while (childKeys.hasNext()){
                                        String subCategoryName = childKeys.next();
                                        JSONArray allArraySubCategory = (JSONArray) allObjectEquipment.get(subCategoryName);
                                        ArrayList<ItemEquipmentTest> itemEquipmentTest = new ArrayList<>();

                                        for (int j = 0; j < allArraySubCategory.length(); j++){
                                            JSONObject subEquipItem = (JSONObject) allArraySubCategory.get(j);
                                            String article = subEquipItem.getString("article");
                                            String name = subEquipItem.getString("name");
                                            String description = subEquipItem.getString("description");
                                            String generalKey = subEquipItem.getString(generalColKey);
                                            String price = subEquipItem.getString("price");
                                            String image = subEquipItem.getString("image");

                                            itemEquipmentTest.add(new ItemEquipmentTest(article, name, description, generalKey,  price, image));
                                        }

                                        arraySubCategory.add(new ItemSubCategoryTest(subCategoryName, itemEquipmentTest));
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

                                        resultChildrenCategory.add(new ItemEquipmentTest(article, name, description, generalKey,  price, image));
                                    }
                                }
                                arrayCategory.add(new ItemCategoryTest(categoryName, resultChildrenCategory, arraySubCategory));
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
