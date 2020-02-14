package com.ie.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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
        String result = parser.parse(invalid);
        assertNull(result);
    }

    @Test
    @DisplayName("handles null input")
    void handleNull(){
        DependencyParser parser = new DependencyParser();
        String result = parser.parse(null);
        assertNull(result);
    }

    @Test
    @DisplayName("handles empty input")
    void handleEmpty(){
        DependencyParser parser = new DependencyParser();
        String result = parser.parse(new String[]{});
        assertNull(result);
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
