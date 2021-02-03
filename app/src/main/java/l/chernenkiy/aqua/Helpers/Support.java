package l.chernenkiy.aqua.Helpers;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import l.chernenkiy.aqua.Equipment.Items.ItemCategory;
import l.chernenkiy.aqua.Equipment.Items.ItemSubCategory;

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

    public void sortListSize_ItemCategory(ArrayList<ItemCategory> arrayList) {
        Collections.sort(arrayList, new Comparator<ItemCategory>() {
            @Override
            public int compare(ItemCategory itemCategory, ItemCategory t1) {
                int v3 = 0;
                int v1 = itemCategory.getItems().size();
                int v2 = t1.getItems().size();
                ArrayList<ItemSubCategory> subCategoryArrayList = itemCategory.getItemSubCategories();
                if (!itemCategory.getItemSubCategories().isEmpty()){
                    for (int i = 0; i < itemCategory.getItemSubCategories().size(); i++){
                        v3 += subCategoryArrayList.get(i).getItems().size();
                    }
                    return Integer.compare(v3, v1);
                }
                return Integer.compare(v1, v2);
            }
        });
    }
}
