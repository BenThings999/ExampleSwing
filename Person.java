import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class Person {
    private Seat seat;
    public GroupOfPeople group;

    private String personId;
    private String name;
    private Order order; // Each person can have an order
    public Person(String name) {
        this.name = name;
        this.order = null; // Initially, the person has no order
        this.personId = IdGenerator.generateUniqueId("P"); // Generate unique person ID
        this.group = null;
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
    public Seat getSeat() {
        return this.seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
    public GroupOfPeople getGroup() {
        return group;
    }

    public void setGroup(GroupOfPeople group) {
        this.group = group;
    }



}
