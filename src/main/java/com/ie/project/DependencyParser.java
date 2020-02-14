package com.ie.project;

import java.util.*;

public class DependencyParser {

    public String parse(String[] dependencies){
        if(dependencies == null || dependencies.length < 1){
            return null;
        }
        Map<String, Set<String>> adjList = buildAdjacencyList(dependencies);
        return createDependencyString(adjList);
    }

    private Map<String, Set<String>> buildAdjacencyList(String[] dependencies){
        Map<String, Set<String>> adjList = new HashMap<>();
        for(String dep : dependencies){
            String[] pair = dep.split(": ");
            String lib = pair[0];
            String dependency = pair.length > 1 ? pair[1] : "";

            //Check for a cycle in the dependencies
            if(adjList.containsKey(lib)){
                Set<String> visited = new HashSet<>();
                String check = lib;
                addDependency(dependency, lib, adjList);
                while(true){
                    if(visited.contains(check)){
                        return null;
                    }
                    if(!adjList.containsKey(check)){
                        break;
                    }
                    visited.add(check);
                    check = adjList.get(check).iterator().next();
                }
            }else{
                addDependency(dependency, lib, adjList);
            }
        }
        return adjList;
    }

    private void addDependency(String dependency, String library, Map<String, Set<String>> adjList){
        Set<String> libs = adjList.getOrDefault(dependency, new HashSet<>());
        libs.add(library);
        adjList.put(dependency, libs);
    }

    private String createDependencyString(Map<String, Set<String>> adjList) {
        StringBuilder sb = new StringBuilder();
        Set<String> heads = adjList.get("");

        for(String lib : heads){
            String current = lib;
            while(current != null){
                if(adjList.get(current) != null){
                    for (String dep : adjList.get(current)) {
                        if(dep.equals("")){
                            current = null;
                        } else {
                            sb.append(current).append(", ");
                            current = dep;
                        }
                    }
                }else{
                    sb.append(current).append(", ");
                    current = null;
                }
            }
        }
        if(sb.length() > 2) sb.setLength(sb.length()-2);
        return sb.toString();
    }
}
