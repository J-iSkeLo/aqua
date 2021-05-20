package l.chernenkiy.aqua.Fish.Items;

public class Product {

    private String vendorCode;
    private String name;
    private String size;
    private final String price;
    private final String image;

    public Product(String vendorCode, String name, String size, String price, String image) {

        this.vendorCode = vendorCode;
        this.name = name;
        this.size = size;
        this.price = price;
        this.image = image;
    }

    public String getVendorCode(){
        return vendorCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    public String getImage() {
        return image;
    }

 }
