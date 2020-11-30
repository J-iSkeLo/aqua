package l.chernenkiy.aqua.Equipment;

import java.io.Serializable;

public class ItemAquarium implements Serializable {

    private String article;
    private String name;
    private String description;
    private String capacity;
    private String price;
    private String image;



    public ItemAquarium(String article, String name, String description,
                        String capacity, String price, String image) {
        this.article = article;
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.price = price;
        this.image = image;
    }

    public String getArticle() {
        return article;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
            return description;

    }

    public String getCapacity() {
        return capacity;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
