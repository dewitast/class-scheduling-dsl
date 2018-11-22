import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.List;
import java.util.Scanner;

public class Main {
    static List<Class> classes;
    static int[] creditsLeft;
    static List<Classroom> classrooms;
    static List<Lecturer> lecturers;

    static Class[][][] classSchedule;
    static Lecturer[][][] lecturerSchedule;

    static int DAYS = 5;
    static int HOURS = 11;

    public static void main(String[] args) {
        String CLASSROOM = "classroom";
        String CLASS = "class";
        String LECTURER = "lecturer";
        String PROJECT = "project";
        String CONSTRAINT = "constraint";
        ClassroomWalker classroomWalker = new ClassroomWalker();
        ClassWalker classWalker = new ClassWalker();
        LecturerWalker lecturerWalker = new LecturerWalker();
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;
        String input;

        while (!quit) {
            System.out.print("> ");
            input = scanner.nextLine();
            if (input.isEmpty()) {
                continue;
            }
            if (input.toLowerCase().equals("quit")) {
                // quit program
                quit = true;
            } else if (input.length() > 10 && input.substring(0, 10).equals(CONSTRAINT)) {
                // add constraint
                SchedulingLexer lexer = new SchedulingLexer(new ANTLRInputStream(input));
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                SchedulingParser parser = new SchedulingParser(tokens);

                ParseTree tree = parser.constraint();
                ParseTreeWalker walker = new ParseTreeWalker();
                walker.walk(classWalker, tree);
                classWalker.print();
            } else if (input.toLowerCase().equals("schedule")) {
                // start scheduling
                classes = classWalker.getClasses();
                classrooms = classroomWalker.getClassrooms();
                lecturers = lecturerWalker.getLecturers();

                creditsLeft = new int[classes.size()];
                for (int i = 0; i < classes.size(); i++) {
                    creditsLeft[i] = classes.get(i).getCredit();
                }

                classSchedule = new Class[classrooms.size()][DAYS][HOURS];
                lecturerSchedule = new Lecturer[classrooms.size()][HOURS][HOURS];

                if (dfsClass(0, 0, 0)) {
                    System.out.println("succeed");
                } else {
                    System.out.println("failed");
                };
                printClass();
            } else {
                // create object
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
                    walker.walk(lecturerWalker, tree);
                    lecturerWalker.print();
                } else if (obj.equals(PROJECT)) {

                } else {

                }
            }
        }
    }

    static boolean dfsClass(int i, int j, int k) {
        boolean still = false;
        for (int x = 0; x < classes.size(); x++) {
            if (creditsLeft[x] > 0) {
                still = true;
                break;
            }
        }
        if (!still || i==classSchedule.length && j==DAYS && k==HOURS) {
            return true;
        } else {
            for (int x = 0; x < classes.size(); x++) {
                if (creditsLeft[x] > 0 && classrooms.get(i).getCapacity() >= classes.get(x).getSize()
                        && classrooms.get(i).getFacilities().containsAll(classes.get(x).getRequirements())) {
                    boolean can = true;
                    for (int y = 0; y < i; y++) {
                        if (classes.get(x).getClashes().contains(classSchedule[y][j][k].getId())) {
                            can = false;
                        }
                        if (classes.get(x).getGrade() == classSchedule[y][j][k].getGrade()
                                && classes.get(x).getNumber() == classSchedule[y][j][k].getNumber()) {
                            can = false;
                        }
                    }
                    if (can) {
                        --creditsLeft[x];
                        classSchedule[i][j][k] = classes.get(x);
                        for (Lecturer l : lecturers) {
                            if (l.getClasses().contains(classes.get(x).getCode()) && l.canTeachAt(j, k)) {
                                lecturerSchedule[i][j][k] = l;
                                if (k+1==HOURS) {
                                    if (j+1==DAYS) {
                                        if (i+1==classSchedule.length) {
                                            if (dfsClass(i+1, j+1, k+1)) return true;
                                        } else {
                                            if (dfsClass(i+1, 0, 0)) return true;
                                        }
                                    } else {
                                        if (dfsClass(i, j+1, 0)) return true;
                                    }
                                } else {
                                    if (dfsClass(i, j, k+1)) return true;
                                }
                                lecturerSchedule[i][j][k] = null;
                            }
                        }
                        classSchedule[i][j][k] = null;
                        ++creditsLeft[x];
                    }
                }
            }
            if (k+1==HOURS) {
                if (j+1==DAYS) {
                    if (i+1==classSchedule.length) {
                        return dfsClass(i+1, j+1, k+1);
                    } else {
                        return (dfsClass(i+1, 0, 0));
                    }
                } else {
                    return (dfsClass(i, j+1, 0));
                }
            } else {
                return (dfsClass(i, j, k+1));
            }
        }
    }

    static void printClass() {
        for (int i = 0; i < classSchedule.length; i++) {
            for (int j = 0; j < DAYS; j++) {
                for (int k = 0; k < HOURS; k++) {
                    if (classSchedule[i][j][k] != null) System.out.print('(' + classSchedule[i][j][k].getId() + ", " + lecturerSchedule[i][j][k].getName() + ')');
                    else System.out.print("- ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
