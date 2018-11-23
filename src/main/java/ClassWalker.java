import java.util.ArrayList;
import java.util.List;

public class ClassWalker extends SchedulingBaseListener {
    private List<Class> classes;
    private Class current;
    private boolean active;
    private String currentKey;
    private String currentFunction;
    private String currentTargetKey;
    private String currentUpdateKey;
    private Class class1;
    private Class class2;

    public ClassWalker() {
        classes = new ArrayList<>();
    }

    public List<Class> getClasses() {
        return classes;
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
        if(!active) {
            return;
        }
        currentFunction = ctx.getText();
        if (currentFunction.equals("show")) {
            print();
        } else if (currentFunction.equals("create")) {
            current = new Class();
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
            if (!current.addString(currentKey, value)) {
                active = false;
            }
        } else if (currentFunction.equals("update")) {
            if (!currentTargetKey.equals("")) {
                boolean hasClass = false;
                if (currentKey.equals(Class.REQUIREMENTS)) {
                    if (currentUpdateKey.equals("add")) {
                        for (Class c : classes) {
                            if (c.getId().equals(currentTargetKey)) {
                                c.addString(currentKey, value);
                                System.out.println("class's requirements has been updated");
                                hasClass = true;
                            }
                        }
                        if (!hasClass) {
                            System.out.println("There's no class with id " + currentTargetKey);
                        }
                    } else if (currentUpdateKey.equals("remove")) {
                        String deletedReq = "";
                        for (Class c : classes) {
                            if (c.getId().equals(currentTargetKey)) {
                                //class id is primary key, only found exactly 1
                                for (String r : c.getRequirements()) {
                                    if (value.equals(r)) {
                                        deletedReq = r;
                                    }
                                }
                                if (deletedReq.equals("")) {
                                    System.out.println("Class " + currentTargetKey + " has no requirement " + value);
                                } else {
                                    c.getRequirements().remove(deletedReq);
                                    System.out.println("Requirement " + deletedReq + " from class " + currentTargetKey + " has been deleted");
                                }
                                hasClass = true;
                            }
                        }
                        if (!hasClass) {
                            System.out.println("There's no class with id " + currentTargetKey);
                        }
                    }
                } else if (currentKey.equals(Class.SIZE)) {
                    for (Class c : classes) {
                        if (c.getId().equals(currentTargetKey)) {
                            c.addString(currentKey, value);
                            System.out.println("class's size has been updated");
                        }
                    }
                } else {
                    System.out.println(currentKey + " cannot be updated");
                }
            } else {
                System.out.println("Class has no id");
            }
        } else {
            System.out.println(currentFunction + "is not a class's function");
        }
    }

    @Override
    public void exitQuery(SchedulingParser.QueryContext ctx) {
        super.exitQuery(ctx);
        if (currentFunction.equals("create")) {
            for (Class c : classes) {
                if (c.getId().equals(current.getCode() + '-' + current.getNumber())) {
                    active = false;
                    System.out.println("Classroom id (code+number) is already used");
                }
            }
            if (current.check() && active) {
                classes.add(current);
            }
        } else if (currentFunction.equals("delete")) {
            Class deletedClass = new Class();
            for (Class c : classes) {
                if (c.getId().equals(currentTargetKey)) {
                    //class name is primary key, only found exactly 1
                    deletedClass = c;
                }
            }
            if (deletedClass.getName().equals("")) {
                System.out.println("There are no class with id " + currentTargetKey);
            } else {
                classes.remove(deletedClass);
                System.out.println("class " + deletedClass.getId() + " has been deleted");
            }
        }
    }

    @Override
    public void enterConstraint(SchedulingParser.ConstraintContext ctx) {
        super.enterConstraint(ctx);
        class1 = null;
        class2 = null;
    }

    @Override
    public void enterClass_string(SchedulingParser.Class_stringContext ctx) {
        super.enterClass_string(ctx);

        String code = ctx.getText();
        for (Class c : classes) {
            if (c.getCode().equals(code)) {
                if (class1 == null) {
                    class1 = c;
                } else {
                    class2 = c;
                }
                break;
            }
        }
    }

    @Override
    public void exitConstraint(SchedulingParser.ConstraintContext ctx) {
        super.exitConstraint(ctx);
        if (class1 != null && class2 != null && !class1.getCode().equals(class2.getCode())) {
            for (Class c1 : classes) {
                for (Class c2 : classes) {
                    if (c1.getCode().equals(class1.getCode()) && c2.getCode().equals(class2.getCode())) {
                        if (c1.getGrade() != c2.getGrade()) c1.addClash(c2.getId());
                        else if (c1.getNumber() != c2.getNumber()) c1.addClash(c2.getId());
                    } else if (c1.getCode().equals(class2.getCode()) && c2.getCode().equals(class1.getCode())) {
                        if (c1.getGrade() != c2.getGrade()) c1.addClash(c2.getId());
                        else if (c1.getNumber() != c2.getNumber()) c1.addClash(c2.getId());
                    }
                }
            }
        }
    }

    public void print() {
        for (Class c:classes) {
            c.print();
        }
    }
}
