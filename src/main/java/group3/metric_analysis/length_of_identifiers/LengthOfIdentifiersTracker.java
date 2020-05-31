package group3.metric_analysis.length_of_identifiers;

import group3.MetricTracker;
import org.apache.commons.cli.*;
import spoon.Launcher;

/**
 * TODO: documentation
 * @author DanielSands
 */
public class LengthOfIdentifiersTracker extends MetricTracker {
   private LengthOfIdentifiersAnalysis lengthOfIdentifiersAnalysis;
   private int noteworhtyCutOffPoint = 5;

    public LengthOfIdentifiersTracker(String[] args) {
        Options options = new Options();
        options.addOption("min", true, "Noteworthy identifiers cutoff value to display");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(1);
        }

        String cutOffArg = cmd.getOptionValue("min");
        if (cutOffArg != null) {
            try {
                noteworhtyCutOffPoint = Integer.parseInt(cutOffArg);
            } catch (NumberFormatException e) {
                System.out.println("Length of identifier cutoff must be an integer value");
                System.exit(1);
            }
        }
        lengthOfIdentifiersAnalysis = new LengthOfIdentifiersAnalysis(noteworhtyCutOffPoint);
    }

    @Override
    public void run(Launcher launcher) {
        lengthOfIdentifiersAnalysis.performAnalysis(launcher);
    }

    @Override
    public String toJson() { //TODO: Maybe have this in toJson method in LoIAnalysis and then just call the function here, like in CommentsCount
        return "Class average scores: " + lengthOfIdentifiersAnalysis.getClassLengthOfIdentifiersScores().toString()
                + " Noteworthy identifiers" + lengthOfIdentifiersAnalysis.getNoteworthyLengthOfIdentifierScores(); //TODO: Decide how to go about showing notwortyLengthOfIdentifiers
    }
}
