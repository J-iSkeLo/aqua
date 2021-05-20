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

import java.util.ArrayList;

import l.chernenkiy.aqua.Fish.Items.Product;
import l.chernenkiy.aqua.Helpers.Support;
import l.chernenkiy.aqua.R;

public class FishListAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Product> mProductList;
    Support support = new Support();

    public FishListAdapter(Context context, ArrayList<Product> mProductList) {
        this.context = context;
        this.mProductList = mProductList;
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public Object getItem(int i) {
        return mProductList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"SetTextI18n", "InflateParams", "ViewHolder"})
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        Product product = mProductList.get(i);

            convertView = mInflater.inflate(R.layout.cart_fish_price_list, null);

            TextView tvName = convertView.findViewById(R.id.name_pn);
            TextView tvSize = convertView.findViewById(R.id.size_pn);
            TextView tvPrice = convertView.findViewById(R.id.price_pn);
            TextView tvVendorCode = convertView.findViewById(R.id.vendor_code_fish);
            ImageView image = convertView.findViewById(R.id.imageFish_pn);

            tvVendorCode.setText(product.getVendorCode());
            tvName.setText(product.getName());
            tvSize.setText(product.getSize() + " см.");
            tvPrice.setText(product.getPrice() + " грн.");

            String urlImage = product.getImage();
            support.loadImage(image, urlImage, context);


        return convertView;
    }
}
