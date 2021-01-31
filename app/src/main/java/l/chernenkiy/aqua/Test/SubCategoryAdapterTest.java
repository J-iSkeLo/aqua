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

import l.chernenkiy.aqua.R;

public class SubCategoryAdapterTest extends BaseAdapter {

    private Context mContext;
    private ArrayList<ItemSubCategoryTest> mItemCategory;


    public SubCategoryAdapterTest(Context mContext, ArrayList<ItemSubCategoryTest> mItemCategory) {
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
        convertView = mInflater.inflate(R.layout.cart_equipment_test, null);

        ImageView ivRightArrow = convertView.findViewById(R.id.imageRightArrow);
        ivRightArrow.setImageResource(R.drawable.right_arrow_equipment_list);

        TextView tvCategory = convertView.findViewById(R.id.name_category);
        TextView tvNumbEquip = convertView.findViewById(R.id.numb_category);

        ItemSubCategoryTest itemSubCategoryTest = mItemCategory.get(i);

        tvCategory.setText(itemSubCategoryTest.getName());
        tvNumbEquip.setText(itemSubCategoryTest.getItems().size() + " позиций");

        return convertView;
    }
}
