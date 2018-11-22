import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String CLASSROOM = "classroom";
        String CLASS = "class";
        String LECTURER = "lecturer";
        String PROJECT = "project";
        ClassroomWalker classroomWalker = new ClassroomWalker();
        ClassWalker classWalker = new ClassWalker();
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;
        String input;

        while (!quit) {
            System.out.print("> ");
            input = scanner.nextLine();
            if (input.isEmpty()) {
                continue;
            }

            SchedulingLexer lexer = new SchedulingLexer(new ANTLRInputStream(input));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SchedulingParser parser = new SchedulingParser(tokens);

            ParseTree tree = parser.query();
            String obj = ((SchedulingParser.QueryContext) tree).method().parameter_create().getText();

            ParseTreeWalker walker = new ParseTreeWalker();
            if (obj.equals(CLASSROOM)) {
                walker.walk(classroomWalker, tree);
                classroomWalker.print();
            } else if (obj.equals(CLASS)) {
                walker.walk(classWalker, tree);
                classWalker.print();
            } else if (obj.equals(LECTURER)) {

            } else if (obj.equals(PROJECT)) {

            } else {

            }

            classroomWalker.print();

            if (input.toLowerCase().equals("quit")) {
                quit = true;
            }
        }
    }
}
