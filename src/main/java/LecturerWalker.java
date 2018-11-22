import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LecturerWalker extends SchedulingBaseListener{

    private List<Lecturer> lecturers;
    private Lecturer current;
    private boolean active;
    private String currentKey;

    public LecturerWalker() {
        lecturers = new ArrayList<>();
    }

    public List<Lecturer> getLecturers() {
        return lecturers;
    }

    @Override
    public void enterQuery(SchedulingParser.QueryContext ctx) {
        super.enterQuery(ctx);
        current = new Lecturer();
        active = true;
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
    public void enterString(SchedulingParser.StringContext ctx) {
        super.enterString(ctx);

        if (!active) {
            return;
        }

        String value = ctx.getText();

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

    }

    @Override
    public void exitQuery(SchedulingParser.QueryContext ctx) {
        super.exitQuery(ctx);
        if (current.check() && active) {
            lecturers.add(current);
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
            System.out.println("There are no lecturers with name " + name);
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
