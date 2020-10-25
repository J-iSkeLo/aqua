package l.chernenkiy.aqua.Equipment;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import l.chernenkiy.aqua.R;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {

    private List<ItemEquipment> itemEquipmentsList;
    private Context context;

    public EquipmentAdapter(List<ItemEquipment> itemEquipmentsList, Context context) {
        this.itemEquipmentsList = itemEquipmentsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_equip_access, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemEquipment listItem = itemEquipmentsList.get(position);

        holder.category.setText(listItem.getCategory());
        holder.numbCategory.setText(itemEquipmentsList.size());

    }

    @Override
    public int getItemCount() {
        return itemEquipmentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView category;
        public TextView numbCategory;

        public ViewHolder(View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.name_category);
            numbCategory = itemView.findViewById(R.id.numb_category);
        }
    }
}
