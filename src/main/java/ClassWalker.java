import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassWalker extends SchedulingBaseListener {
    private List<Class> classes;
    private Class current;
    private boolean active;
    private String currentKey;
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
        current = new Class();
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

        if (!current.addString(currentKey, value)) {
            active = false;
        }
    }

    @Override
    public void exitQuery(SchedulingParser.QueryContext ctx) {
        super.exitQuery(ctx);
        for (Class c : classes) {
            if (c.getId().equals(current.getCode()+'-'+current.getNumber())) {
                active = false;
                System.out.println("Classroom id (code+number) is already used");
            }
        }
        if (current.check() && active) {
            classes.add(current);
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

        String name = ctx.getText();
        for (Class c : classes) {
            if (c.getName().equals(name)) {
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

        if (class1 != null && class2 != null && class1.getGrade() != class2.getGrade()
                && !class1.getName().equals(class2.getName())) {
            for (Class c1 : classes) {
                for (Class c2 : classes) {
                    if (c1.getName().equals(class1.getName()) && c2.getName().equals(class2.getName())) {
                        c1.addClash(c2.getId());
                    } else if (c1.getName().equals(class2.getName()) && c2.getName().equals(class1.getName())) {
                        c1.addClash(c2.getId());
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
