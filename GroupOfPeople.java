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


}
