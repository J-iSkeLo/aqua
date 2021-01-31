package l.chernenkiy.aqua.Test;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemCategoryTest implements Serializable {

    private final String name;
    private final ArrayList<ItemEquipmentTest> items;
    private final ArrayList<ItemSubCategoryTest> itemSubCategoryTests;

    public ItemCategoryTest(String name, ArrayList<ItemEquipmentTest> items, ArrayList<ItemSubCategoryTest> itemSubCategoryTests) {
        this.name = name;
        this.items = items;
        this.itemSubCategoryTests = itemSubCategoryTests;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ItemEquipmentTest> getItems() {
        return items;
    }

    public ArrayList<ItemSubCategoryTest> getItemSubCategoryTests() {
        return itemSubCategoryTests;
    }

}