import java.util.Optional;

/*
使用 Optional 为 null 提供轻量级代理，
以防止抛出 NullPointerException。
 */
class Person2 {
    public final Optional<String> first;
    public final Optional<String> last;
    public final Optional<String> address;
    public final Boolean empty;
    Person2(String first, String last, String address) {
        this.first = Optional.ofNullable(first);
        this.last = Optional.ofNullable(last);
        this.address = Optional.ofNullable(address);
        empty = !this.first.isPresent() && !this.last.isPresent() && !this.address.isPresent();
    }
    Person2(String first, String last) {
        this(first, last, null);
    }
    Person2(String last) {
        this(null, last, null);
    }
    Person2() {
        this(null, null, null);
    }

    @Override
    public String toString() {
        if (empty) {
            return "<Empty>";
        }
        return (first.orElse("") + "-"
                + last.orElse("") + "-"
                + address.orElse("")).trim();
    }

    public static void main(String[] args) {
        System.out.println(new Person2());
        System.out.println(new Person2("Smith"));
        System.out.println(new Person2("Bob", "Smith"));
        System.out.println(new Person2("Bob", "Smith", "11 Degree Lane, Frostbite Falls, MN"));
    }
}

class EmptyTitleException extends RuntimeException {}
class Position {
    private String title;
    private Person2 person;

    Position(String jobTitle, Person2 employee) {
        setTitle(jobTitle);
        setPerson(employee);
    }
    Position(String jobTitle) {
        this(jobTitle, null);
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String newTitle) {
        title = Optional.ofNullable(newTitle).orElseThrow(EmptyTitleException::new);
    }
    public Person2 getPerson() {
        return person;
    }
    public void setPerson(Person2 newPerson) {
        person = Optional.ofNullable(newPerson).orElse(new Person2());
    }

    @Override
    public String toString() {
        return "Position{" +
                "title='" + title + '\'' +
                ", person=" + person +
                '}';
    }

    public static void main(String[] args) {
        System.out.println(new Position("CEO"));
        System.out.println(new Position("CEO", new Person2("Arthur", "Fonzarelli")));
        try {
            new Position(null);
        } catch (Exception e) {
            System.out.println("caught: " + e);
        }
    }
}
