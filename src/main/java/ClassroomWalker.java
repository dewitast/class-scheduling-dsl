import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassroomWalker extends SchedulingBaseListener {
    private List<Classroom> classrooms;
    private Classroom current;
    private boolean active;
    private String currentKey;

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

        currentKey = ctx.SINGLE_STRING().getText();
    }

    @Override
    public void enterString(SchedulingParser.StringContext ctx) {
        super.enterString(ctx);

        if (!active) {
            return;
        }

        String value = ctx.getText();

        if (currentKey.equals(Classroom.NAME)) {
            for (Classroom c : classrooms) {
                if (c.getName().equals(value)) {
                    active = false;
                    System.out.println("Classroom name is already used");
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
            classrooms.add(current);
        }
    }

    public void print() {
        for (Classroom c:classrooms) {
            c.print();
        }
    }
}
