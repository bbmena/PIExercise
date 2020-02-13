package com.ie.project;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DependencyParser {

    public String parse(String[] dependencies){
        if(dependencies == null || dependencies.length < 1){
            return null;
        }
        Map<String, String> adjList = new HashMap<>();
        for(String dep : dependencies){
            String[] pair = dep.split(": ");
            String lib = pair[0];
            String dependency = pair[1];

            //Check for a cycle in the dependencies
            if(adjList.containsKey(dependency)){
                Set<String> visited = new HashSet<>();
                String check = dependency;
                while(true){
                    if(visited.contains(check)){
                        return null;
                    }
                    if(!adjList.containsKey(check)){
                        break;
                    }
                    visited.add(check);
                    check = adjList.get(check);
                }
            }else{
                adjList.put(lib, dependency);
            }
        }

        return null;
    }
}
