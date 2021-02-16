package l.chernenkiy.aqua.Fish.Items;

import java.io.Serializable;
import java.util.ArrayList;

public class FishCategory implements Serializable {

    private final String name;
    private final ArrayList<Product> items;


    public String getName() {
        return name;
    }

    public ArrayList<Product> getItems() {
        return items;
    }

    public FishCategory(String name, ArrayList<Product> items) {
        this.name = name;
        this.items = items;
    }
}
