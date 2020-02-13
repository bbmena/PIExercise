package com.ie.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependencyParser {

    public String parse(String[] dependencies){
        if(dependencies == null || dependencies.length < 1){
            return null;
        }
        Map<String, List<String>> adjList = new HashMap<>();
        for(String dep : dependencies){
            String[] pair = dep.split(": ");
            String dependency = pair[1];
            List<String> dependants = adjList.getOrDefault(dependency, new ArrayList<>());
            dependants.add(pair[0]);
            adjList.put(dependency, dependants);
        }

        return null;
    }
}
