package l.chernenkiy.aqua.Equipment.Items;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemCategory implements Serializable {

    private String name;
    private ArrayList<ItemEquipment> items;

    public ItemCategory(String name, ArrayList<ItemEquipment> items) {
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
