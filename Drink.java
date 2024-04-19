class Drink extends Item {
    private boolean alcoholic;

    public Drink(String name, double price, boolean alcoholic) {
        super(name, price);
        setItemId(IdGenerator.generateUniqueId("DR")); // Generate unique item ID
        this.alcoholic = alcoholic;
    }

    public boolean isAlcoholic() {
        return alcoholic;
    }

    public void setAlcoholic(boolean alcoholic) {
        this.alcoholic = alcoholic;
    }
}