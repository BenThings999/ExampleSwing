import java.util.Random;

class Drink {
    private String drinkId;
    private String name;
    private double price;
    private boolean alcoholic;

    public Drink(String name, double price, boolean alcoholic) {
        this.name = name;
        this.price = price;
        this.alcoholic = alcoholic;
        this.drinkId = IdGenerator.generateUniqueId("DR"); // Generate unique Drink ID

    }


    public String getDrinkId() {
        return drinkId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAlcoholic() {
        return alcoholic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAlcoholic(boolean alcoholic) {
        this.alcoholic = alcoholic;
    }

    @Override
    public String toString() {
        return "Drink{" +
                "drinkId='" + drinkId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", alcoholic=" + alcoholic +
                '}';
    }
}
