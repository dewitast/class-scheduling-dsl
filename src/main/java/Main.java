import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<String> restrictedSchedule = new ArrayList<>();
    private static List<Class> classes;
    private static int[] creditsLeft;
    private static List<Classroom> classrooms;
    private static List<Lecturer> lecturers;

    private static Class[][][] classSchedule;
    private static Lecturer[][][] lecturerSchedule;

    private static int DAYS = 5;
    private static int HOURS = 11;

    public static void main(String[] args) {
        String CLASSROOM = "classroom";
        String CLASS = "class";
        String LECTURER = "lecturer";
        String PROJECT = "project";
        String CONSTRAINT = "constraint";
        String PREFERENCE = "preference";
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
            } else if (input.toLowerCase().indexOf("add "+CONSTRAINT)==0) {
                SchedulingLexer lexer = new SchedulingLexer(new ANTLRInputStream(input));
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                SchedulingParser parser = new SchedulingParser(tokens);

                ParseTree tree = parser.constraint();
                ParseTreeWalker walker = new ParseTreeWalker();
                if (input.contains("lecture unavailability")) {
                    walker.walk(lecturerWalker, tree);
                    lecturerWalker.print();
                } else if (input.contains("restricted hour")) {
                    String s = ((SchedulingParser.ConstraintContext) tree).constraint_type().schedule().getText();
                    restrictedSchedule.add(s);
                } else {
                    walker.walk(classWalker, tree);
                    classWalker.print();
                }
            } else if (input.toLowerCase().indexOf("add "+PREFERENCE)==0) {
                SchedulingLexer lexer = new SchedulingLexer(new ANTLRInputStream(input));
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                SchedulingParser parser = new SchedulingParser(tokens);

                ParseTree tree = parser.preference();
                ParseTreeWalker walker = new ParseTreeWalker();
                walker.walk(lecturerWalker, tree);
                lecturerWalker.print();
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

                generateSchedule();
                printClass();
                quit = true;
            } else {
                // create object
                SchedulingLexer lexer = new SchedulingLexer(new ANTLRInputStream(input));
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                SchedulingParser parser = new SchedulingParser(tokens);

                ParseTree tree = parser.query();
                String obj = ((SchedulingParser.QueryContext) tree).target().getText();

                ParseTreeWalker walker = new ParseTreeWalker();
                if (obj.equals(CLASSROOM)) {
                    walker.walk(classroomWalker, tree);
//                    classroomWalker.print();
                } else if (obj.equals(CLASS)) {
                    walker.walk(classWalker, tree);
                } else if (obj.equals(LECTURER)) {
                    walker.walk(lecturerWalker, tree);
                    lecturerWalker.print();
                } else if (obj.equals(PROJECT)) {

                } else {

                }
            }
        }
    }

    static void generateSchedule() {
        dfsClass(0, 0, 0);

        // find preferred schedule
        for (int i = 0; i < classSchedule.length; i++) {
            for (int j = 0; j < DAYS; j++) {
                for (int k = 0; k < HOURS; k++) {
                    if (classSchedule[i][j][k] != null && lecturerSchedule[i][j][k] != null) {
                        Lecturer l = lecturerSchedule[i][j][k];
                        Class c = classSchedule[i][j][k];
                        for (String p : l.getPreferences()) {
                            int d = p.charAt(0) - '0' - 1;
                            int h = (p.charAt(1) - '0') * 10 + p.charAt(2) - '0' - 1;
                            if (d==j && h==k) continue;
                            if (!l.canTeachAt(d, h)) continue;
                            if (restrictedSchedule.contains(p)) continue;
                            boolean can = true;
                            for (int x = 0; x < classrooms.size(); x++) {
                                if (lecturerSchedule[x][d][h] != null
                                        && !lecturerSchedule[x][d][h].getName().equals(l.getName())) {
                                    can = false;
                                    break;
                                }
                                Class now = classSchedule[x][d][h];
                                if (now != null && c.getClashes().contains(now.getId())) {
                                    can = false;
                                    break;
                                }
                                if (now != null && c.getGrade() == now.getGrade() && c.getNumber() == now.getNumber()) {
                                    can = false;
                                    break;
                                }
                            }
                            if (can) {
                                for (int x = 0; x < classrooms.size(); x++) {
                                    if (classSchedule[x][d][h] == null && classrooms.get(x).getCapacity() >= c.getSize()
                                            && classrooms.get(i).getFacilities().containsAll(c.getRequirements())) {
                                        classSchedule[x][d][h] = c;
                                        lecturerSchedule[x][d][h] = l;
                                        classSchedule[i][j][k] = null;
                                        lecturerSchedule[i][j][k] = null;
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
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
            String schedule = Integer.toString(j+1) + Integer.toString(k+1);
            if (schedule.length() < 3) {
                schedule = "" + schedule.charAt(0) + '0' + schedule.charAt(1);
            }
            if (!restrictedSchedule.contains(schedule)) {
                for (int x = 0; x < classes.size(); x++) {
                    if (creditsLeft[x] > 0 && classrooms.get(i).getCapacity() >= classes.get(x).getSize()
                            && classrooms.get(i).getFacilities().containsAll(classes.get(x).getRequirements())) {
                        boolean can = true;
                        for (int y = 0; y < i; y++) {
                            Class now = classSchedule[y][j][k];
                            if (now != null && classes.get(x).getClashes().contains(now.getId())) {
                                can = false;
                                break;
                            }
                            if (now != null && classes.get(x).getGrade() == now.getGrade()
                                    && classes.get(x).getNumber() == now.getNumber()) {
                                can = false;
                                break;
                            }
                        }
                        if (can) {
                            --creditsLeft[x];
                            classSchedule[i][j][k] = classes.get(x);
                            for (Lecturer l : lecturers) {
                                if (l.getClasses().contains(classes.get(x).getCode()) && l.canTeachAt(j, k)) {
                                    boolean avail = true;
                                    for (int y = 0; y < i; y++) {
                                        if (lecturerSchedule[y][j][k] != null
                                                && lecturerSchedule[y][j][k].getName().equals(l.getName())) {
                                            avail = false;
                                            break;
                                        }
                                    }
                                    if (avail) {
                                        lecturerSchedule[i][j][k] = l;
                                        if (k + 1 == HOURS) {
                                            if (j + 1 == DAYS) {
                                                if (i + 1 == classSchedule.length) {
                                                    if (dfsClass(i + 1, j + 1, k + 1)) return true;
                                                } else {
                                                    if (dfsClass(i + 1, 0, 0)) return true;
                                                }
                                            } else {
                                                if (dfsClass(i, j + 1, 0)) return true;
                                            }
                                        } else {
                                            if (dfsClass(i, j, k + 1)) return true;
                                        }
                                        lecturerSchedule[i][j][k] = null;
                                    }
                                }
                            }
                            classSchedule[i][j][k] = null;
                            ++creditsLeft[x];
                        }
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
