import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Main {
    public static void main( String[] args) {
        SchedulingLexer lexer = new SchedulingLexer( new ANTLRInputStream("create classroom(name:veren,capacity:50,)"));
        CommonTokenStream tokens = new CommonTokenStream( lexer );
        SchedulingParser parser = new SchedulingParser( tokens );
        ParseTree tree = parser.query();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk( new SchedulingWalker(), tree );
    }
}
