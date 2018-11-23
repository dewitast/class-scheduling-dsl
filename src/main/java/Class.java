import java.lang.reflect.Field;
import java.util.*;

public class Class {
    public static String ID = "id";
    public static String NAME = "name";
    public static String CODE = "code";
    public static String REQUIREMENTS = "requirements";
    public static String SIZE = "size";
    public static String NUMBER = "number";
    public static String GRADE = "grade";
    public static String CREDIT = "credit";
    public static String CLASHES = "clashes";
    public static String SCHEDULES = "schedules";
    public static Set<String> INTEGER_KEY = new HashSet<>(Arrays.asList(NUMBER, SIZE, GRADE, CREDIT));

    private String id;
    private String name;
    private String code;
    private Set<String> requirements;
    private int size;
    private int number;
    private int credit;
    private int grade;
    private Set<String> clashes; //Untuk kelas yang memang scr default ga boleh bareng (yaitu tingkatnya sama tapi code nya beda) ga usah disimpan disini
    private Set<String> schedules;

    public Class() {
        id = "";
        name = "";
        code = "";
        requirements = new HashSet<>();
        size = 0;
        number = 0;
        credit = 0;
        grade = 0;
        clashes = new HashSet<>();
        schedules = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setRequirements(Set<String> requirements) {
        this.requirements = requirements;
    }

    public void addRequirement(String requirement) {
        this.requirements.add(requirement);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setClashes(Set<String> clashes) {
        this.clashes = clashes;
    }

    public void addClash(String clash) {
        this.clashes.add(clash);
    }

    public void setSchedules(Set<String> schedules) {
        this.schedules = schedules;
    }

    public void addSchedule(String schedule) {
        this.schedules.add(schedule);
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Set<String> getRequirements() {
        return requirements;
    }

    public int getSize() {
        return size;
    }

    public int getNumber() {
        return number;
    }

    public int getCredit() {
        return credit;
    }

    public int getGrade() {
        return grade;
    }

    public Set<String> getClashes() {
        return clashes;
    }

    public Set<String> getSchedules() {
        return schedules;
    }

    public boolean check() {
        if (id.length()<=0){
            System.out.println("Class id must has min 1 character");
            return false;
        } else if (name.length()<=0){
            System.out.println("Class name must has min 1 character");
            return false;
        } else if (code.length()<=0){
            System.out.println("Class code must has min 1 character");
            return false;
        } else if (size<=0){
            System.out.println("Class size min 1 participants");
            return false;
        }  else if (number<=0){
            System.out.println(number);
            System.out.println("Class number must positive integer");
            return false;
        } else if (grade<=0){
            System.out.println("Class grade must be positive integer");
            return false;
        } else if (credit<=0){
            System.out.println("Class credit must be positive integer");
            return false;
        }
        return true;
    }

    public Boolean isPositiveInteger(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < '0' || s.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }

    public boolean addString(String key, String value) {
        if (key.equals(ID)) {
            this.id = value;
        } else if (key.equals(NAME)) {
            this.name = value;
        } else if (key.equals(CODE)) {
            this.code = value;
        } else if (key.equals(REQUIREMENTS)) {
            requirements.add(value);
        } else if (key.equals(CLASHES)) {
            clashes.add(value);
        } else if (key.equals(schedules)) {
            schedules.add(value);
        } else if (INTEGER_KEY.contains(key)) {
            if (isPositiveInteger(value)) {
                try {
                    Field field = getClass().getDeclaredField(key);
                    field.setInt(this, Integer.parseInt(value));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new AssertionError(e);
                }
            } else {
                System.out.println("Class " + key + " must be a positive integer");
            }
        } else {
            System.out.println("(" + key + "," + value + ")" + "is not a class's feature");
            return false;
        }
        this.id = this.code+'-'+this.number;
        return true;
    }

    public boolean addArray(String key, List<String> value) {
        if (key.equals(REQUIREMENTS)) {
            requirements.addAll(value);
        } else if (key.equals(CLASHES)) {
            clashes.addAll(value);
        } else if (key.equals(SCHEDULES)) {
            schedules.addAll(value);
        } else {
            System.out.println("Classroom feature and value does not match");
            return false;
        }
        return true;
    }

    public void print() {
        System.out.println("Id: " + this.id);
        System.out.println("    Name: " + this.name);
        System.out.println("    Code: " + this.code + " - Number: "+this.number+ " - Grade: "+this.grade);
        System.out.println("    Requirements: " + requirements);
        System.out.println("    Size: " + this.size+" - Credit: "+this.credit);
        System.out.println("    Clashes: " + this.clashes);
    }
}
