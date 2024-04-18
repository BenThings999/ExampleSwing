import java.util.ArrayList;
import java.util.List;

class GroupOfPeople {
    private List<Person> people;

    public GroupOfPeople() {
        this.people = new ArrayList<>();
    }

    public void addPerson(Person person) {
        people.add(person);
    }

    public List<Person> getPeople() {
        return people;
    }


}
