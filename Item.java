
class Item {
    private String itemId;
    private String name;
    private double price;
    private int quantity;

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
        this.quantity = 1; // Default quantity is 1

    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    // Getters and setters for quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemId() {
        return itemId;
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
        return "Item{" +
                "itemId='" + itemId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}