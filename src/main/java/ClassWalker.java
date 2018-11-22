import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassWalker extends SchedulingBaseListener {
    private List<Class> classes;
    private Class current;
    private boolean active;
    private String currentKey;

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

    public void print() {
        for (Class c:classes) {
            c.print();
        }
    }
}
