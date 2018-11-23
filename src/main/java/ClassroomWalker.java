import java.util.ArrayList;
import java.util.List;

public class ClassroomWalker extends SchedulingBaseListener {
    private List<Classroom> classrooms;
    private Classroom current;
    private boolean active;
    private String currentKey;
    private String currentFunction;
    private String currentTargetKey;
    private String currentUpdateKey;

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
            if (!current.addString(currentKey, value)) {
                active = false;
            }
        } else if (currentFunction.equals("update")) {
            if (!currentTargetKey.equals("")) {
                boolean hasClass = false;
                if (currentUpdateKey.equals("add")) {
                    if (currentKey.equals(Classroom.FACILITIES)) {
                        for (Classroom c : classrooms) {
                            if (c.getName().equals(currentTargetKey)) {
                                c.addString(currentKey, value);
                                System.out.println("classroom's facilities has been updated");
                                hasClass = true;
                            }
                        }
                        if (!hasClass) {
                            System.out.println("There's no classroom with name " + currentTargetKey);
                        }
                    }
                } else if (currentUpdateKey.equals("remove")) {
                    if (currentKey.equals(Classroom.FACILITIES)) {
                        String deletedFacility = "";
                        for (Classroom c : classrooms) {
                            if (c.getName().equals(currentTargetKey)) {
                                //classroom name is primary key, only found exactly 1
                                for (String f : c.getFacilities()) {
                                    if (value.equals(f)) {
                                        deletedFacility = f;
                                    }
                                }
                                if (deletedFacility.equals("")) {
                                    System.out.println("Classroom " + currentTargetKey + " has no facility " + value);
                                } else {
                                    c.getFacilities().remove(deletedFacility);
                                    System.out.println("Facility " + deletedFacility + " from classroom " + currentTargetKey + " has been deleted");
                                }
                                hasClass = true;
                            }
                        }
                        if (!hasClass) {
                            System.out.println("There's no classroom with id " + currentTargetKey);
                        }
                    }
                } else if (currentKey.equals(Classroom.CAPACITY)) {
                    for (Classroom c : classrooms) {
                        if (c.getName().equals(currentTargetKey)) {
                            c.addString(currentKey, value);
                            System.out.println("classroom's capacity has been updated");
                        }
                    }
                } else {
                    System.out.println(currentKey + " cannot be updated");
                }
            } else {
                System.out.println("Classroom has no name");
            }
        } else {
            System.out.println(currentFunction + "is not a clasroom's function");
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
            if (deletedClassroom.getName().equals("")) {
                System.out.println("There are no clasroom with name " + currentTargetKey);
            } else {
                classrooms.remove(deletedClassroom);
                System.out.println("classroom " + deletedClassroom.getName() + " has been deleted");
            }
        }

    }

    public void print() {
        for (Classroom c:classrooms) {
            c.print();
        }
    }
}
