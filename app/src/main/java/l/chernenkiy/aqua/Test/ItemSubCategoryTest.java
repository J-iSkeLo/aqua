package l.chernenkiy.aqua.Test;

import java.io.Serializable;
import java.util.ArrayList;

import l.chernenkiy.aqua.Equipment.Items.ItemEquipment;

public class ItemSubCategoryTest implements Serializable {


    private final String name;
    private final ArrayList<ItemEquipmentTest> items;

    public ItemSubCategoryTest(String name, ArrayList<ItemEquipmentTest> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ItemEquipmentTest> getItems() {
        return items;
    }
}
