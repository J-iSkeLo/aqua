package l.chernenkiy.aqua.Equipment;

import java.io.Serializable;

public class ItemEquipment implements Serializable {

    private String article;
    private String name;
    private String description;
    private String producer;
    private String price;
    private String image;



    public ItemEquipment(String article, String name, String description,
                         String producer, String price, String image) {
        this.article = article;
        this.name = name;
        this.description = description;
        this.producer = producer;
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

    public String getProducer() {
        return producer;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
