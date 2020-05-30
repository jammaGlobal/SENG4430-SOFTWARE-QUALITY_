package group3;

import group3.metric_analysis.comments_counts.CommentsCountTracker;
import group3.metric_analysis.conditional_nesting.ConditionalNestingTracker;
import group3.metric_analysis.depth_inheritance_tree.DepthInheritanceTreeTracker;
import group3.metric_analysis.fog_index.FogIndexTracker;
import group3.metric_analysis.fan_in.FanInTracker;
import group3.metric_analysis.halstead_complexity.HalsteadComplexityTracker;
import group3.metric_analysis.lack_of_cohesion.LackOfCohesionTracker;
import group3.metric_analysis.fan_out.FanOutTracker;
import group3.metric_analysis.coupling.CouplingTracker;
import group3.metric_analysis.length_of_identifiers.LengthOfIdentifiersTracker;
import spoon.Launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Metrics {
    private ArrayList<MetricTracker> metricTrackers;

    public Metrics (String[] metricDefinitions) {
        metricTrackers = new ArrayList<>();
        for (String def : metricDefinitions) {

            String arr[] = def.split(" ");
            MetricTracker tracker;
            switch (arr[0]) {
                case "inheritance_depth":
                    tracker = new DepthInheritanceTreeTracker(Arrays.copyOfRange(arr, 1, arr.length));
                    break;
                case "cohesion_score":
                    tracker = new LackOfCohesionTracker(Arrays.copyOfRange(arr, 1, arr.length));
                    break;
                case "depth_conditional_nesting":
                    tracker = new ConditionalNestingTracker(Arrays.copyOfRange(arr, 1, arr.length));
                    break;
                case "fan_in":
                    tracker = new FanInTracker(Arrays.copyOfRange(arr, 1, arr.length));
                    break;
                case "fan_out":
                    tracker = new FanOutTracker(Arrays.copyOfRange(arr, 1, arr.length));
                    break;
                case "comments_count":
                    tracker = new CommentsCountTracker(Arrays.copyOfRange(arr, 1, arr.length));
                    break;
                case "coupling":
                    tracker = new CouplingTracker(Arrays.copyOfRange(arr, 1, arr.length));
                    break;
                case "length_of_identifiers":
                    tracker = new LengthOfIdentifiersTracker(Arrays.copyOfRange(arr, 1, arr.length));
                    break;
                case "halstead_complexity":
                    tracker = new HalsteadComplexityTracker(Arrays.copyOfRange(arr, 1, arr.length));
                    break;
                case "fog_index":
                    tracker = new FogIndexTracker(Arrays.copyOfRange(arr, 1, arr.length));
                    break;
                default:
                    throw new IllegalArgumentException("Metric " + arr[0] + " is invalid");

            }
            metricTrackers.add(tracker);

        }
    }

    public void runMetrics(Launcher launcher, Launcher launcherNoComments) {
        for (MetricTracker tracker : metricTrackers) {
            if (!tracker.includeComments()) {
                tracker.run(launcherNoComments);
            } else {
                tracker.run(launcher);
            }

        }
    }

    public ArrayList<String> getResults() {
        ArrayList<String> results = new ArrayList<String>();
        for (MetricTracker tracker : metricTrackers) {
            results.add(tracker.toJson());
        }
        return results;
    }

    public List<MetricTracker> getMetricTrackers() {
        return metricTrackers;
    }
}
