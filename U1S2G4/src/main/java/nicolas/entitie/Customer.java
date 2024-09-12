package nicolas.entitie;

import java.util.Random;

public class Customer {
    private long id;
    private String name;
    private int tier;

    public Customer(String name, int tier) {
        this.name = name;
        this.tier = tier;
        this.id = new Random().nextLong();
    }


    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + name + ", tier=" + tier + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public long getId() {
        return id;
    }
}