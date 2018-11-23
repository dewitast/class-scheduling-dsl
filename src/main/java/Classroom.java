import java.util.HashSet;
import java.util.Set;

public class Classroom {
    public static String NAME = "name";
    public static String CAPACITY = "capacity";
    public static String FACILITIES = "facilities";

    private String name;
    private int capacity;
    private Set<String> facilities;

    public Classroom() {
        name = "";
        capacity = 0;
        facilities = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Set<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(Set<String> facilities) {
        this.facilities = facilities;
    }

    public void addFacility(String facility) {
        this.facilities.add(facility);
    }

    public boolean check() {
        return (name.length() > 0);
    }

    public boolean addString(String key, String value) {
        if (key.equals(NAME)) {
            this.name = value;
        } else if (key.equals(CAPACITY)) {
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) < '0' || value.charAt(i) > '9') {
                    System.out.println("Classroom capacity must be a positive integer");
                    return false;
                }
            }
            this.capacity = Integer.parseInt(value);
        } else if (key.equals(FACILITIES)) {
            facilities.add(value);
        } else {
            System.out.println(value + "is not a classroom's feature");
            return false;
        }
        return true;
    }

    public void print() {
        System.out.println("Name: " + this.name);
        System.out.println("    Capacity: " + this.capacity);
        System.out.println("    Facilities: " + this.facilities);
    }
}
