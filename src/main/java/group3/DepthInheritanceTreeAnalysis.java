package group3;


import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.visitor.Query;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class DepthInheritanceTreeAnalysis extends MetricAnalysis {
    private HashMap<String, Integer> visited_classes;
    private HashMap<String, CtClass<?>> ctClasses;


    public DepthInheritanceTreeAnalysis() {
        visited_classes = new HashMap<String, Integer>();
        ctClasses = new HashMap<String, CtClass<?>>();
    }

    public MetricReturn performAnalysis (String fileName) {

        Launcher launcher = Utilities.importCodeSample(fileName);
        List<CtClass<?>> classes = Query.getElements(launcher.getFactory(), new TypeFilter<CtClass<?>>(CtClass.class));

        for (CtClass<?> c : classes) {
            ctClasses.put(c.getQualifiedName(), c);
        }

        int maxDepth = 0;
        int currentDepth;
        for (CtClass<?> c : classes) {
            if (!visited_classes.containsKey(c.getQualifiedName())) {
                currentDepth = depthInheritanceRecursive(c);

                maxDepth= (currentDepth > maxDepth) ? currentDepth : maxDepth;
            }
        }


        DepthInheritanceTreeReturn metricResults = new DepthInheritanceTreeReturn();
        metricResults.setMaxDepth(maxDepth);

        return metricResults;
    }

    private int depthInheritanceRecursive(CtClass<?> currentClass) {

        if (!visited_classes.containsKey(currentClass.getQualifiedName())) {
            visited_classes.put(currentClass.getQualifiedName(), 0);
        } else {
            return visited_classes.get(currentClass.getQualifiedName());
        }


        if (currentClass.getSuperclass() != null) {
            CtClass<?> superClass = ctClasses.get(currentClass.getSuperclass().getQualifiedName());

            int thisClassDepth = 1 + depthInheritanceRecursive(superClass);
            visited_classes.put(currentClass.getQualifiedName(), thisClassDepth);
            return thisClassDepth;
        }

        // current class has no super class
        return 0;
    }


}
