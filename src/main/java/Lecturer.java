import java.util.*;

public class Lecturer {
    public static String NAME = "name";
    public static String UNAVAILABILITY = "unavailability";
    public static String CLASSES = "classes";
    public static String PREFERENCES =  "preferences";

    private String name;
    private Set<String> unavailability;
    private Set<String> classes;

    private Set<String> preferences;

    public Lecturer() {
        name = "";
        unavailability = new HashSet<>();
        classes = new HashSet<>();
        preferences = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getUnavailability() {
        return unavailability;
    }

    public void setUnavailability(Set<String> unavailability) {
        this.unavailability = unavailability;
    }

    public Set<String> getClasses() {
        return classes;
    }

    public void setClasses(Set<String> classes) {
        this.classes = classes;
    }

    public Set<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(Set<String> preferences) {
        this.preferences = preferences;
    }

    public void addUnavailability(String unavailability) {
        this.unavailability.add(unavailability);
    }
    public void addClass(String aClass) {
        this.unavailability.add(aClass);
    }
    public void addPreference(String preference) {
        this.preferences.add(preference);
    }

    public boolean check() {
        return (name.length() > 0);
    }

    public boolean addString(String key, String value) {
        if (key.equals(NAME)) {
            this.name = value;
        } else if (key.equals(UNAVAILABILITY)) {
            if (checkTimeFormat(value)) {
                this.unavailability.add(value);
            } else return false;
        } else if (key.equals(CLASSES)) {
            classes.add(value);
        } else if (key.equals(PREFERENCES)){
            if (checkTimeFormat(value)) {
                preferences.add(value);
            } else return false;
        } else {
            System.out.println(value + "is not a lecturer's feature");
            return false;
        }
        return true;
    }

    private boolean checkTimeFormat(String value) {
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

    public boolean canTeachAt(int day, int hour) {
        for (String s : unavailability) {
            if (s.charAt(0)-'0' == day + 1 && (s.charAt(1)-'0')*10 + s.charAt(2)-'0' == hour + 1) {
                return false;
            }
        }
        return true;
    }

    public void print() {
        System.out.println("Name: " + this.name);
        System.out.println("Unavailability: " + this.unavailability);
        System.out.println("Classes: " + this.classes);
        System.out.println("Preferences: "+ this.preferences);
    }
}
