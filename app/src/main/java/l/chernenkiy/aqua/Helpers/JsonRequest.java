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

import l.chernenkiy.aqua.Equipment.ItemCategory;
import l.chernenkiy.aqua.Equipment.ItemEquipment;

public class JsonRequest  {



    public ArrayList JsonRequest(String url, RequestQueue mQueue) {

        url = "https://aqua-m.kh.ua/api/equipment";
        final ArrayList resultEquip = new ArrayList<>();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Iterator<String> keys = response.keys();


                            while (keys.hasNext()) {
                                String category = keys.next();
                                JSONArray allEquipment = response.getJSONArray(category);
                                ArrayList resultChildrenCategory = new ArrayList<ItemEquipment>();

                                for (int i = 0; i < allEquipment.length(); i++) {
                                    JSONObject equipItem = allEquipment.getJSONObject(i);

                                    String article = equipItem.getString("article");
                                    String name = equipItem.getString("name");
                                    String description = equipItem.getString("description");
                                    String producer = equipItem.getString("producer");
                                    String price = equipItem.getString("price");
                                    String image = equipItem.getString("image");

                                    resultChildrenCategory.add(new ItemEquipment(article, name, description, producer,  price, image));
                                }

                                resultEquip.add(new ItemCategory (category, resultChildrenCategory));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });

        mQueue.add(request);
        return resultEquip;

    }

}
