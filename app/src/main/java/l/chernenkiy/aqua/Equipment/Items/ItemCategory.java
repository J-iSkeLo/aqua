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

        ArrayList<ItemEquipment> result = getItems();

        for (int j = 0; j < getItemSubCategories().size(); j++){

            ArrayList<ItemEquipment> childrenSubcategory = getItemSubCategories().get(j).getItems();

            result.addAll(childrenSubcategory);
        }
        return result;
    }
}