class Dish extends Item {
    public Dish(String name, double price) {
        super(name, price);
        setItemId(IdGenerator.generateUniqueId("DI")); // Generate unique item ID
    }
}