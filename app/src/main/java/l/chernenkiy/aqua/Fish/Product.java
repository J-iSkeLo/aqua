package l.chernenkiy.aqua.Fish;

public class Product {

    private int number;
    private String name;
    private String size;
    private String price;
    private String title;
    private String image;

    public Product(int number, String name, String size, String price, String title, String image) {

        this.number = number;
        this.name = name;
        this.size = size;
        this.price = price;
        this.title = title;
        this.image = image;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String image) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
