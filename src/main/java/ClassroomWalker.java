import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassroomWalker extends SchedulingBaseListener {
    private int VALUE_STRING = 0;
    private int VALUE_ARRAY = 1;

    private List<Classroom> classrooms;
    private Classroom current;
    private boolean active;

    public ClassroomWalker() {
        classrooms = new ArrayList<>();
    }

    public List<Classroom> getClassrooms() {
        return classrooms;
    }

    @Override
    public void enterQuery(SchedulingParser.QueryContext ctx) {
        super.enterQuery(ctx);
        current = new Classroom();
        active = true;
    }

    @Override
    public void enterFeature(SchedulingParser.FeatureContext ctx) {
        super.enterFeature(ctx);

        if (!active) {
            return;
        }

        String key = ctx.SINGLE_STRING().getText();
        String value = ctx.value().getText();

        if (key.equals(Classroom.NAME)) {
            for (Classroom c : classrooms) {
                if (c.getName().equals(value)) {
                    active = false;
                    System.out.println("Classroom name is already used");
                    return;
                }
            }
        }

        if (ctx.value().getRuleIndex() == VALUE_ARRAY) {
            List<String> valueArray = asArray(value);
            if (!current.addArray(key, valueArray)) {
                active = false;
            }
        } else {
            if (!current.addString(key, value)) {
                active = false;
            }
        }
    }

    @Override
    public void exitQuery(SchedulingParser.QueryContext ctx) {
        super.exitQuery(ctx);
        if (current.check() && active) {
            classrooms.add(current);
        }
    }

    private List<String> asArray(String s) {
        // Array is empty
        if (s.length()<=2) {
            return new ArrayList<>();
        }

        s = s.substring(1, s.length()-2);
        return new ArrayList<>(Arrays.asList(s.split(",")));
    }

    public void print() {
        for (Classroom c:classrooms) {
            c.print();
        }
    }
}
