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
import l.chernenkiy.aqua.Equipment.Items.ItemEquipment;
import l.chernenkiy.aqua.Fish.Product;

public class JsonRequest  {

    public final ArrayList resultFish = new ArrayList<>();
    public final ArrayList resultEquip = new ArrayList<>();
    public final ArrayList resultFeed = new ArrayList<>();
    public final ArrayList resultChemistry = new ArrayList<>();
    public final ArrayList resultAquariums = new ArrayList<>();


    public ArrayList makeFishRequest(RequestQueue mQueue) {

        String url = "https://aqua-m.kh.ua/api/price-list";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Iterator<String> keys = response.keys();

                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONArray allFish = response.getJSONArray(key);

                                resultFish.add(new Product (0, "", "", "", key, ""));

                                for (int i = 0; i < allFish.length(); i++) {
                                    JSONObject fishItem = allFish.getJSONObject(i);

                                    int number = fishItem.getInt("number");
                                    String name = fishItem.getString("name");
                                    String size = fishItem.getString("size");
                                    String price = fishItem.getString("price");
                                    String image = fishItem.getString("image");

                                    resultFish.add(new Product(number, name, size, price, "", image));
                                }
                            }


                        } catch (JSONException e) {
                            e.getMessage();

                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }
        });

        mQueue.add(request);
        return resultFish;

    }




    public ArrayList makeEquipRequest (RequestQueue mQueue) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiInfo.equipment, null,
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
                                    String generalKey = equipItem.getString(ApiInfo.equipmentGeneralColKey);
                                    String price = equipItem.getString("price");
                                    String image = equipItem.getString("image");


                                    resultChildrenCategory.add(new ItemEquipment(article, name, description, generalKey,  price, image));
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
                error.printStackTrace ();

            }

        });
        mQueue.add(request);
        return resultEquip;

    }

    public ArrayList makeFeedRequest (RequestQueue mQueue) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiInfo.feed, null,
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
                                    String generalKey = equipItem.getString(ApiInfo.feedGeneralColKey);
                                    String price = equipItem.getString("price");
                                    String image = equipItem.getString("image");


                                    resultChildrenCategory.add(new ItemEquipment(article, name, description, generalKey,  price, image));
                                }

                                resultFeed.add(new ItemCategory (category, resultChildrenCategory));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace ();

            }

        });
        mQueue.add(request);
        return resultFeed;

    }

    public ArrayList makeChemistryRequest (RequestQueue mQueue) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiInfo.chemistry, null,
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
                                    String generalKey = equipItem.getString(ApiInfo.chemistryGeneralColKey);
                                    String price = equipItem.getString("price");
                                    String image = equipItem.getString("image");


                                    resultChildrenCategory.add(new ItemEquipment(article, name, description, generalKey,  price, image));
                                }

                                resultChemistry.add(new ItemCategory (category, resultChildrenCategory));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace ();

            }

        });
        mQueue.add(request);
        return resultChemistry;

    }

    public ArrayList makeAquariumsRequest (RequestQueue mQueue) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiInfo.aquariums, null,
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
                                    String generalKey = equipItem.getString(ApiInfo.aquariumsGeneralColKey);
                                    String price = equipItem.getString("price");
                                    String image = equipItem.getString("image");


                                    resultChildrenCategory.add(new ItemEquipment(article, name, description, generalKey,  price, image));
                                }

                                resultAquariums.add(new ItemCategory (category, resultChildrenCategory));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace ();

            }

        });
        mQueue.add(request);
        return resultAquariums;

    }



}
