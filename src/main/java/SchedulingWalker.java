public class SchedulingWalker extends SchedulingBaseListener {

    public void enterQuery(SchedulingParser.QueryContext ctx ) {
//        System.out.println( "Entering Query : " + ctx.method().parameter_method().getText() );
//        for (SchedulingParser.FeatureContext f : ctx.feature()) {
//            System.out.println( "Entering Query : " + f.SINGLE_STRING().getText() + ": " + f.value().getText() );
//        }
    }

    public void exitQuery(SchedulingParser.QueryContext ctx ) {
        System.out.println( "Exiting Query" );
    }
}
