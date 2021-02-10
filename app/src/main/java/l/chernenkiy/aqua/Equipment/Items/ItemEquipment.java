package l.chernenkiy.aqua.Equipment.Items;

import java.io.Serializable;

public class ItemEquipment implements Serializable {

    private String article;
    private String name;
    private String description;
    private String generalColKey;
    private String price;
    private final String image;




    public ItemEquipment(String article, String name, String description,
                         String generalColKey, String price, String image) {
        this.article = article;
        this.name = name;
        this.description = description;
        this.generalColKey = generalColKey;
        this.price = price;
        this.image = image;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeneralColKey() {
        return generalColKey;
    }

    public void setGeneralColKey(String generalColKey) {
        this.generalColKey = generalColKey;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }


}
