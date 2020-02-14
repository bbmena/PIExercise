package com.ie.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PIExerciseTests {

    @ParameterizedTest(name = "parses dependencies")
    @MethodSource("rawAndValidProvider")
    void parseDependency(String[] raw, String valid){
        DependencyParser parser = new DependencyParser();
        String result = parser.parse(raw);
        assertEquals(valid, result);
    }

    @Test
    @DisplayName("no cycles allowed")
    void rejectCyclicDependency(){
        String[] invalid = new String[]{"KittenService: ",
                "Leetmeme: Cyberportal",
                "Cyberportal: Ice",
                "CamelCaser: KittenService",
                "Fraudstream: ",
                "Ice: Leetmeme"};
        DependencyParser parser = new DependencyParser();
        IllegalArgumentException exception = assertThrows(java.lang.IllegalArgumentException.class, () -> parser.parse(invalid));
        String expected = "Cyclic dependency found";
        assertTrue(exception.getMessage().contains(expected));
    }

    @Test
    @DisplayName("handles null input")
    void handleNull(){
        DependencyParser parser = new DependencyParser();
        IllegalArgumentException exception = assertThrows(java.lang.IllegalArgumentException.class, () -> parser.parse(null));
        String expected = "Dependency array may not be null or empty";
        assertTrue(exception.getMessage().contains(expected));
    }

    @Test
    @DisplayName("handles empty input")
    void handleEmpty(){
        DependencyParser parser = new DependencyParser();
        IllegalArgumentException exception = assertThrows(java.lang.IllegalArgumentException.class, () -> parser.parse(new String[]{}));
        String expected = "Dependency array may not be null or empty";
        assertTrue(exception.getMessage().contains(expected));
    }

    @Test
    @DisplayName("handles incomplete dependency listing")
    void handleIncomplete(){
        String[] invalid = new String[]{"KittenService: Ice"};
        DependencyParser parser = new DependencyParser();
        IllegalArgumentException exception = assertThrows(java.lang.IllegalArgumentException.class, () -> parser.parse(invalid));
        String expected = "All packages must have their required dependencies listed. ex: [\"kittens: ice\", \"ice: \"]";
        assertTrue(exception.getMessage().contains(expected));
    }

    static Stream<Arguments> rawAndValidProvider(){
        return Stream.of(
                Arguments.arguments(
                        new String[]{"KittenService: CamelCaser", "CamelCaser: "},
                        "CamelCaser, KittenService"
                ),
                Arguments.arguments(
                        new String[]{"KittenService: ", "Leetmeme: Cyberportal", "Cyberportal: Ice", "CamelCaser: KittenService", "Fraudstream: Leetmeme", "Ice: "},
                        "Ice, Cyberportal, Leetmeme, Fraudstream, KittenService, CamelCaser"
                ),
                Arguments.arguments(new String[]{""}, ""),
                Arguments.arguments(new String[]{"KittenService"}, "KittenService"),
                Arguments.arguments(new String[]{"KittenService: Ice", "Leetmeme: Ice", "Ice: "}, "Ice, Leetmeme, KittenService")
        );
    }
}
