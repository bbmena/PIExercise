package com.ie.project;

import java.util.*;

public class DependencyParser {

    public String parse(String[] dependencies){
        if(dependencies == null || dependencies.length < 1){
            return null;
        }
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

        return createDepString(adjList);
    }

    private void addDependency(String dependency, String library, Map<String, Set<String>> adjList){
        Set<String> libs = adjList.getOrDefault(dependency, new HashSet<>());
        libs.add(library);
        adjList.put(dependency, libs);
    }

//    private String createDepString(Map<String, String> adjList) {
    private String createDepString(Map<String, Set<String>> adjList) {
//        List<String> depList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        Stack<String> stack = new Stack<>();
        String temp = adjList.keySet().iterator().next();
//        while(!adjList.isEmpty()){
//            if(adjList.get(temp).equals("")){
//                sb.append(temp).append(", ");
//                adjList.remove(temp);
//                temp = adjList.keySet().iterator().next();
//            } else{
//                stack.push(temp);
//                stack.push(adjList.get(temp));
//                adjList.remove(temp);
//            }
//            while(!stack.isEmpty()){
//                if(adjList.containsKey(stack.peek())){
//                    stack.push(adjList.get(stack.peek()));
//                    adjList.remove(stack.peek());
//                } else {
//                    sb.append(stack.pop()).append(", ");
//                }
//            }
//        }

        sb.setLength(sb.length()-2);
        return sb.toString();
    }
}
