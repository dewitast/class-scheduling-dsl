import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassroomWalker extends SchedulingBaseListener {
    private List<Classroom> classrooms;
    private Classroom current;
    private boolean active;
    private String currentKey;
    private String currentFunction;
    private String currentTargetKey;

    public ClassroomWalker() {
        classrooms = new ArrayList<>();
    }

    public List<Classroom> getClassrooms() {
        return classrooms;
    }

    @Override
    public void enterQuery(SchedulingParser.QueryContext ctx) {
        super.enterQuery(ctx);
        active = true;
    }

    @Override
    public void enterFunction(SchedulingParser.FunctionContext ctx) {
        super.enterFunction(ctx);
        if (!active) {
            return;
        }
        currentFunction = ctx.getText();
        if (currentFunction.equals("show")){
            print();
        } else if (currentFunction.equals("create")){
            current = new Classroom();
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
    public void enterString(SchedulingParser.StringContext ctx) {
        super.enterString(ctx);

        if (!active) {
            return;
        }

        String value = ctx.getText();
        if (currentFunction.equals("create")){
            if (currentKey.equals(Classroom.NAME)) {
                for (Classroom c : classrooms) {
                    if (c.getName().equals(value)) {
                        active = false;
                        System.out.println("Classroom name is already used");
                        return;
                    }
                }
            }
        } else if (currentFunction.equals("update")) {
            if (currentKey.equals(Classroom.CAPACITY)) {
                for (Classroom c : classrooms) {
                    if (c.getName().equals(currentTargetKey)) {
                        c.addString(currentKey,value);
                    }
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
        if (currentFunction.equals("create")) {
            if (current.check() && active) {
                classrooms.add(current);
            }
        } else if (currentFunction.equals("delete")){
            Classroom deletedClassroom = new Classroom();
            for (Classroom c : classrooms) {
                if (c.getName().equals(currentTargetKey)) {
                    //classroom name is primary key, only found exactly 1
                    deletedClassroom = c;
                }
            }
            classrooms.remove(deletedClassroom);
        }

    }

    public void print() {
        for (Classroom c:classrooms) {
            c.print();
        }
    }
}
