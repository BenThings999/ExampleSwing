import java.util.Random;

class Dish {
    private String dishId;
    private String name;
    private double price;

    public Dish(String name, double price) {
        this.name = name;
        this.price = price;
        this.dishId = IdGenerator.generateUniqueId("DI"); // Generate unique dish ID
    }


    public String getDishId() {
        return dishId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "dishId='" + dishId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
