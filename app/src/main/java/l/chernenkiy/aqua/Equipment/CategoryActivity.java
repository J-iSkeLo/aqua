package l.chernenkiy.aqua.Equipment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.ortiz.touchview.TouchImageView;
import java.util.HashMap;
import java.util.Map;

import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;

import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.ConnectionDetector;
import l.chernenkiy.aqua.R;

public class CategoryActivity extends AppCompatActivity {
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    ListView lvCategory;
    EquipmentListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_category);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.ConnectingToInternet();

        ItemCategory itemCategory = (ItemCategory) getIntent().getSerializableExtra("position");

        toolbar(itemCategory);

        adapter = new EquipmentListAdapter(getApplicationContext(), itemCategory.getItems());
        lvCategory = findViewById(R.id.list_equip2);
        lvCategory.setAdapter(adapter);



        lvOnItemCliclListener(itemCategory);


    }

    public void lvOnItemCliclListener(final ItemCategory itemCategory){
        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Dialog dialog = new Dialog(CategoryActivity.this, R.style.FullHeightDialog);
                dialog.setContentView(R.layout.dialog_item_equip_set);

                final ItemEquipment item = itemCategory.getItems().get(i);

                TextView nameEquip = dialog.findViewById(R.id.name_dialog_equip);
                TextView articleEquip = dialog.findViewById(R.id.article_dialog);
                TextView producerEquip = dialog.findViewById(R.id.dialog_producer_equip);
                TextView priceEquip = dialog.findViewById(R.id.priceEquip_dialog);
                TextView descriptionEquip = dialog.findViewById(R.id.dialog_description_equip);
                TouchImageView touchImageView = dialog.findViewById(R.id.imageTouchEquip);

                final TextView quantity = dialog.findViewById(R.id.quantity_equip_dialog);

                nameEquip.setText(item.getName());
                articleEquip.setText("Артикул: " + item.getArticle());
                producerEquip.setText(item.getProducer());
                priceEquip.setText(item.getPrice() + " грн.");
                descriptionEquip.setText(item.getDescription());

                Button btnCancelDialog = dialog.findViewById(R.id.cancel_dialogEquip_btn);
                Button btnAddShopBask = dialog.findViewById(R.id.addShopBaskEquip_btn);


                btnCancelDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnAddShopBask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String quantityEquip = quantity.getText().toString();
                        if (quantityEquip.isEmpty() || Integer.parseInt(quantityEquip) < 1) {
                            return;
                        }

                        Map <String, String> singleEquipItem = new HashMap<>();
                        singleEquipItem.put("name", item.getName());
                        singleEquipItem.put("article", item.getArticle());
                        singleEquipItem.put("producer", item.getProducer());
                        singleEquipItem.put("price", item.getPrice());
                        singleEquipItem.put("description", item.getDescription());
                        singleEquipItem.put("quantity", quantityEquip);

                        boolean hasDuplicate = CartHelper.findCartItem(singleEquipItem.get("name"),singleEquipItem.get("price"), cartEquipmentItem);

                        if (hasDuplicate)
                        {
                            Toast toast = Toast.makeText
                                    (getApplicationContext(),"Позиция уже в Корзине",Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }
                        else
                            {
                                cartEquipmentItem.add((HashMap) singleEquipItem);
                                CartHelper.calculateItemsCart();
                                dialog.dismiss();
                            }
                    }
                });

                dialog.show();
            }
        });
    }

    private void toolbar(ItemCategory itemCategory){

        Toolbar toolbar = findViewById(R.id.toolbarEquipCategory2);
        setSupportActionBar(toolbar);
        try {
            toolbar.setTitle(itemCategory.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showToastInternetPresent(String msg) {
        Toast toast = Toast.makeText
                (getApplicationContext(),msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        new Intent(CategoryActivity.this, Equipment_accessories_Activity.class);
        finish();
    }
}
