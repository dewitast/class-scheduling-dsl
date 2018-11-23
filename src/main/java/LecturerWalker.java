import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LecturerWalker extends SchedulingBaseListener{

    private List<Lecturer> lecturers;
    private Lecturer current;
    private boolean active;
    private String currentKey;
    private String currentFunction;
    private String currentTargetKey;
    private String currentUpdateKey;

    public LecturerWalker() {
        lecturers = new ArrayList<>();
    }

    public List<Lecturer> getLecturers() {
        return lecturers;
    }

    @Override
    public void enterQuery(SchedulingParser.QueryContext ctx) {
        super.enterQuery(ctx);
        active = true;
        currentTargetKey = "";
        currentUpdateKey = "";
    }

    @Override
    public void enterFunction(SchedulingParser.FunctionContext ctx) {
        super.enterFunction(ctx);

        if (!active) {
            return;
        }
        currentFunction = ctx.getText();
        if(currentFunction.equals("show")) {
            print();
        } else if (currentFunction.equals("create")) {
            current = new Lecturer();
        }
    }

    @Override
    public void enterFeature(SchedulingParser.FeatureContext ctx) {
        super.enterFeature(ctx);

        if (!active) {
            return;
        }

        currentKey = ctx.SINGLE_STRING().getText();
    }

    @Override
    public void enterTarget_key(SchedulingParser.Target_keyContext ctx) {
        super.enterTarget_key(ctx);
        currentTargetKey = ctx.getText();
    }

    @Override
    public void enterUpdate_key(SchedulingParser.Update_keyContext ctx) {
        super.enterUpdate_key(ctx);
        currentUpdateKey = ctx.getText();
    }

    @Override
    public void enterString(SchedulingParser.StringContext ctx) {
        super.enterString(ctx);

        if (!active) {
            return;
        }

        String value = ctx.getText();

        if (currentFunction.equals("create")) {
            if (currentKey.equals(Lecturer.NAME)) {
                for (Lecturer l : lecturers) {
                    if (l.getName().equals(value)) {
                        active = false;
                        System.out.println("Lecturer name is already used");
                        return;
                    }
                }
            }
            if (!current.addString(currentKey, value)) {
                active = false;
            }
        } else if (currentFunction.equals("update")) {
            if (!currentTargetKey.equals("")) {
                boolean hasClass = false;
                if (currentKey.equals(Lecturer.UNAVAILABILITY)) {
                    if (currentUpdateKey.equals("remove")) {
                        String deletedUnavailability = "";
                        for (Lecturer l : lecturers) {
                            if (l.getName().equals(currentTargetKey)) {
                                //lecturer name is primary key, only found exactly 1
                                for (String u : l.getUnavailability()) {
                                    if (value.equals(u)) {
                                        deletedUnavailability = u;
                                    }
                                }
                                if (deletedUnavailability.equals("")) {
                                    System.out.println("Lecturer " + currentTargetKey + " has no unavailability " + value);
                                } else {
                                    l.getUnavailability().remove(deletedUnavailability);
                                    System.out.println("Unavailability " + deletedUnavailability + " from lecturer " + currentTargetKey + " has been deleted");
                                }
                                hasClass = true;
                            }
                        }
                        if (!hasClass) {
                            System.out.println("There's no lecturer with name " + currentTargetKey);
                        }
                    }
                } else if (currentKey.equals(Lecturer.CLASSES)) {
                    if (currentUpdateKey.equals("add")) {
                        for (Lecturer l : lecturers) {
                            if (l.getName().equals(currentTargetKey)) {
                                l.addString(currentKey, value);
                                System.out.println("lecturer's classes has been updated");
                                hasClass = true;
                            }
                        }
                        if (!hasClass) {
                            System.out.println("There's no lecturer with name " + currentTargetKey);
                        }
                    } else if (currentUpdateKey.equals("remove")) {
                        String deletedClass = "";
                        for (Lecturer l : lecturers) {
                            if (l.getName().equals(currentTargetKey)) {
                                //lecturer name is primary key, only found exactly 1
                                for (String c : l.getClasses()) {
                                    if (value.equals(c)) {
                                        deletedClass = c;
                                    }
                                }
                                if (deletedClass.equals("")) {
                                    System.out.println("Lecturer " + currentTargetKey + " has no class " + value);
                                } else {
                                    l.getClasses().remove(deletedClass);
                                    System.out.println("Class " + deletedClass + " from lecturer " + currentTargetKey + " has been deleted");
                                }
                                hasClass = true;
                            }
                        }
                        if (!hasClass) {
                            System.out.println("There's no lecturer with name " + currentTargetKey);
                        }
                    }
                } else if (currentKey.equals(Lecturer.PREFERENCES)) {
                    if (currentUpdateKey.equals("add")) {
                        for (Lecturer l : lecturers) {
                            if (l.getName().equals(currentTargetKey)) {
                                l.addString(currentKey, value);
                                System.out.println("lecturer's preferences has been updated");
                                hasClass = true;
                            }
                        }
                        if (!hasClass) {
                            System.out.println("There's no lecturer with name " + currentTargetKey);
                        }
                    } else if (currentUpdateKey.equals("remove")) {
                        String deletedPreference = "";
                        for (Lecturer l : lecturers) {
                            if (l.getName().equals(currentTargetKey)) {
                                //lecturer name is primary key, only found exactly 1
                                for (String p : l.getPreferences()) {
                                    if (value.equals(p)) {
                                        deletedPreference = p;
                                    }
                                }
                                if (deletedPreference.equals("")) {
                                    System.out.println("Lecturer " + currentTargetKey + " has no preference " + value);
                                } else {
                                    l.getPreferences().remove(deletedPreference);
                                    System.out.println("Preference " + deletedPreference + " from lecturer " + currentTargetKey + " has been deleted");
                                }
                                hasClass = true;
                            }
                        }
                        if (!hasClass) {
                            System.out.println("There's no lecturer with name " + currentTargetKey);
                        }
                    }
                } else {
                    System.out.println(currentKey + " cannot be updated");
                }
            } else {
                System.out.println("Class has no id");
            }
        } else {
            System.out.println(currentFunction + "is not a lecturer's function");
        }
    }

    @Override
    public void exitQuery(SchedulingParser.QueryContext ctx) {
        super.exitQuery(ctx);
        if (currentFunction.equals("create")) {
            if (current.check() && active) {
                lecturers.add(current);
            }
        } else if (currentFunction.equals("delete")) {
            Lecturer deletedLecturer = new Lecturer();
            for (Lecturer l : lecturers) {
                if (l.getName().equals(currentTargetKey)) {
                    //lecturer name is primary key, only found exactly 1
                    deletedLecturer = l;
                }
            }
            if (deletedLecturer.getName().equals("")) {
                System.out.println("There are no lecturers with name " + currentTargetKey);
            } else {
                lecturers.remove(deletedLecturer);
                System.out.println("lecturer " + deletedLecturer.getName() + " has been deleted");
            }
        }
        current = null;
        currentKey = "";
    }

    @Override
    public void enterLecturer(SchedulingParser.LecturerContext ctx) {
        super.enterLecturer(ctx);

        currentKey = "";
        String name = ctx.getText();
        for (Lecturer l : lecturers) {
            if (l.getName().equals(name)) {
                currentKey = name;
                break;
            }
        }
        if (currentKey.length() == 0) {
            System.out.println("There are no lecturer with name " + name);
        }
    }

    @Override
    public void enterUnavailability(SchedulingParser.UnavailabilityContext ctx) {
        super.enterUnavailability(ctx);

        if (currentKey.length() == 0) {
            return;
        }

        List<String> unavailability = asArray(ctx.getText());
        for (Lecturer l : lecturers) {
            if (l.getName().equals(currentKey)) {
                for (String s : unavailability) {
                    if (l.checkTimeFormat(s)) {
                        l.addUnavailability(s);
                    }
                }
            }
        }
    }

    @Override
    public void enterPreferences(SchedulingParser.PreferencesContext ctx) {
        super.enterPreferences(ctx);

        if (currentKey.length() == 0) {
            return;
        }

        List<String> preferences = asArray(ctx.getText());
        for (Lecturer l : lecturers) {
            if (l.getName().equals(currentKey)) {
                for (String s : preferences) {
                    if (l.checkTimeFormat(s)) {
                        l.addPreference(s);
                    }
                }
            }
        }
    }

    private List<String> asArray(String s) {
        if (s.charAt(0) == '[' && s.charAt(s.length()-1) == ']') {
            // Array is empty
            if (s.length() <= 2) {
                return new ArrayList<>();
            }
            s = s.substring(1, s.length() - 1);
            return new ArrayList<>(Arrays.asList(s.split(",")));
        } else {
            ArrayList<String> arr = new ArrayList<>();
            arr.add(s);
            return arr;
        }
    }

    public void print() {
        for (Lecturer l:lecturers) {
            l.print();
        }
    }
}
