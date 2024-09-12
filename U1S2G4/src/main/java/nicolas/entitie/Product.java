package nicolas.entitie;

import java.util.Random;

public class Product {
    private long id;
    private String name;
    private String category;
    private double price;

    public Product(String name, String category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.id = new Random().nextLong();
    }


    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", category=" + category + ", price=" + price + "]";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        if (this.id == id) this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
