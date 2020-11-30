package l.chernenkiy.aqua.Equipment;

import java.io.Serializable;

public class ItemFeed implements Serializable {

    private String article;
    private String name;
    private String description;
    private String weight;
    private String price;
    private String image;



    public ItemFeed(String article, String name, String description,
                    String weight, String price, String image) {
        this.article = article;
        this.name = name;
        this.description = description;
        this.weight = weight;
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

    public String getWeight() {
        return weight;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
