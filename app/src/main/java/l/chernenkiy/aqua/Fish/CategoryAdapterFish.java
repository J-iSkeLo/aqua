package l.chernenkiy.aqua.Fish;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import l.chernenkiy.aqua.Fish.Items.FishCategory;
import l.chernenkiy.aqua.R;

public class CategoryAdapterFish extends BaseAdapter {

    private final Context mContext;
    private final List<FishCategory> mItemCategory;



    public CategoryAdapterFish(Context mContext, List<FishCategory> mItemCategory) {
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

    @SuppressLint({"SetTextI18n", "ViewHolder", "InflateParams"})
    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.cart_category, null);

        ImageView ivRightArrow = convertView.findViewById(R.id.imageRightArrow);
        ivRightArrow.setImageResource(R.drawable.right_arrow_equipment_list);

        TextView tvCategory = convertView.findViewById(R.id.name_category);
        TextView tvNumbEquip = convertView.findViewById(R.id.numb_category);

        FishCategory item = mItemCategory.get(i);

        tvCategory.setText(item.getName());
        tvNumbEquip.setText(item.getItems().size() + " позиций");

        return convertView;
    }
}
