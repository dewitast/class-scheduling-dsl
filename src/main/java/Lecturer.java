import java.util.*;

public class Lecturer {
    public static String NAME = "name";
    public static String UNAVAILABILITY = "unavailability";
    public static String CLASSES = "classes";

    private String name;

    private Set<String> unavailability;

    private Set<String> classes;
    public Lecturer() {
        name = "";
        unavailability = new HashSet<>();
        classes = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getAvailability() {
        return unavailability;
    }

    public void setAvailability(Set<String> unavailability) {
        this.unavailability = unavailability;
    }

    public Set<String> getClasses() {
        return classes;
    }

    public void setClasses(Set<String> classes) {
        this.classes = classes;
    }

    public void addAvailability(String unavailability) {
        this.unavailability.add(unavailability);
    }
    public void addClass(String aClass) {
        this.unavailability.add(aClass);
    }

    public boolean check() {
        return (name.length() > 0);
    }

    public boolean addString(String key, String value) {
        if (key.equals(NAME)) {
            this.name = value;
        } else if (key.equals(UNAVAILABILITY)) {
            if (checkFormatAvailability(value)) {
                this.unavailability.add(value);
            }
        } else if (key.equals(CLASSES)) {
            classes.add(value);
        } else {
            System.out.println(value + "is not a lecturer's feature");
            return false;
        }
        return true;
    }

    private boolean checkFormatAvailability(String value) {
        if (value.length() != 3) {
            System.out.println("Lecturer unavailability must be 3 digit");
            return false;
        } else {
            if (value.charAt(0) < '1' || value.charAt(0) > '5') {
                System.out.println("First digit lecture unavailability must be in range 1-5");
                return false;
            }
            if (value.charAt(1) != '0' && value.charAt(1) != '1') {
                System.out.println("Second digit lecture unavailability must be 1 or 0");
                return false;
            }
            if (value.charAt(2) < '0' || value.charAt(2) > '9') {
                System.out.println("Lecturer unavailability must be a positive integer");
                return false;
            }
            return true;
        }
    }

    public void print() {
        System.out.println("Name: " + this.name);
        System.out.println("Unavailability: " + this.unavailability);
        System.out.println("Classes: " + this.classes);
    }
}
