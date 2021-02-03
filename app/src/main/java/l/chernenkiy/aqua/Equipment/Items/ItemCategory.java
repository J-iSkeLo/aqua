package l.chernenkiy.aqua.Equipment.Items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

public class ItemCategory implements Serializable {

    private final String name;
    private final ArrayList<ItemEquipment> items;
    private final ArrayList<ItemSubCategory> itemSubCategories;

    public ItemCategory(String name, ArrayList<ItemEquipment> items, ArrayList<ItemSubCategory> itemSubCategories) {
        this.name = name;
        this.items = items;
        this.itemSubCategories = itemSubCategories;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ItemEquipment> getItems() {
        return items;
    }

    public ArrayList<ItemSubCategory> getItemSubCategories() {
        return itemSubCategories;
    }

    public ArrayList<ItemEquipment> getAllItems () {
        ArrayList<ItemEquipment> items = getItems();
        ArrayList<ItemEquipment> result = new ArrayList<>(items);
        ArrayList<ItemSubCategory> subCategoryArrayList = getItemSubCategories();

        for (int j = 0; j < subCategoryArrayList.size(); j++){
            ArrayList<ItemEquipment> childrenSubcategory = subCategoryArrayList.get(j).getItems();
            result.addAll(childrenSubcategory);
        }

        return result;
    }

    public ArrayList<ItemEquipment> getSubCategoryItems() {
        ArrayList<ItemSubCategory> itemSubCategories = getItemSubCategories();
        ArrayList<ItemEquipment> result = new ArrayList<>();

        if (!itemSubCategories.isEmpty()) {
            for (int i = 0; i < itemSubCategories.size(); i++) {
                result.addAll(itemSubCategories.get(i).getItems());
            }
        }

        return result;
    }
}