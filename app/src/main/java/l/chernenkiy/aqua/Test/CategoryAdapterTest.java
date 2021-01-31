package l.chernenkiy.aqua.Test;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import l.chernenkiy.aqua.Equipment.Items.ItemCategory;
import l.chernenkiy.aqua.R;

public class CategoryAdapterTest extends BaseAdapter {

    private Context mContext;
    private List<ItemCategoryTest> mItemCategory;


    public CategoryAdapterTest(Context mContext, List<ItemCategoryTest> mItemCategory) {
        this.mContext = mContext;
        this.mItemCategory = mItemCategory;

    }



    @Override
    public int getCount() {
        return mItemCategory.size();
    }

    @Override
    public Object getItem(int i) {
        return mItemCategory.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.cart_equipment, null);

        ImageView ivRightArrow = convertView.findViewById(R.id.imageRightArrow);
        ivRightArrow.setImageResource(R.drawable.right_arrow_equipment_list);


        TextView tvCategory = convertView.findViewById(R.id.name_category);
        TextView tvNumbEquip = convertView.findViewById(R.id.numb_category);



        tvCategory.setText(mItemCategory.get(i).getName());


        ArrayList<ItemSubCategoryTest> itemSubCategoryTests = mItemCategory.get(i).getItemSubCategoryTests();

        if (itemSubCategoryTests.isEmpty()) {
            tvNumbEquip.setText(mItemCategory.get(i).getItems().size() + " позиций");
        } else {
            int sizeSubCategoryItems = 0;
            for (int j = 0; j < itemSubCategoryTests.size(); j++){
                sizeSubCategoryItems += itemSubCategoryTests.get(j).getItems().size();
            }
        tvNumbEquip.setText(sizeSubCategoryItems + " позиций");
        }
        return convertView;
    }
}
