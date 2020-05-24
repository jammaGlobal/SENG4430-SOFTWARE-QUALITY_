package group3.metric_analysis.fan_out;

import group3.MetricTracker;
import org.apache.commons.cli.*;
import spoon.Launcher;

import java.util.HashMap;

public class FanOutTracker extends MetricTracker {

    private FanOutAnalysis fanOutAnalysis;

    private Integer lowerThreshold = 5;
    private Boolean moduleMode = false;

    public FanOutTracker(String[] args) {
        Options options = new Options();
        options.addOption("min", true, "Lower threshold of fan out value to display");
        options.addOption("module", false, "Calculate fan out for module rather than methods");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(1);
        }

        String lowerThresholdArg = cmd.getOptionValue("min");
        if (lowerThresholdArg != null) {
            try {
                lowerThreshold = Integer.parseInt(lowerThresholdArg);
            } catch (NumberFormatException e) {
                System.out.println("Fan out lower threshold must be an integer value");
                System.exit(1);
            }
        }
        if (cmd.hasOption("module")) {
            moduleMode = true;
        }
        fanOutAnalysis = new FanOutAnalysis();
    }

    @Override
    public void run(Launcher launcher) {
        fanOutAnalysis.performAnalysis(launcher);
    }

    @Override
    public String toJson() {
        if (moduleMode) {
            return moduleModeToJSON();
        } else {
            return methodModeToJSON();
        }

    }

    private String methodModeToJSON() {
        HashMap<String, HashMap<String, Integer>> scores = new HashMap<>();
        HashMap<String, HashMap<String, Integer>> rawScores = fanOutAnalysis.getMethodModeScores();
        System.out.println(rawScores);
        for (HashMap.Entry<String, HashMap<String, Integer>> classMap : rawScores.entrySet()) {
            String className = classMap.getKey();
            HashMap<String, Integer> methodScores = new HashMap<>();
            for (HashMap.Entry<String, Integer> methodMap : classMap.getValue().entrySet()) {
                String methodName = methodMap.getKey();
                Integer methodScore = methodMap.getValue();
                if (methodScore >= lowerThreshold) {
                    methodScores.put(methodName, methodScore);
                    System.out.println(methodScore);
                }
            }
            if (!methodScores.isEmpty()) {
                scores.put(className, methodScores);
            }
        }
        return scores.toString();
    }

    private String moduleModeToJSON() {
        HashMap<String, Integer> scores = new HashMap<>();
        HashMap<String, Integer> rawScores = fanOutAnalysis.getModuleModeScores();
        System.out.println(rawScores);
        for (HashMap.Entry<String, Integer> classMap : rawScores.entrySet()) {
            String className = classMap.getKey();
            Integer moduleScore = classMap.getValue();
            if (moduleScore >= lowerThreshold) {
                scores.put(className, moduleScore);
                System.out.println(moduleScore);
            }
        }
        return scores.toString();
    }
}
