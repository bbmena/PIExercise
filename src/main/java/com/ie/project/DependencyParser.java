package com.ie.project;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DependencyParser {

    /**
     * Will convert an array of dependencies into a string of package names in the order they should be installed
     * @param dependencies array of strings containing names of packages and their dependencies
     * @return string of all package names and the order they should be installed in
     * @throws IllegalArgumentException if incorrectly formatted input is submitted, or a cyclic dependency is found
     */
    public String parse(String[] dependencies) throws IllegalArgumentException{
        if(dependencies == null || dependencies.length < 1){
            throw new IllegalArgumentException("Dependency array may not be null or empty");
        }
        Map<String, Set<String>> adjList = buildAdjacencyList(dependencies);
        return createDependencyString(adjList);
    }

    private Map<String, Set<String>> buildAdjacencyList(String[] dependencies) throws IllegalArgumentException{
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
                        throw new IllegalArgumentException("Cyclic dependency found");
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
        //There should be at least one library without any dependencies
        if(heads == null) throw new IllegalArgumentException("All packages must have their required dependencies listed. ex: [\"kittens: ice\", \"ice: \"]");

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
