import java.util.ArrayList;
import java.util.List;

class GroupOfPeople {

    private List<Person> people;

    public GroupOfPeople() {
        this.people = new ArrayList<>();

    }
    public void printNames() {
        for (Person person : people) {
            System.out.println(person.getName());
        }
    }
    public void addPerson(Person person) {
        people.add(person);
    }

    public List<Person> getPeople() {
        return people;
    }
    public int c() {
        int occupiedSeats = 0;
        for (Person person : people) {
            if (person.getSeat() != null && person.getSeat().isOccupied()) {
                occupiedSeats++;
            }
        }
        return occupiedSeats;
    }


    public int getSize() {
        return people.size();
    }
}
