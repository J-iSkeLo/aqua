package l.chernenkiy.aqua.Test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ortiz.touchview.TouchImageView;

import java.util.ArrayList;
import java.util.HashMap;

import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.ConnectionDetector;
import l.chernenkiy.aqua.R;

import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.nextItemsSubCategory;

public class SubCategoryActivity extends AppCompatActivity {

    ListView lvSubCategory;
    EquipmentListAdapterTest equipmentListAdapterTest;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategoty);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.ConnectingToInternet();
        lvSubCategory = findViewById(R.id.list_equip_subcategory);

        System.out.println(nextItemsSubCategory);
        System.out.println(findViewById(R.id.list_equip_subcategory));

        if (nextItemsSubCategory != null){
            ArrayList<ItemEquipmentTest> nextItemsSubCategoryItems = nextItemsSubCategory.getItems();

            equipmentListAdapterTest = new EquipmentListAdapterTest(getApplicationContext(), nextItemsSubCategoryItems);
            lvSubCategory.setAdapter(equipmentListAdapterTest);
            equipmentListAdapterTest.notifyDataSetChanged();

            lvOnItemClickListener(nextItemsSubCategoryItems);
        }

    }

    public void lvOnItemClickListener(final ArrayList<ItemEquipmentTest> itemEquipmentTestCategory){
        lvSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Dialog dialog = new Dialog(SubCategoryActivity.this, R.style.FullHeightDialog);
                dialog.setContentView(R.layout.dialog_equip_set);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final ItemEquipmentTest items = itemEquipmentTestCategory.get(i);

                TextView nameEquip = dialog.findViewById(R.id.name_dialog_equip);
                TextView articleEquip = dialog.findViewById(R.id.article_dialog);
                TextView producerEquip = dialog.findViewById(R.id.dialog_producer_equip);
                TextView priceEquip = dialog.findViewById(R.id.priceEquip_dialog);
                TextView descriptionEquip = dialog.findViewById(R.id.dialog_description_equip);
                TouchImageView touchImageView = dialog.findViewById(R.id.imageTouchEquip);

                if(isInternetPresent) {
                    loadImage(touchImageView, items.getImage());
                }
                else{
                    showToastInternetPresent("Нет интернет соединения для загрузки изображения!");
                }

                final TextView quantity = dialog.findViewById(R.id.quantity_equip_dialog);

                nameEquip.setText(items.getName());
                articleEquip.setText("Артикул: " + items.getArticle());
                producerEquip.setText(items.getGeneralColKey ());
                priceEquip.setText(items.getPrice() + " грн.");
                descriptionEquip.setText(items.getDescription());


                Button btnCancelDialog = dialog.findViewById(R.id.cancel_dialogEquip_btn);
                Button btnAddShopBask = dialog.findViewById(R.id.addShopBaskEquip_btn);
                ImageButton btnCloseDialog = dialog.findViewById(R.id.btn_close_equip_dialog);

                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


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

                        if (quantity.length () < 1) {
                            showToastInternetPresent ("Укажите количество");
                            return;
                        }

                        HashMap<String, String> singleEquipItem = new HashMap<>();
                        singleEquipItem.put("name", items.getName());
                        singleEquipItem.put("article", items.getArticle());
                        singleEquipItem.put("producer", items.getGeneralColKey ());
                        singleEquipItem.put("price", items.getPrice());
                        singleEquipItem.put("description", items.getDescription());
                        singleEquipItem.put("quantity", quantityEquip);
                        singleEquipItem.put ("image" , items.getImage ());

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
                            cartEquipmentItem.add(singleEquipItem);
                            CartHelper.calculateItemsCart();
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
            }
        });
    }
    public void loadImage (TouchImageView touchImageView, String imageURL){

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(60f);
        circularProgressDrawable.setColorSchemeColors(Color.rgb (155,155,155));
        circularProgressDrawable.start();



        Glide.with(this)
                .load(imageURL)
                .placeholder(circularProgressDrawable)
                .into(touchImageView);
    }
    private void showToastInternetPresent(String msg) {
        Toast toast = Toast.makeText
                (getApplicationContext(),msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}