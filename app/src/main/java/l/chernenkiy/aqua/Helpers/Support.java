package l.chernenkiy.aqua.Helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import l.chernenkiy.aqua.Equipment.Items.ItemCategory;
import l.chernenkiy.aqua.Equipment.Items.ItemEquipment;
import l.chernenkiy.aqua.ShoppingBasket.ShoppingBasket;

import static l.chernenkiy.aqua.MainActivity.lastClass;

public class Support {



    public void showToast(Context context, String msg) {
        Toast toast = Toast.makeText
                (context,msg,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public void loadImage (ImageView image, String imageURL, Context context){

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(60f);
        circularProgressDrawable.setColorSchemeColors(Color.rgb (155,155,155));
        circularProgressDrawable.start();

        Glide.with(context)
                .load(imageURL)
                .placeholder(circularProgressDrawable)
                .into(image);
    }

    public void sortListSizeItemCategory(final ArrayList<ItemCategory> arrayList) {
        Collections.sort(arrayList, new Comparator<ItemCategory>() {

            @Override
            public int compare(ItemCategory current, ItemCategory next) {

                int v1 = current.getItems().size();
                int v2 = next.getItems().size();

                int subCategoriesItemsCount = current.getSubCategoryItems().size();
                int nextSubCategoriesItemsCount = next.getSubCategoryItems().size();

                int x1 = (v1 + subCategoriesItemsCount);
                int x2 = (v2 + nextSubCategoriesItemsCount);

                return Integer.compare(x2, x1);
            }
        });
    }

    public void sortItemEquipmentAlphabetical(final ArrayList<ItemEquipment> arrayList) {
        Collections.sort(arrayList, new Comparator<ItemEquipment>() {
            @Override
            public int compare(ItemEquipment current, ItemEquipment next) {

                return current.getName().compareToIgnoreCase(next.getName());
            }
        });
    }



    public void openShopBask(ImageButton btn, final Context context, final Class backClass) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View actionView) {
                Intent intent = new Intent(context, ShoppingBasket.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra ("class", backClass);
                context.startActivity(intent);
            }
        });
    }

    public void search(SearchView searchView, final Context context, final Class backClass) {

        searchView.setQueryHint("Поиск позиции...");
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(false);
        searchView.setOnSearchClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (context, SearchActivity.class);
                lastClass = backClass;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra ("class", backClass);
                context.startActivity (intent);
            }
        });
    }
}
