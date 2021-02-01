package l.chernenkiy.aqua.Equipment.Items;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemSubCategory implements Serializable {


    private final String name;
    private final ArrayList<ItemEquipment> items;

    public ItemSubCategory(String name, ArrayList<ItemEquipment> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ItemEquipment> getItems() {
        return items;
    }
}
