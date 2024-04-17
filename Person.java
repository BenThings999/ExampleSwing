import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class Person {
    private String personId;
    private String name;
    private Order order; // Each person can have an order
    public Person(String name) {
        this.name = name;
        this.order = null; // Initially, the person has no order
        this.personId = IdGenerator.generateUniqueId("P"); // Generate unique person ID

    }

    public String getName() {
        return name;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }





}
